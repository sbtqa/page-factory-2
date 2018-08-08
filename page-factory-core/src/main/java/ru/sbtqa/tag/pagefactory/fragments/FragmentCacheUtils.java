package ru.sbtqa.tag.pagefactory.fragments;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.*;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FragmentCacheUtils {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);
    private static final String FRAGMENT_TAG = "@fragment";

    private FragmentCacheUtils() {
    }

    public static List<CucumberFeature> cacheFragmentsToFeatures(Class clazz, List<CucumberFeature> features) {
        if (PROPERTIES.getFragmentsPath().isEmpty()) {
            return features;
        } else {
            ClassLoader classLoader = clazz.getClassLoader();
            ResourceLoader resourceLoader = new MultiLoader(classLoader);
            List<CucumberFeature> fragmentsRaw = CucumberFeature.load(resourceLoader, Collections.singletonList(PROPERTIES.getFragmentsPath()));
            return Stream.concat(features.stream(), fragmentsRaw.stream()).collect(Collectors.toList());
        }
    }

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

    public static MutableGraph<Object> cacheFragmentsAsGraph(List<CucumberFeature> features,
                                                             Map<String, ScenarioDefinition> fragmentsMap,
                                                             Map<ScenarioDefinition, String> scenarioLanguageMap) throws FragmentException {
        MutableGraph<Object> graph = GraphBuilder.directed().allowsSelfLoops(false).build();

        for (CucumberFeature cucumberFeature : features) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarioDefinitions = feature.getChildren();
            for (ScenarioDefinition scenario : scenarioDefinitions) {
                if (!scenario.getName().isEmpty()) {
                    graph.addNode(scenario);

                    List<Step> steps = scenario.getSteps();
                    for (Step step : steps) {
                        String language = scenarioLanguageMap.get(scenario);

                        if (FragmentUtils.isStepFragmentRequire(step, language)) {
                            String scenarioName = FragmentUtils.getFragmentName(step, language);
                            if (fragmentsMap.get(scenarioName) != null) {
                                graph.putEdge(scenario, fragmentsMap.get(scenarioName));
                            } else {
                                throw new FragmentException("Scenario \"" + scenario.getName() + "\" needs scenario \"" + scenarioName + "\" as fragment, " +
                                        "but scenario \"" + scenarioName + "\" doesn't exist");
                            }
                        }

                    }
                }
            }
        }

        return graph;
    }

    public static Map<ScenarioDefinition, String> cacheScenarioLanguage(List<CucumberFeature> features) {
        Map<ScenarioDefinition, String> scenarioLanguageMap = new HashMap<>();

        for (CucumberFeature cucumberFeature : features) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();
            List<ScenarioDefinition> scenarios = feature.getChildren();
            for (ScenarioDefinition scenario : scenarios) {
                scenarioLanguageMap.put(scenario, feature.getLanguage());
            }
        }

        return scenarioLanguageMap;
    }
}
