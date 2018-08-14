package ru.sbtqa.tag.pagefactory.data;

import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.DataTable;
import gherkin.ast.DocString;
import gherkin.ast.Examples;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.Node;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;
import gherkin.ast.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.TestDataObject;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;

public class DataParser {

    private static final String STEP_PARSE_REGEX = "(:?\\$([\\w|-|\\s|_\\d]+)?(\\{[^\\}|^\\$|^\\{]+\\}))+";
    private static final String TAG_PARSE_REGEX = "(?:\\$([^\\{]+)(\\{([^\\}]+)\\})?)";

    private String featureDataTag;
    private String currentScenarioTag;

    /**
     * Parse features and replace all data placeholders inside
     *
     * @param cucumberFeatures list of cucumber features
     * @throws DataException
     * @throws IllegalAccessException
     */
    public void replaceStepPlaceholders(List<CucumberFeature> cucumberFeatures) throws DataException, IllegalAccessException {

        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
            Feature feature = gherkinDocument.getFeature();

            setFeatureDataTag(parseTags(feature.getTags()));
            List<ScenarioDefinition> featureChildren = feature.getChildren();

            for (ScenarioDefinition scenarioDefinition : featureChildren) {
                List<Tag> currentScenarioTags = ReflectionUtils.getScenarioTags(scenarioDefinition);
                setCurrentScenarioTag(parseTags(currentScenarioTags));
                List<Step> steps = scenarioDefinition.getSteps();

                if (scenarioDefinition instanceof ScenarioOutline) {
                    List<Examples> examples = ((ScenarioOutline) scenarioDefinition).getExamples();
                    FieldUtils.writeField(scenarioDefinition, "examples", replaceExamplesPlaceholders(examples), true);
                }

                for (Step step : steps) {
                    FieldUtils.writeField(step, "argument", replaceArgumentPlaceholders(step.getArgument()), true);
                    FieldUtils.writeField(step, "text", replaceStepPlaceholders(step.getText()), true);
                }
            }
        }
    }

    private String parseTags(List<Tag> tags) {
        Optional<Tag> dataTag = tags.stream().filter(predicate -> predicate.getName().startsWith("@data=")).findFirst();
        return dataTag.map(tag -> tag.getName().split("=")[1].trim()).orElse(null);
    }

    private Node replaceArgumentPlaceholders(Node argument) throws DataException {
        if (argument instanceof DataTable) {
            DataTable dataTable = (DataTable) argument;
            argument = replaceDataTablePlaceholders(dataTable);
        } else if (argument instanceof DocString) {
            DocString docString = (DocString) argument;
            argument = new DocString(docString.getLocation(), docString.getContentType(), replaceStepPlaceholders(docString.getContent()));

        }
        return argument;
    }

    private String replaceStepPlaceholders(String raw) throws DataException {
        Pattern stepDataPattern = Pattern.compile(STEP_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedStep = new StringBuilder(raw);
        // Offset to handle diff between placeholder length and value length
        int offset = 0;

        while (stepDataMatcher.find()) {
            String collection = stepDataMatcher.group(2);
            String value = stepDataMatcher.group(3);

            if (value == null) {
                continue;
            }
            if (collection != null) {
                DataProvider.updateCollection(DataProvider.getInstance().fromCollection(collection.replace("$", "")));

                replacedStep = replacedStep.replace(stepDataMatcher.start(2) + offset, stepDataMatcher.end(2) + offset, "");
                offset -= collection.length();
            } else {
                String tag = currentScenarioTag != null ? currentScenarioTag : featureDataTag;

                if (tag != null) {
                    parseTestDataObject(tag);
                }
            }

            String dataPath = normalizeValue(value);
            String parsedValue = DataProvider.getInstance().get(dataPath).getValue();
            replacedStep = replacedStep.replace(stepDataMatcher.start(3) - 1 + offset, stepDataMatcher.end(3) + offset, parsedValue);
            offset += parsedValue.length() - 1 - value.length();
        }
        return replacedStep.toString();
    }

    private void parseTestDataObject(String tag) throws DataException {
        Pattern tagPattern = Pattern.compile(TAG_PARSE_REGEX);
        Matcher tagMatcher = tagPattern.matcher(tag.trim());

        if (tagMatcher.matches()) {
            String collection = tagMatcher.group(1);
            String value = tagMatcher.group(2);
            TestDataObject tdo = DataProvider.getInstance().fromCollection(collection);

            if (value != null) {
                tdo = tdo.get(normalizeValue(value));
            }

            DataProvider.updateCollection(tdo);
        }
    }

    private void setFeatureDataTag(String featureDataTag) {
        this.featureDataTag = featureDataTag;
    }

    private void setCurrentScenarioTag(String currentScenarioTag) {
        this.currentScenarioTag = currentScenarioTag;
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
                TableCell resultCell = new TableCell(cell.getLocation(), replaceStepPlaceholders(cell.getValue()));
                resultCells.add(resultCell);
            }
            resultTableRows.add(new TableRow(row.getLocation(), resultCells));
        }
        return resultTableRows;
    }

    private String normalizeValue(String value) {
        return value.replace("$", "").replace("{", "").replace("}", "");
    }
}
