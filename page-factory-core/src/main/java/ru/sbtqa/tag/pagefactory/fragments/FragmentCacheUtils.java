package ru.sbtqa.tag.pagefactory.fragments;

import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import gherkin.ast.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;

public class FragmentCacheUtils {

    private static final String FRAGMENT_TAG = "@fragment";

    private FragmentCacheUtils() {}

    public static Map<String, List<Step>> cacheFragments(List<CucumberFeature> features) {
        Map<String, List<Step>> fragments = new HashMap<>();

        for (CucumberFeature cucumberFeature : features) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarioDefinitions = feature.getChildren();
            for (ScenarioDefinition scenarioDefinition : scenarioDefinitions) {
                List<Tag> tags = ReflectionUtils.getScenarioTags(scenarioDefinition);
                if (isFragmentTagContains(tags)) {
                    fragments.put(scenarioDefinition.getName(), scenarioDefinition.getSteps());
                }
            }
        }

        return fragments;
    }

    public static Map<String, ScenarioDefinition> cacheFragments1(List<CucumberFeature> features) {
        Map<String, ScenarioDefinition> fragments = new HashMap<>();

        for (CucumberFeature cucumberFeature : features) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarioDefinitions = feature.getChildren();
            for (ScenarioDefinition scenarioDefinition : scenarioDefinitions) {
                List<Tag> tags = ReflectionUtils.getScenarioTags(scenarioDefinition);
                if (isFragmentTagContains(tags)) {
                    fragments.put(scenarioDefinition.getName(), scenarioDefinition);
                }
            }
        }

        return fragments;
    }

    private static boolean isFragmentTagContains(List<Tag> tags) {
        return tags.stream().anyMatch(tag -> tag.getName().equals(FRAGMENT_TAG));
    }

}
