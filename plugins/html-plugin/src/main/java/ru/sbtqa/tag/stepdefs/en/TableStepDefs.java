package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.en.When;
import java.util.List;
import ru.sbtqa.tag.pagefactory.html.junit.TableSteps;
import ru.sbtqa.tag.pagefactory.transformer.ClickVariationTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.ClickVariation;

public class TableStepDefs {

    private final TableSteps tableSteps = TableSteps.getInstance();

    @When("^user selects (double-click )?the first row of the table \"([^\"]*)\"$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName) {
        tableSteps.selectRow(doubleClick, tableName);
    }

    @When("^user selects (double-click )?the first row of the table \"([^\"]*)\" in group \"([^\"]*)\"$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName, String groupName) {
        tableSteps.selectRowInGroup(doubleClick, tableName, groupName);
    }

    @When("^user selects (double-click )?row number \"([^\"]*)\" in the table \"([^\"]*)\"$")
    public void selectRowByNumber(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String rowIndex, String tableName) {
        tableSteps.selectRowByNumber(doubleClick, rowIndex, tableName);
    }

    @When("^user selects (double-click )?row number \"([^\"]*)\" in the table \"([^\"]*)\" in group \"([^\"]*)\"$")
    public void selectRowByNumberInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                   String rowIndex, String tableName, String groupName) {
        tableSteps.selectRowByNumberInGroup(doubleClick, rowIndex, tableName, groupName);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\"$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                    String tableName, String cellText, String columnName) {
        tableSteps.selectRow(doubleClick, tableName, cellText, columnName);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" in group \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\"$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                           String tableName, String gropName, String cellText, String columnName) {
        tableSteps.selectRowInGroup(doubleClick, tableName, gropName, cellText, columnName);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\" (\\d+) in order$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                    String tableName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRow(doubleClick, tableName, cellText, columnName, serialNumber);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" in group \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                           String tableName, String groupName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRowInGroup(doubleClick, tableName, groupName, cellText, columnName, serialNumber);
    }

    @When("^user selects (double-click )?row in the table \"([^\"]*)\" in the column \"([^\"]*)\" by element \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void selectRowByElement(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                             String tableName, String columnName, String elementName, String elementValue) {
        tableSteps.selectRowByElement(doubleClick, tableName, columnName, elementName, elementValue);
    }

    @When("^user selects (double-click )?row in the table \"([^\"]*)\" in the column \"([^\"]*)\" by element \"([^\"]*)\" with value \"([^\"]*)\" (\\d+) in order$")
    public void selectRowByElement(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                             String tableName, String columnName, String elementName, String elementValue, int serialNumber) {
        tableSteps.selectRowByElement(doubleClick, tableName, columnName, elementName, elementValue, serialNumber);
    }

    @When("^user remembers the line number in the table \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\" in variable \"([^\"]*)\"$")
    public void saveRowNumber(String tableName, String cellText, String columnName, String stashKey) {
        tableSteps.saveRowNumber(tableName, cellText, columnName, stashKey);
    }

    @When("^user remembers the line number in the table \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\" in variable \"([^\"]*)\"$")
    public void saveRowNumberViaColumnIndex(String tableName, String cellText, String columnNumber, String stashKey) {
        tableSteps.saveRowNumberViaColumnIndex(tableName, cellText, columnNumber, stashKey);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void clickButtonViaColumnIndex(String buttonName, String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonViaColumnIndex(buttonName, tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" and row with number \"([^\"]*)\"$")
    public void clickButtonViaIndexes(String buttonName, String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonViaIndexes(buttonName, tableName, columnNumber, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" and row with number \"([^\"]*)\"$")
    public void clickButtonViaColumnIndex(String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonViaColumnIndex(tableName, columnNumber, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void clickButtonViaColumnIndex(String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonViaColumnIndex(tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void clickButton(String buttonName, String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(buttonName, tableName, columnWithButtonName, cellText, columnName);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column \"([^\"]*)\" and row with number \"([^\"]*)\"$")
    public void clickButtonViaRowIndex(String buttonName, String tableName, String columnName, String rowNumber) {
        tableSteps.clickButtonViaRowIndex(buttonName, tableName, columnName, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column \"([^\"]*)\"and row with number \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnName, String rowNumber) {
        tableSteps.clickButton(tableName, columnName, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(tableName, columnWithButtonName, cellText, columnName);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" row with the value \"([^\"]*)\" in the column with number \"([^\"]*)\"$")
    public void selectRowInColumn(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                            String tableName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumn(doubleClick, tableName, cellText, columnNumber);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" in group \"([^\"]*)\" row with the value \"([^\"]*)\" in the column with number \"([^\"]*)\"$")
    public void selectRowInColumnInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                   String tableName, String groupName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumnInGroup(doubleClick, tableName, groupName, cellText, columnNumber);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" row with value \"([^\"]*)\" in the column with number \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInColumnByNumber(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                    String tableName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumber(doubleClick, tableName, cellText, columnNumber, serialNumber);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" in group \"([^\"]*)\" row " +
            "with value \"([^\"]*)\" in the column with number \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInColumnByNumberInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                           String tableName, String groupName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumberInGroup(doubleClick, tableName, groupName, cellText, columnNumber, serialNumber);
    }

    @When("^user selects (double-click )?row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\"$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String cellText, String tableName) {
        tableSteps.selectRow(doubleClick, cellText, tableName);
    }

    @When("^user selects (double-click )?row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\" in group \"([^\"]*)\"$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String cellText, String tableName, String groupName) {
        tableSteps.selectRowInGroup(doubleClick, cellText, tableName, groupName);
    }

    @When("^user selects (double-click )?row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\" (\\d+) in order$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                    String cellText, String tableName, int serialNumber) {
        tableSteps.selectRow(doubleClick, cellText, tableName, serialNumber);
    }

    @When("^user selects (double-click )?row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\" in group \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                           String cellText, String tableName, String groupName, int serialNumber) {
        tableSteps.selectRowInGroup(doubleClick, cellText, tableName, groupName, serialNumber);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" row with values$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(doubleClick, tableName, row);
    }

    @When("^user selects (double-click )?in the table \"([^\"]*)\" in group \"([^\"]*)\" row with values$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName, String groupName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(doubleClick, tableName, groupName, row);
    }

    @When("^user checks table in the table \"([^\"]*)\" any row of the column \"([^\"]*)\" contains text \"([^\"]*)\"$")
    public void tableHasText(String tableName, String columnName, String cellText) {
        tableSteps.tableHasText(tableName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" any row of the column \"([^\"]*)\" contains text \"([^\"]*)\"$")
    public void tableHasTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.tableHasTextInGroup(tableName, groupName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in any row of the column \"([^\"]*)\" there is a text strictly equal \"([^\"]*)\"$")
    public void tableContainsText(String tableName, String columnName, String cellText) {
        tableSteps.tableContainsText(tableName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" in any row of the column \"([^\"]*)\" there is a text strictly equal \"([^\"]*)\"$")
    public void tableContainsTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.tableContainsTextInGroup(tableName, groupName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" any cell contains text \"([^\"]*)\"$")
    public void tableCellHasText(String tableName, String cellText) {
        tableSteps.tableCellHasText(tableName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" any cell contains text \"([^\"]*)\"$")
    public void tableCellHasTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.tableCellHasTextInGroup(tableName, groupName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in any cell there is a text strictly equal \"([^\"]*)\"$")
    public void tableCellContainsText(String tableName, String cellText) {
        tableSteps.tableCellContainsText(tableName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" in any cell there is a text strictly equal \"([^\"]*)\"$")
    public void tableCellContainsTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.tableCellContainsTextInGroup(tableName, groupName, cellText);
    }

    @When("^user checks table row cells \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void checkTableRow(String tableName, String cellText, String columnName, DataTable data) {
        tableSteps.checkTableRow(tableName, cellText, columnName, data);
    }

    @When("^user checks table row cells \"([^\"]*)\" in group \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void checkTableRowInGroup(String tableName, String groupName, String cellText, String columnName, DataTable data) {
        tableSteps.checkTableRowInGroup(tableName, groupName, cellText, columnName, data);
    }

    @When("^user checks table row cells \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void checkTableRowViaColumnIndexes(String tableName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkTableRowViaColumnIndexes(tableName, cellText, columnNumber, data);
    }

    @When("^user checks table row cells \"([^\"]*)\" in group \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void checkTableRowViaColumnIndexesInGroup(String tableName, String groupName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkTableRowViaColumnIndexesInGroup(tableName, groupName, cellText, columnNumber, data);
    }
}
