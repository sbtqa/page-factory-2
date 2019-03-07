package ru.sbtqa.tag.pagefactory.data;

import static ru.sbtqa.tag.pagefactory.data.DataUtils.replaceDataPlaceholders;

import cucumber.runtime.model.CucumberFeature;
import gherkin.ast.DataTable;
import gherkin.ast.DocString;
import gherkin.ast.Examples;
import gherkin.ast.Node;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.exceptions.DataException;

public class DataParser {

    public void replace(List<CucumberFeature> cucumberFeatures) throws DataException, IllegalAccessException {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            String featureData = DataUtils.formFeatureData(cucumberFeature);

            List<ScenarioDefinition> featureChildren = DataUtils.getScenarioDefinitions(cucumberFeature);

            for (ScenarioDefinition scenarioDefinition : featureChildren) {
                String scenarioData = DataUtils.formScenarioDataTag(scenarioDefinition, featureData);
                List<Step> steps = scenarioDefinition.getSteps();

                if (scenarioDefinition instanceof ScenarioOutline) {
                    List<Examples> examples = ((ScenarioOutline) scenarioDefinition).getExamples();
                    FieldUtils.writeField(scenarioDefinition, "examples", replaceExamplesPlaceholders(examples, scenarioData), true);
                }

                for (Step step : steps) {
                    FieldUtils.writeField(step, "argument", replaceArgumentPlaceholders(step.getArgument(), scenarioData), true);
                    FieldUtils.writeField(step, "text", replaceDataPlaceholders(step.getText(), scenarioData), true);
                }
            }
        }
    }

    private static Node replaceArgumentPlaceholders(Node argument, String currentScenarioData) throws DataException {
        if (argument instanceof DataTable) {
            DataTable dataTable = (DataTable) argument;
            argument = replaceDataTablePlaceholders(dataTable, currentScenarioData);
        } else if (argument instanceof DocString) {
            DocString docString = (DocString) argument;
            argument = new DocString(docString.getLocation(), docString.getContentType(),
                    replaceDataPlaceholders(docString.getContent(), currentScenarioData));
        }
        return argument;
    }

    private static List<Examples> replaceExamplesPlaceholders(List<Examples> examples, String currentScenarioData) throws DataException {
        List<Examples> resultExamples = new ArrayList<>();
        for (Examples example : examples) {
            Examples resultExample = new Examples(
                    example.getLocation(),
                    example.getTags(),
                    example.getKeyword(),
                    example.getName(),
                    example.getDescription(),
                    example.getTableHeader(),
                    replaceTableRows(example.getTableBody(), currentScenarioData));
            resultExamples.add(resultExample);
        }
        return resultExamples;
    }

    private static Node replaceDataTablePlaceholders(DataTable dataTable, String currentScenarioData) throws DataException {
        return new DataTable(replaceTableRows(dataTable.getRows(), currentScenarioData));
    }

    private static List<TableRow> replaceTableRows(List<TableRow> tableRows, String currentScenarioData) throws DataException {
        List<TableRow> resultTableRows = new ArrayList<>();

        for (TableRow row : tableRows) {
            List<TableCell> resultCells = new ArrayList<>();

            for (TableCell cell : row.getCells()) {
                TableCell resultCell = new TableCell(cell.getLocation(), replaceDataPlaceholders(cell.getValue(), currentScenarioData));
                resultCells.add(resultCell);
            }
            resultTableRows.add(new TableRow(row.getLocation(), resultCells));
        }
        return resultTableRows;
    }
}