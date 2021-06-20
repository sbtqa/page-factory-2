package ru.sbtqa.tag.pagefactory.utils;

import cucumber.runtime.io.Resource;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.FeatureBuilder;
import gherkin.ast.DataTable;
import gherkin.ast.DocString;
import gherkin.ast.Examples;
import gherkin.ast.Feature;
import gherkin.ast.Scenario;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;
import gherkin.ast.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GherkinSerializer {

    private static final String NL = "\n";
    private static final String SPACE = " ";
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
    public List<CucumberFeature> reserializeFeatures(List<CucumberFeature> cucumberFeatures) {
        FeatureBuilder featureBuilder = new FeatureBuilder();
        cucumberFeatures.forEach(cucumberFeature -> {
            builder = new StringBuilder();
            Feature feature = cucumberFeature.getGherkinFeature().getFeature();
            builder.append("#language: ").append(feature.getLanguage());
            nl(1);
            if (!feature.getTags().isEmpty()) {
                feature.getTags().forEach(tag -> builder.append(tag.getName()).append(SPACE));
                nl(1);
            }
            builder.append(feature.getKeyword()).append(":").append(SPACE).append(feature.getName());
            nl(1);
            if (feature.getDescription() != null) {
                tab(1);
                builder.append(feature.getDescription());
                nl(1);
            }
            nl(1);
            feature.getChildren().forEach(scenarioDefinition -> buildScenario(builder, scenarioDefinition));
            Resource gherkinResource = new GherkinResource(builder.toString(), cucumberFeature.getUri());
            featureBuilder.parse(gherkinResource);
        });
        return featureBuilder.build();
    }

    private void buildScenario(StringBuilder sb, ScenarioDefinition scenarioDefinition) {
        buildScenarioTags(scenarioDefinition);

        tab(1);
        builder.append(scenarioDefinition.getKeyword()).append(":").append(SPACE).append(scenarioDefinition.getName());
        nl(1);

        if (scenarioDefinition.getDescription() != null) {
            nl(1);
            tab(2);
            builder.append(scenarioDefinition.getDescription());
            nl(2);
        }

        scenarioDefinition.getSteps().forEach(this::buildStep);

        if (scenarioDefinition instanceof ScenarioOutline) {
            ((ScenarioOutline) scenarioDefinition)
                    .getExamples().forEach(this::buildExamples);
        }
        nl(1);
    }

    private void buildScenarioTags(ScenarioDefinition scenarioDefinition) {
        List<Tag> tags = new ArrayList<>();
        if (scenarioDefinition instanceof Scenario) {
            tags = ((Scenario) scenarioDefinition).getTags();
        } else if (scenarioDefinition instanceof ScenarioOutline) {
            tags = ((ScenarioOutline) scenarioDefinition).getTags();
        }
        if (!tags.isEmpty()) {
            tab(1);
            tags.forEach(tag -> builder.append(tag.getName()).append(SPACE));
            nl(1);
        }
    }

    private void buildExamples(Examples examples) {
        nl(1);
        tab(2);
        builder.append(examples.getKeyword()).append(":");
        nl(1);
        List<String> header = examples.getTableHeader().getCells().stream()
                .map(TableCell::getValue).collect(Collectors.toList());
        tab(2);
        space(2);
        builder.append("|").append(String.join("|", header)).append("|");
        nl(1);
        examples.getTableBody().forEach(this::buildTableRow);
    }

    private void buildStep(Step step) {
        tab(2);
        builder.append(step.getKeyword()).append(SPACE).append(step.getText());
        if (step.getArgument() != null) {
            nl(1);
            if (step.getArgument() instanceof DataTable) {
                DataTable table = (DataTable) step.getArgument();
                table.getRows().forEach(this::buildTableRow);
            } else if (step.getArgument() instanceof DocString) {
                tab(2);
                builder.append("\"\"\"");
                nl(1);
                DocString docString = (DocString) step.getArgument();
                tab(2);
                builder.append(docString.getContent());
                nl(1);
                tab(2);
                builder.append("\"\"\"");
                nl(1);
            }

        } else {
            builder.append("\n");
        }
    }

    private void buildTableRow(TableRow tableRow) {
        List<String> collect = tableRow.getCells().stream()
                .map(tableCell -> tableCell.getValue()
                        .replaceAll("\\|", "\\\\|"))
                .collect(Collectors.toList());
        tab(2);
        space(2);
        builder.append("|").append(String.join("|", collect)).append("|");
        nl(1);
    }


    private void space(int count) {
        appendTimes(SPACE, count);
    }

    private void nl(int count) {
        appendTimes(NL, count);
    }

    private void tab(int count) {
        appendTimes(SPACE, count * 2);
    }

    private void appendTimes(String source, int times) {
        for (int i = 0; i < times; i++) {
            builder.append(source);
        }
    }
}
