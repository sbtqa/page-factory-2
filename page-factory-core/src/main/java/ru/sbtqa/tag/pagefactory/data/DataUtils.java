package ru.sbtqa.tag.pagefactory.data;

import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.DataTable;
import gherkin.ast.DocString;
import gherkin.ast.Examples;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.Node;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;
import gherkin.ast.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import static ru.sbtqa.tag.datajack.providers.AbstractDataProvider.PATH_PARSE_REGEX;

public class DataUtils {

    private static final Configuration PROPERTIES = Configuration.create();
    private static final String DATA_TAG = "@data=";
    private static final String COLLECTION_SIGNATURE = "$";

    public static boolean isDataParameter(String dataParameter) {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(dataParameter);
        return stepDataMatcher.find();
    }

    public static String replaceDataPlaceholders(String raw, String currentScenarioDataTagValue) throws DataException {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedStep = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String collection = stepDataMatcher.group(1);
            String value = stepDataMatcher.group(2);

            if (collection == null) {
                parseDataTagValue(currentScenarioDataTagValue);
            }

            String builtPath = COLLECTION_SIGNATURE + (collection == null ? "" : collection) + value;
            String parsedValue = DataFactory.getDataProvider().getByPath(builtPath).getValue();
            replacedStep = replacedStep.replace(stepDataMatcher.start(), stepDataMatcher.end(), parsedValue);
            stepDataMatcher = stepDataPattern.matcher(replacedStep);
        }
        return replacedStep.toString();
    }

    public static Node replaceArgumentPlaceholders(Node argument, String currentScenarioDataTagValue) throws DataException {
        if (argument instanceof DataTable) {
            DataTable dataTable = (DataTable) argument;
            argument = replaceDataTablePlaceholders(dataTable, currentScenarioDataTagValue);
        } else if (argument instanceof DocString) {
            DocString docString = (DocString) argument;
            argument = new DocString(docString.getLocation(), docString.getContentType(), DataUtils.replaceDataPlaceholders(docString.getContent(), currentScenarioDataTagValue));
        }
        return argument;
    }

    public static List<Examples> replaceExamplesPlaceholders(List<Examples> examples, String currentScenarioDataTagValue) throws DataException {
        List<Examples> resultExamples = new ArrayList<>();
        for (Examples example : examples) {
            Examples resultExample = new Examples(
                    example.getLocation(),
                    example.getTags(),
                    example.getKeyword(),
                    example.getName(),
                    example.getDescription(),
                    example.getTableHeader(),
                    replaceTableRows(example.getTableBody(), currentScenarioDataTagValue));
            resultExamples.add(resultExample);
        }
        return resultExamples;
    }

    private static List<TableRow> replaceTableRows(List<TableRow> tableRows, String currentScenarioDataTagValue) throws DataException {
        List<TableRow> resultTableRows = new ArrayList<>();

        for (TableRow row : tableRows) {
            List<TableCell> resultCells = new ArrayList<>();

            for (TableCell cell : row.getCells()) {
                TableCell resultCell = new TableCell(cell.getLocation(), DataUtils.replaceDataPlaceholders(cell.getValue(), currentScenarioDataTagValue));
                resultCells.add(resultCell);
            }
            resultTableRows.add(new TableRow(row.getLocation(), resultCells));
        }
        return resultTableRows;
    }

    public static String formFeatureData(CucumberFeature cucumberFeature) {
        String featureDataTagValue = COLLECTION_SIGNATURE + PROPERTIES.getDataInitialCollection();
        String tag = parseDataTags(getFeature(cucumberFeature).getTags());
        return tag != null ? tag : featureDataTagValue;
    }

    public static String formScenarioDataTag(ScenarioDefinition scenarioDefinition, String featureDataTag) {
        List<Tag> currentScenarioTags = DataUtils.getScenarioTags(scenarioDefinition);
        String scenarioDataTagValue = DataUtils.parseDataTags(currentScenarioTags);
        return scenarioDataTagValue != null ? scenarioDataTagValue : featureDataTag;
    }

    public static String parseDataTags(List<Tag> tags) {
        Optional<Tag> dataTag = tags.stream()
                .filter(predicate -> predicate.getName().startsWith(DATA_TAG))
                .findFirst();

        return dataTag.isPresent() ? dataTag.get().getName().split("=")[1].trim() : null;
    }

    public static List<ScenarioDefinition> getScenarioDefinitions(CucumberFeature cucumberFeature){
        return getFeature(cucumberFeature).getChildren();
    }

    public static List<Tag> getScenarioTags(ScenarioDefinition scenarioDefinition) {
        try {
            return (List<Tag>) FieldUtils.readField(scenarioDefinition, "tags", true);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return Collections.EMPTY_LIST;
        }
    }


    private static Node replaceDataTablePlaceholders(DataTable dataTable, String currentScenarioDataTagValue) throws DataException {
        return new DataTable(replaceTableRows(dataTable.getRows(), currentScenarioDataTagValue));
    }

    private static void parseDataTagValue(String tag) throws DataException {
        DataFactory.updateCollection(DataFactory.getDataProvider().getByPath(tag));
    }

    private static Feature getFeature(CucumberFeature cucumberFeature) {
        GherkinDocument gherkinDocument = cucumberFeature.getGherkinFeature();
        return gherkinDocument.getFeature();
    }
}
