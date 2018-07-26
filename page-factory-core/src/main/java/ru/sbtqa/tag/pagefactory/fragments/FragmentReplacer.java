package ru.sbtqa.tag.pagefactory.fragments;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;

public class FragmentReplacer {

    private List<CucumberFeature> features;
    private Map<String, ScenarioDefinition> fragmentsMap;
    private MutableGraph<Object> fragmentsGraph;
    private Map<ScenarioDefinition, String> scenarioLanguageMap;

    public FragmentReplacer(List<CucumberFeature> features) {
        this.features = FragmentCacheUtils.cacheFragmentsAsList(this.getClass(), features);
        this.scenarioLanguageMap = FragmentCacheUtils.cacheScenarioLanguage(this.features);
        this.fragmentsMap = FragmentCacheUtils.cacheFragmentsAsMap(this.features);
        this.fragmentsGraph = FragmentCacheUtils.cacheFragmentsAsGraph(this.features, fragmentsMap, scenarioLanguageMap);
    }

    /**
     * The graph of insertion of fragments is transposing and reversing. Starting from the lower-level elements,
     * fragments are inserting into the upper levels.
     *
     * @throws IllegalAccessException if it was not possible to replace a step with a fragment
     * @throws FragmentException if it is impossible to find the fragment by the specified name
     */
    public void replace() throws IllegalAccessException, FragmentException {
        Set<EndpointPair<Object>> edgesSet = Graphs.transpose(fragmentsGraph).edges();
        ArrayList<EndpointPair<Object>> edgesList = new ArrayList<>(edgesSet);
        Collections.reverse(edgesList);

        for (EndpointPair edge : edgesList) {
            ScenarioDefinition scenario = (ScenarioDefinition) edge.nodeV();
            replaceInScenario(scenario);
        }
    }

    private void replaceInScenario(ScenarioDefinition scenario) throws FragmentException, IllegalAccessException {
        String language = scenarioLanguageMap.get(scenario);
        List<Step> replacementSteps = replaceSteps(scenario.getSteps(), language);

        FieldUtils.writeField(scenario, "steps", replacementSteps, true);
    }

    /**
     * Bypass the passed list of steps and if a step is found that should be replaced with a fragment, then take the scenario
     * name, find its steps and substitute them for a step that requires a replacement for the fragment
     *
     * @param steps list of steps in which the search for a step requiring replacement is performed on a fragment
     * @param language steps language
     * @return the list of steps with the substituted fragments
     * @throws IllegalAccessException If can not copy the location from the replaced step to the steps of the fragment
     * @throws FragmentException if the fragment with the required name is not found
     */
    private List<Step> replaceSteps(List<Step> steps, String language) throws IllegalAccessException, FragmentException {
        List<Step> replacementSteps = new ArrayList<>();

        for (Step step : steps) {
            if (FragmentUtils.isStepFragmentRequire(step, language)) {
                replacementSteps.addAll(replaceStep(step, language));
            } else {
                replacementSteps.add(step);
            }
        }

        return replacementSteps;
    }

    private List<Step> replaceStep(Step stepToReplace, String language) throws IllegalAccessException, FragmentException {
        List<Step> replacementSteps = new ArrayList<>();
        String fragmentName = FragmentUtils.getFragmentName(stepToReplace, language);
        List<Step> replacementStepsDraft = fragmentsMap.get(fragmentName).getSteps();

        if (replacementStepsDraft == null) {
            throw new FragmentException(String.format("Can't find scenario fragment with name \"%s\"", fragmentName));
        }

        for (Step replacementStep : replacementStepsDraft) {
            copyLocation(stepToReplace, replacementStep);
            FragmentDataTableUtils.applyDataTable(stepToReplace, replacementStep);

            replacementSteps.add(replacementStep);
        }

        return replacementSteps;
    }

    private void copyLocation(Step originalStep, Step targetStep) throws IllegalAccessException {
        FieldUtils.writeField(targetStep, "location", originalStep.getLocation(), true);
    }
}
