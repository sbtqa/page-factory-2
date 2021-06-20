package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import java.util.List;
import ru.sbtqa.tag.pagefactory.html.junit.TableSteps;
import ru.sbtqa.tag.pagefactory.transformer.ClickVariation;

public class TableStepDefs {

    private final TableSteps tableSteps = TableSteps.getInstance();

    @When("^user selects (single-click|double-click) the first row of the table \"([^\"]*)\"$")
    public void selectRow(ClickVariation clickVariation, String tableName) {
        tableSteps.selectRow(clickVariation, tableName);
    }

    @When("^user selects (single-click|double-click) the first row of the table \"([^\"]*)\" in group \"([^\"]*)\"$")
    public void selectRowInGroup(ClickVariation clickVariation, String tableName, String groupName) {
        tableSteps.selectRowInGroup(clickVariation, tableName, groupName);
    }

    @When("^user selects (single-click|double-click) row number \"([^\"]*)\" in the table \"([^\"]*)\"$")
    public void selectRowByNumber(ClickVariation clickVariation, String rowIndex, String tableName) {
        tableSteps.selectRowByNumber(clickVariation, rowIndex, tableName);
    }

    @When("^user selects (single-click|double-click) row number \"([^\"]*)\" in the table \"([^\"]*)\" in group \"([^\"]*)\"$")
    public void selectRowByNumberInGroup(ClickVariation clickVariation,
                                                   String rowIndex, String tableName, String groupName) {
        tableSteps.selectRowByNumberInGroup(clickVariation, rowIndex, tableName, groupName);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\"$")
    public void selectRow(ClickVariation clickVariation,
                                    String tableName, String cellText, String columnName) {
        tableSteps.selectRow(clickVariation, tableName, cellText, columnName);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" in group \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\"$")
    public void selectRowInGroup(ClickVariation clickVariation,
                                           String tableName, String groupName, String cellText, String columnName) {
        tableSteps.selectRowInGroup(clickVariation, tableName, groupName, cellText, columnName);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\" (\\d+) in order$")
    public void selectRow(ClickVariation clickVariation,
                                    String tableName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRow(clickVariation, tableName, cellText, columnName, serialNumber);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" in group \"([^\"]*)\" row with the value \"([^\"]*)\" in the column \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInGroup(ClickVariation clickVariation,
                                           String tableName, String groupName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRowInGroup(clickVariation, tableName, groupName, cellText, columnName, serialNumber);
    }

    @When("^user selects (single-click|double-click) row in the table \"([^\"]*)\" in the column \"([^\"]*)\" by element \"([^\"]*)\" with value \"([^\"]*)\"$")
    public void selectRowByElement(ClickVariation clickVariation,
                                             String tableName, String columnName, String elementName, String elementValue) {
        tableSteps.selectRowByElement(clickVariation, tableName, columnName, elementName, elementValue);
    }

    @When("^user selects (single-click|double-click) row in the table \"([^\"]*)\" in the column \"([^\"]*)\" by element \"([^\"]*)\" with value \"([^\"]*)\" (\\d+) in order$")
    public void selectRowByElement(ClickVariation clickVariation,
                                             String tableName, String columnName, String elementName, String elementValue, int serialNumber) {
        tableSteps.selectRowByElement(clickVariation, tableName, columnName, elementName, elementValue, serialNumber);
    }

    @When("^user remembers the line number in the table \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\" in variable \"([^\"]*)\"$")
    public void saveRowNumber(String tableName, String cellText, String columnName, String stashKey) {
        tableSteps.saveRowNumber(tableName, cellText, columnName, stashKey);
    }

