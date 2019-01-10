package ru.sbtqa.tag.pagefactory.fragments;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableGraph;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;

public class FragmentReplacer {

    private List<CucumberFeature> features;
    private MutableGraph<Object> fragmentsGraph;
    private Map<ScenarioDefinition, String> scenarioLanguageMap;

    public FragmentReplacer(List<CucumberFeature> features) throws FragmentException {
        this.features = FragmentCacheUtils.cacheFragmentsToFeatures(this.getClass(), features);
        this.scenarioLanguageMap = FragmentCacheUtils.cacheScenarioLanguage(this.features);
        Map<String, ScenarioDefinition> fragmentsMap = FragmentCacheUtils.cacheFragmentsAsMap(this.features);
        this.fragmentsGraph = FragmentCacheUtils.cacheFragmentsAsGraph(this.features, fragmentsMap, scenarioLanguageMap);
    }

    /**
     * In the graph, terminal fragments are searched and substituted cyclically.
     * The cycle will be completed when all fragments are substituted and there are no edges left in the graph.
     *
     * @throws IllegalAccessException if it was not possible to replace a step with a fragment
     * @throws FragmentException if fragments replacing is in an infinite loop
     */
    public void replace() throws IllegalAccessException, FragmentException {
        ArrayList<EndpointPair<Object>> edgesList = new ArrayList<>(fragmentsGraph.edges());


        while (!fragmentsGraph.edges().isEmpty()) {
            int fragmentsGraphSize = fragmentsGraph.edges().size();

            for (EndpointPair edge : edgesList) {
                ScenarioDefinition fragment = (ScenarioDefinition) edge.nodeV();
                ScenarioDefinition scenario = (ScenarioDefinition) edge.nodeU();

                if (isTerminal(fragment)) {
                    replaceFragmentInScenario(scenario, fragment);
                    fragmentsGraph.removeEdge(scenario, fragment);
                }
            }

            if (fragmentsGraphSize == fragmentsGraph.edges().size()) {
                throw new FragmentException("Fragments replacing is no longer performed, it will lead to an infinite loop. Interrupting...");
            }
        }
    }

    /**
     * In case of graph
     *
     * a -> b
     * |
     * -> c
     * |
     * d -> e -> f
     *
     * nodes 'a' and 'd' is terminal
     *
     * @param node
     * @return
     */
    private boolean isTerminal(Object node) {
        for (EndpointPair edge : fragmentsGraph.edges()) {
            if (edge.nodeU().equals(node)) {
                return false;
            }
        }

        return true;
    }

    private void replaceFragmentInScenario(ScenarioDefinition scenario, ScenarioDefinition fragment) throws FragmentException, IllegalAccessException {
        String language = scenarioLanguageMap.get(scenario);
        List<Step> replacementSteps = new ArrayList<>();

        for (Step step : scenario.getSteps()) {
            if (FragmentUtils.isStepFragmentRequire(step, language)
                    && FragmentUtils.getFragmentName(step, language).equals(fragment.getName())) {
                replacementSteps.addAll(replaceStepWithFragment(step, fragment));
            } else {
                replacementSteps.add(step);
            }
        }

        FieldUtils.writeField(scenario, "steps", replacementSteps, true);
    }

    private List<Step> replaceStepWithFragment(Step stepToReplace, ScenarioDefinition fragment) throws FragmentException {
        List<Step> replacementSteps = fragment.getSteps();
        StepReplacer stepReplacer = new StepReplacer(stepToReplace);

        return stepReplacer.replaceWith(replacementSteps);
    }
}