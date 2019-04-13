package ru.sbtqa.tag.pagefactory.utils;

import cucumber.runtime.FeatureBuilder;
import cucumber.runtime.io.Resource;
import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.Background;
import gherkin.ast.DataTable;
import gherkin.ast.Examples;
import gherkin.ast.Feature;
import gherkin.ast.Scenario;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import gherkin.ast.TableCell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GherkinSerializer {

    private final String NL = "\n";
    private final String TAB = "\t";
    private final String SPACE = " ";
    private StringBuilder builder;

    public GherkinSerializer() {
        builder = new StringBuilder();
    }

    /**
     * As soon as fragments are inserted, all features structure will unaligned by its file locationss.
     * So we need to realign it by reserializing featues sources.
     *
     * @param cucumberFeatures unaligned features
     * @return reserealized features
     */
    public List<CucumberFeature> normalizeSources(List<CucumberFeature> cucumberFeatures) {
        List<CucumberFeature> features = new ArrayList<>();

        FeatureBuilder featureBuilder = new FeatureBuilder(features);
        cucumberFeatures.forEach(cucumberFeature -> {
            StringBuilder source = new StringBuilder();
            Feature feature = cucumberFeature.getGherkinFeature().getFeature();
            source.append("#language: " + feature.getLanguage());
            nl(1);
            feature.getTags().forEach(tag -> source.append(tag.getName()).append(SPACE));
            nl(1);
            source.append(feature.getKeyword()).append(":").append(SPACE).append(feature.getName());
            nl(1);
            if (feature.getDescription() != null) {
                tab(1);
                source.append(feature.getDescription());
                nl(2);
            }
            feature.getChildren().forEach(scenarioDefinition -> buildScenario(source, scenarioDefinition));
            Resource gherkinResource = new GherkinResource(source.toString(), cucumberFeature.getUri());
            featureBuilder.parse(gherkinResource);
        });
        return features;
    }

    private void buildScenario(StringBuilder sb, ScenarioDefinition scenarioDefinition) {
        if (scenarioDefinition instanceof Scenario) {
            Scenario scenario = (Scenario) scenarioDefinition;
            if (!scenario.getTags().isEmpty()) {
                tab(1);
                scenario.getTags().forEach(tag -> builder.append(tag.getName()).append(SPACE));
                nl(1);
            }
            tab(1);
            builder.append(scenario.getKeyword()).append(":").append(SPACE).append(scenario.getName());
            nl(1);
            if (scenario.getDescription() != null) {
                tab(2);
                builder.append(scenario.getDescription());
            }
            scenario.getSteps().forEach(step -> buildStep(step));
        } else if (scenarioDefinition instanceof ScenarioOutline) {
            ScenarioOutline scenario = (ScenarioOutline) scenarioDefinition;
            if (!scenario.getTags().isEmpty()) {
                tab(1);
                scenario.getTags().forEach(tag -> builder.append(tag.getName()).append(SPACE));
                nl(1);
            }
            tab(1);
            builder.append(scenario.getKeyword()).append(":").append(SPACE).append(scenario.getName());
            nl(1);
            if (scenario.getDescription() != null) {
                nl(1);
                tab(2);
                builder.append(scenario.getDescription());
                nl(2);
            }
            scenario.getSteps().forEach(step -> buildStep(step));
            scenario.getExamples().forEach(examples -> buildExamples(examples));
        } else if (scenarioDefinition instanceof Background) {
            Background scenario = (Background) scenarioDefinition;
            tab(1);
            builder.append(scenario.getKeyword()).append(":");
            builder.append("\n");
            scenario.getSteps().forEach(step -> buildStep(step));
        }
    }

    private void buildExamples(Examples examples) {
        nl(1);
        tab(2);
        builder.append(examples.getKeyword()).append(":");
        nl(1);
        List<String> header = examples.getTableHeader().getCells().stream()
                .map(tableCell -> tableCell.getValue()).collect(Collectors.toList());
        tab(2);
        space(2);
        builder.append("|").append(String.join("|", header) + "|");
        nl(1);
        examples.getTableBody().forEach(row -> {
            List<String> cells = row.getCells().stream().map(TableCell::getValue).collect(Collectors.toList());
            tab(2);
            space(2);
            builder.append("|").append(String.join("|", cells) + "|");
            nl(1);
        });

    }

    private void buildStep(Step step) {
        tab(2);
        builder.append(step.getKeyword()).append(SPACE).append(step.getText());
        if (step.getArgument() != null) {
            nl(1);
            if (step.getArgument() instanceof DataTable) {
                DataTable table = (DataTable) step.getArgument();
                table.getRows().forEach(tableRow -> {
                    List<String> collect = tableRow.getCells().stream()
                            .map(TableCell::getValue).collect(Collectors.toList());
                    tab(2);
                    space(2);
                    builder.append("|").append(String.join("|", collect));
                    nl(1);
                });
            } else {
                // TODO: support pystring
            }

        } else {
            builder.append("\n");
        }
    }


    private void space(int count) {
        appendTimes(SPACE, count);
    }

    private void nl(int count) {
        appendTimes(NL, count);
    }

    private void tab(int count) {
        appendTimes(TAB, count);
    }

    private void appendTimes(String source, int times) {
        for (int i = 0; i < times; i++) {
            builder.append(source);
        }
    }
}
