package ru.sbtqa.tag.pagefactory.fragments;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import gherkin.ast.Tag;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;

public class FragmentCacheUtils {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);
    private static final String FRAGMENT_TAG = "@fragment";

    private FragmentCacheUtils() {}

    public static Map<String, ScenarioDefinition> cacheFragmentsAsMap(List<CucumberFeature> features) {
        Map<String, ScenarioDefinition> fragments = new HashMap<>();

        for (CucumberFeature cucumberFeature : features) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarioDefinitions = feature.getChildren();
            for (ScenarioDefinition scenario : scenarioDefinitions) {
                List<Tag> tags = ReflectionUtils.getScenarioTags(scenario);
                if (isFragmentTagContains(tags)) {
                    fragments.put(scenario.getName(), scenario);
                }
            }
        }

        return fragments;
    }

    private static boolean isFragmentTagContains(List<Tag> tags) {
        return tags.stream().anyMatch(tag -> tag.getName().equals(FRAGMENT_TAG));
    }


    public static List<CucumberFeature> cacheFragmentsAsList(Class clazz, List<CucumberFeature> features) {
        if (PROPERTIES.getFragmentsPath().isEmpty()) {
            return features;
        } else {
            ClassLoader classLoader = clazz.getClassLoader();
            ResourceLoader resourceLoader = new MultiLoader(classLoader);
            List<CucumberFeature> fragmentsRaw = CucumberFeature.load(resourceLoader, Collections.singletonList(PROPERTIES.getFragmentsPath()));
            return Stream.concat(features.stream(), fragmentsRaw.stream()).collect(Collectors.toList());
        }
    }

    public static MutableGraph<Object> cacheFragmentsAsGraph(Map<ScenarioDefinition, String> fragmentsLanguageMap, Map<String, ScenarioDefinition> fragmentsMap) {
        MutableGraph<Object> graph = GraphBuilder.directed().allowsSelfLoops(false).build();

        for (Map.Entry<String, ScenarioDefinition> fragment : fragmentsMap.entrySet()) {
            ScenarioDefinition scenario = fragment.getValue();
            if (!scenario.getName().isEmpty()) {
                graph.addNode(scenario);

                List<Step> steps = scenario.getSteps();
                for (Step step : steps) {
                    String language = fragmentsLanguageMap.get(scenario);
                    if (FragmentUtils.isStepFragmentRequire(step, language)) {
                        String scenarioName = FragmentUtils.getFragmentName(step, language);
                        graph.putEdge(scenario, fragmentsMap.get(scenarioName));
                    }
                }
            }
        }

        return graph;
    }

    public static Map<ScenarioDefinition, String> cacheScenarioLanguage(List<CucumberFeature> features) {
        Map<ScenarioDefinition, String> featuresLanguageMap = new HashMap<>();

        for (CucumberFeature cucumberFeature : features) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarios = feature.getChildren();
            for (ScenarioDefinition scenario : scenarios) {
                featuresLanguageMap.put(scenario, feature.getLanguage());
            }
        }

        return featuresLanguageMap;
    }
}