    @When("^user remembers the line number in the table \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\" in variable \"([^\"]*)\"$")
    public void saveRowNumberByIndex(String tableName, String cellText, String columnNumber, String stashKey) {
        tableSteps.saveRowNumberByIndex(tableName, cellText, columnNumber, stashKey);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void clickButtonByIndex(String buttonName, String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonByIndex(buttonName, tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" and row with number \"([^\"]*)\"$")
    public void clickButtonByIndexes(String buttonName, String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonByIndexes(buttonName, tableName, columnNumber, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" and row with number \"([^\"]*)\"$")
    public void clickButtonByIndex(String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonByIndex(tableName, columnNumber, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column with number \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void clickButtonByIndex(String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonByIndex(tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void clickButton(String buttonName, String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(buttonName, tableName, columnWithButtonName, cellText, columnName);
    }

    @When("^user press button \"([^\"]*)\" in the table \"([^\"]*)\" in the column \"([^\"]*)\" and row with number \"([^\"]*)\"$")
    public void clickButtonByRowIndex(String buttonName, String tableName, String columnName, String rowNumber) {
        tableSteps.clickButtonByRowIndex(buttonName, tableName, columnName, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column \"([^\"]*)\"and row with number \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnName, String rowNumber) {
        tableSteps.clickButton(tableName, columnName, rowNumber);
    }

    @When("^user press button in the table \"([^\"]*)\" in the column \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(tableName, columnWithButtonName, cellText, columnName);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" row with the value \"([^\"]*)\" in the column with number \"([^\"]*)\"$")
    public void selectRowInColumn(ClickVariation clickVariation,
                                            String tableName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumn(clickVariation, tableName, cellText, columnNumber);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" in group \"([^\"]*)\" row with the value \"([^\"]*)\" in the column with number \"([^\"]*)\"$")
    public void selectRowInColumnInGroup(ClickVariation clickVariation,
                                                   String tableName, String groupName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumnInGroup(clickVariation, tableName, groupName, cellText, columnNumber);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" row with value \"([^\"]*)\" in the column with number \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInColumnByNumber(ClickVariation clickVariation,
                                                    String tableName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumber(clickVariation, tableName, cellText, columnNumber, serialNumber);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" in group \"([^\"]*)\" row " +
            "with value \"([^\"]*)\" in the column with number \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInColumnByNumberInGroup(ClickVariation clickVariation,
                                                           String tableName, String groupName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumberInGroup(clickVariation, tableName, groupName, cellText, columnNumber, serialNumber);
    }

    @When("^user selects (single-click|double-click) row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\"$")
    public void selectRow(ClickVariation clickVariation, String cellText, String tableName) {
        tableSteps.selectRow(clickVariation, cellText, tableName);
    }

    @When("^user selects (single-click|double-click) row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\" in group \"([^\"]*)\"$")
    public void selectRowInGroup(ClickVariation clickVariation, String cellText, String tableName, String groupName) {
        tableSteps.selectRowInGroup(clickVariation, cellText, tableName, groupName);
    }

    @When("^user selects (single-click|double-click) row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\" (\\d+) in order$")
    public void selectRow(ClickVariation clickVariation,
                                    String cellText, String tableName, int serialNumber) {
        tableSteps.selectRow(clickVariation, cellText, tableName, serialNumber);
    }

    @When("^user selects (single-click|double-click) row by text in any cell \"([^\"]*)\" in the table \"([^\"]*)\" in group \"([^\"]*)\" (\\d+) in order$")
    public void selectRowInGroup(ClickVariation clickVariation,
                                           String cellText, String tableName, String groupName, int serialNumber) {
        tableSteps.selectRowInGroup(clickVariation, cellText, tableName, groupName, serialNumber);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" row with values$")
    public void selectRow(ClickVariation clickVariation, String tableName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(clickVariation, tableName, row);
    }

    @When("^user selects (single-click|double-click) in the table \"([^\"]*)\" in group \"([^\"]*)\" row with values$")
    public void selectRow(ClickVariation clickVariation, String tableName, String groupName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(clickVariation, tableName, groupName, row);
    }

    @When("^user checks table in the table \"([^\"]*)\" any row of the column \"([^\"]*)\" contains text \"([^\"]*)\"$")
    public void containsText(String tableName, String columnName, String cellText) {
        tableSteps.containsText(tableName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" any row of the column \"([^\"]*)\" contains text \"([^\"]*)\"$")
    public void containsTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.containsTextInGroup(tableName, groupName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in any row of the column \"([^\"]*)\" there is a text strictly equal \"([^\"]*)\"$")
    public void hasText(String tableName, String columnName, String cellText) {
        tableSteps.hasText(tableName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" in any row of the column \"([^\"]*)\" there is a text strictly equal \"([^\"]*)\"$")
    public void hasTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.hasTextInGroup(tableName, groupName, columnName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" any cell contains text \"([^\"]*)\"$")
    public void containsText(String tableName, String cellText) {
        tableSteps.containsText(tableName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" any cell contains text \"([^\"]*)\"$")
    public void containsTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.containsTextInGroup(tableName, groupName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in any cell there is a text strictly equal \"([^\"]*)\"$")
    public void hasText(String tableName, String cellText) {
        tableSteps.hasText(tableName, cellText);
    }

    @When("^user checks table in the table \"([^\"]*)\" in group \"([^\"]*)\" in any cell there is a text strictly equal \"([^\"]*)\"$")
    public void hasTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.hasTextInGroup(tableName, groupName, cellText);
    }

    @When("^user checks table row cells \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void checkRow(String tableName, String cellText, String columnName, DataTable data) {
        tableSteps.checkRow(tableName, cellText, columnName, data);
    }

    @When("^user checks table row cells \"([^\"]*)\" in group \"([^\"]*)\" for value \"([^\"]*)\" of the column \"([^\"]*)\"$")
    public void checkRowInGroup(String tableName, String groupName, String cellText, String columnName, DataTable data) {
        tableSteps.checkRowInGroup(tableName, groupName, cellText, columnName, data);
    }

    @When("^user checks table row cells \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void checkRowByIndex(String tableName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkRowByIndex(tableName, cellText, columnNumber, data);
    }

    @When("^user checks table row cells \"([^\"]*)\" in group \"([^\"]*)\" for value \"([^\"]*)\" of the column with number \"([^\"]*)\"$")
    public void checkRowByIndexInGroup(String tableName, String groupName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkRowByIndexInGroup(tableName, groupName, cellText, columnNumber, data);
    }
}
