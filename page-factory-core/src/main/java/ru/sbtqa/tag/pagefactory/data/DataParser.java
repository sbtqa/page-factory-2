package ru.sbtqa.tag.pagefactory.data;

import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.*;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.exceptions.DataException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.sbtqa.tag.datajack.providers.AbstractDataProvider.PATH_PARSE_REGEX;

public class DataParser {

    private String featureDataTagValue;
    private String currentScenarioDataTagValue;

    public void replaceDataPlaceholders(List<CucumberFeature> cucumberFeatures) throws DataException, IllegalAccessException {

        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();

            setFeatureDataTag(parseTags(feature.getTags()));
            List<ScenarioDefinition> featureChildren = feature.getChildren();

            for (ScenarioDefinition scenarioDefinition : featureChildren) {
                List<Tag> currentScenarioTags = getScenarioTags(scenarioDefinition);
                setCurrentScenarioTag(parseTags(currentScenarioTags));
                List<Step> steps = scenarioDefinition.getSteps();

                if (scenarioDefinition instanceof ScenarioOutline) {
                    List<Examples> examples = ((ScenarioOutline) scenarioDefinition).getExamples();
                    FieldUtils.writeField(scenarioDefinition, "examples", replaceExamplesPlaceholders(examples), true);
                }

                for (Step step : steps) {
                    FieldUtils.writeField(step, "argument", replaceArgumentPlaceholders(step.getArgument()), true);
                    FieldUtils.writeField(step, "text", replaceDataPlaceholders(step.getText()), true);
                }
            }
        }
    }

    private List<Tag> getScenarioTags(ScenarioDefinition scenarioDefinition) {
        try {
            return (List<Tag>) FieldUtils.readField(scenarioDefinition, "tags", true);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return Collections.EMPTY_LIST;
        }
    }

    private String parseTags(List<Tag> tags) {
        Optional<Tag> dataTag = tags.stream().filter(predicate -> predicate.getName().startsWith("@data=")).findFirst();
        return dataTag.isPresent() ? dataTag.get().getName().split("=")[1].trim() : null;
    }

    private Node replaceArgumentPlaceholders(Node argument) throws DataException {
        if (argument instanceof DataTable) {
            DataTable dataTable = (DataTable) argument;
            argument = replaceDataTablePlaceholders(dataTable);
        } else if (argument instanceof DocString) {
            DocString docString = (DocString) argument;
            argument = new DocString(docString.getLocation(), docString.getContentType(), replaceDataPlaceholders(docString.getContent()));

        }
        return argument;
    }

    private String replaceDataPlaceholders(String raw) throws DataException {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedStep = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String collection = stepDataMatcher.group(1);
            String value = stepDataMatcher.group(2);

            if (collection == null) {
                parseDataTagValue(currentScenarioDataTagValue != null ? currentScenarioDataTagValue : featureDataTagValue);
            }

            String builtPath = "$" + (collection == null ? "" : collection) + value;
            String parsedValue = DataFactory.getDataProvider().getByPath(builtPath).getValue();
            replacedStep = replacedStep.replace(stepDataMatcher.start(), stepDataMatcher.end(), parsedValue);
            stepDataMatcher = stepDataPattern.matcher(replacedStep);
        }
        return replacedStep.toString();
    }

    private void parseDataTagValue(String tag) throws DataException {
        DataFactory.updateCollection(DataFactory.getDataProvider().getByPath(tag));
    }

    private void setFeatureDataTag(String featureDataTag) {
        this.featureDataTagValue = featureDataTag;
    }

    private void setCurrentScenarioTag(String currentScenarioDataTag) {
        this.currentScenarioDataTagValue = currentScenarioDataTag;
    }

    private Node replaceDataTablePlaceholders(DataTable dataTable) throws DataException {
        return new DataTable(replaceTableRows(dataTable.getRows()));
    }

    private List<Examples> replaceExamplesPlaceholders(List<Examples> examples) throws DataException {
        List<Examples> resultExamples = new ArrayList<>();
        for (Examples example : examples) {
            Examples resultExample = new Examples(
                    example.getLocation(),
                    example.getTags(),
                    example.getKeyword(),
                    example.getName(),
                    example.getDescription(),
                    example.getTableHeader(),
                    replaceTableRows(example.getTableBody()));
            resultExamples.add(resultExample);
        }
        return resultExamples;
    }

    private List<TableRow> replaceTableRows(List<TableRow> tableRows) throws DataException {
        List<TableRow> resultTableRows = new ArrayList<>();

        for (TableRow row : tableRows) {
            List<TableCell> resultCells = new ArrayList<>();

            for (TableCell cell : row.getCells()) {
                TableCell resultCell = new TableCell(cell.getLocation(), replaceDataPlaceholders(cell.getValue()));
                resultCells.add(resultCell);
            }
            resultTableRows.add(new TableRow(row.getLocation(), resultCells));
        }
        return resultTableRows;
    }
}