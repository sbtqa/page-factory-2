package ru.sbtqa.tag.pagefactory.fragments;

import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import gherkin.ast.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;
import ru.sbtqa.tag.qautils.i18n.I18N;
import ru.sbtqa.tag.stepdefs.CoreGenericSteps;

public class FragmentReplacer {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);
    private static final String FRAGMENT_TAG = "@fragment";
    private static final String FRAGMENT_STEP_REGEX_KEY = "ru.sbtqa.tag.pagefactory.insertFragment";

    private String language;
    private List<CucumberFeature> cucumberFeatures;

    public FragmentReplacer(List<CucumberFeature> cucumberFeatures) {
        this.cucumberFeatures = cucumberFeatures;
    }

    /**
     * Replace in all the features of special steps to the fragments (scenarios)
     *
     * @throws IllegalAccessException if it was not possible to replace a step with a fragment
     * @throws FragmentException if you can not find the fragment by the specified name
     */
    public void replaceAll() throws IllegalAccessException, FragmentException {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            this.language = feature.getLanguage();
            List<ScenarioDefinition> featureChildren = feature.getChildren();
            for (ScenarioDefinition scenarioDefinition : featureChildren) {
                List<Step> steps = scenarioDefinition.getSteps();

                FieldUtils.writeField(scenarioDefinition, "steps", replaceOnFragments(steps), true);
            }
        }
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
    private List<Step> replaceOnFragments(List<Step> steps) throws IllegalAccessException, FragmentException {
        List<Step> fragmentedSteps = new ArrayList<>();

        for (Step step : steps) {
            if (isFragmentRequire(step)) {
                String requiredFragmentName = getRequiredFragmentName(step);
                List<CucumberFeature> cucumberFeaturesWithFragments = getCucumberFeaturesWithFragments();
                for (Step fragmentStep : getFragmentSteps(requiredFragmentName, cucumberFeaturesWithFragments)) {
                    copyLocation(step, fragmentStep);
                    FragmentDataTableUtils.applyDataTable(step, fragmentStep);

                    fragmentedSteps.add(fragmentStep);
                }
            } else {
                fragmentedSteps.add(step);
            }
        }

        return fragmentedSteps;
    }

    private boolean isFragmentRequire(Step step) {
        String regex = getFragmentStepRegex();
        return Pattern.matches(regex, step.getText());
    }

    private String getFragmentStepRegex() {
        return I18N.getI18n(CoreGenericSteps.class, new Locale(language)).get(FRAGMENT_STEP_REGEX_KEY);
    }

    private String getRequiredFragmentName(Step step) {
        String regex = getFragmentStepRegex();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(step.getText());
        matcher.find();
        return matcher.group(1);
    }

    private List<CucumberFeature> getCucumberFeaturesWithFragments() {
        if (PROPERTIES.getFragmentsPath().isEmpty()) {
            return cucumberFeatures;
        } else {
            ClassLoader classLoader = this.getClass().getClassLoader();
            ResourceLoader resourceLoader = new MultiLoader(classLoader);
            return CucumberFeature.load(resourceLoader, Collections.singletonList(PROPERTIES.getFragmentsPath()));
        }
    }

    /**
     * Find a list of scenario steps that will be substituted as a fragment
     *
     * @param fragmentName sceanrio name
     * @return the list of steps of the found scenario
     */
    private List<Step> getFragmentSteps(String fragmentName, List<CucumberFeature> cucumberFeatures) throws FragmentException {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarioDefinitions = feature.getChildren();
            for (ScenarioDefinition scenarioDefinition : scenarioDefinitions) {
                String tag = parseTags(ReflectionUtils.getScenarioTags(scenarioDefinition));
                if (fragmentName.equals(tag)) {
                    return scenarioDefinition.getSteps();
                }
            }
        }

        throw new FragmentException("Cant find scenario fragment with name " + fragmentName);
    }

    private void copyLocation(Step originalStep, Step targetStep) throws IllegalAccessException {
        FieldUtils.writeField(targetStep, "location", originalStep.getLocation(), true);
    }

    public String parseTags(List<Tag> tags) {
        Optional<Tag> dataTag = tags.stream().filter(predicate -> predicate.getName().startsWith(FRAGMENT_TAG)).findFirst();
        return (dataTag.isPresent() && dataTag.get().getName().contains("=")) ? dataTag.get().getName().split("=")[1].trim() : null;
    }
}
