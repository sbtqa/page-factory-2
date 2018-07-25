package ru.sbtqa.tag.pagefactory.fragments;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.qautils.i18n.I18N;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;

public class FragmentReplacer {

    private static final String FRAGMENT_STEP_REGEX_KEY = "ru.sbtqa.tag.pagefactory.insertFragment";
    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    private String language;
    private List<CucumberFeature> features;
    private Map<String, List<Step>> fragments;


    private List<CucumberFeature> featuresAndFragments = new ArrayList<>();
    private Map<String, ScenarioDefinition> fragments1;
    private MutableGraph<Object> graph = GraphBuilder.directed().allowsSelfLoops(false).build();

    public FragmentReplacer(List<CucumberFeature> features) {
        this.features = features;
        this.fragments = FragmentCacheUtils.cacheFragments(getFragments());

        this.featuresAndFragments.addAll(features);
        this.featuresAndFragments.addAll(getFragments());
        this.fragments1 = FragmentCacheUtils.cacheFragments1(this.featuresAndFragments);

    }

    private List<CucumberFeature> getFragments() {
        if (PROPERTIES.getFragmentsPath().isEmpty()) {
            return features;
        } else {
            ClassLoader classLoader = this.getClass().getClassLoader();
            ResourceLoader resourceLoader = new MultiLoader(classLoader);
            return CucumberFeature.load(resourceLoader, Collections.singletonList(PROPERTIES.getFragmentsPath()));
        }
    }

    /**
     * Replace in all the features of special steps to the fragments (scenarios)
     *
     * @throws IllegalAccessException if it was not possible to replace a step with a fragment
     * @throws FragmentException if you can not find the fragment by the specified name
     */
    public void replace() throws IllegalAccessException, FragmentException {

        for (CucumberFeature cucumberFeature : featuresAndFragments) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            this.language = feature.getLanguage();
            List<ScenarioDefinition> featureChildren = feature.getChildren();
            for (ScenarioDefinition scenarioDefinition : featureChildren) {
                if (!scenarioDefinition.getName().isEmpty()) {
                    graph.addNode(scenarioDefinition);

                    List<Step> steps = scenarioDefinition.getSteps();
                    for (Step step : steps) {
                        if (isStepFragmentRequire(step)) {

                            String scenarioName = getFragmentName(step);
                            graph.putEdge(scenarioDefinition, fragments1.get(scenarioName));


                        }
                    }
                }
            }
        }
        Set<EndpointPair<Object>> edges = Graphs.transpose(graph).edges();


        ArrayList<EndpointPair<Object>> list = new ArrayList<>(Graphs.transpose(graph).edges());
        Collections.reverse(list);
        System.out.println(list);

        for (EndpointPair pair : list) {
//            System.out.println(((ScenarioDefinition) pair.nodeU()).getName() + " - " + ((ScenarioDefinition) pair.nodeV()).getName());
            replaceInScenario((ScenarioDefinition) pair.nodeV());

        }









    }

    public void replaceInScenario(ScenarioDefinition scenarioDefinition) throws FragmentException, IllegalAccessException {
            List<Step> replacementSteps = replaceSteps(scenarioDefinition.getSteps());
            FieldUtils.writeField(scenarioDefinition, "steps", replacementSteps, true);
    }

    /**
     * Bypass the passed list of steps and if a step is found that should be replaced with a fragment, then take the scenario
     * name, find its steps and substitute them for a step that requires a replacement for the fragment
     *
     * @param steps list of steps in which the search for a step requiring replacement is performed on a fragment
     * @return the list of steps with the substituted fragments
     * @throws IllegalAccessException If can not copy the location from the replaced step to the steps of the fragment
     * @throws FragmentException if the fragment with the required name is not found
     */
    private List<Step> replaceSteps(List<Step> steps) throws IllegalAccessException, FragmentException {
        List<Step> replacementSteps = new ArrayList<>();

        for (Step step : steps) {
            if (isStepFragmentRequire(step)) {
                replacementSteps.addAll(replaceStep(step));
            } else {
                replacementSteps.add(step);
            }
        }

        return replacementSteps;
    }

    private boolean isStepFragmentRequire(Step step) {
        String regex = getFragmentStepRegex();
        return Pattern.matches(regex, step.getText());
    }

    private List<Step> replaceStep(Step stepToReplace) throws IllegalAccessException, FragmentException {
        List<Step> replacementSteps = new ArrayList<>();
        String fragmentName = getFragmentName(stepToReplace);
        List<Step> replacementStepsDraft = fragments.get(fragmentName);

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

    private String getFragmentStepRegex() {
        return I18N.getI18n(CoreGenericSteps.class, new Locale(language)).get(FRAGMENT_STEP_REGEX_KEY);
    }

    private String getFragmentName(Step step) {
        String regex = getFragmentStepRegex();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(step.getText());
        matcher.find();
        return matcher.group(1);
    }

    private void copyLocation(Step originalStep, Step targetStep) throws IllegalAccessException {
        FieldUtils.writeField(targetStep, "location", originalStep.getLocation(), true);
    }
}
