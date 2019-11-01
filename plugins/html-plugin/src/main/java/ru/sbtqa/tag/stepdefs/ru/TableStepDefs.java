package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.ru.Когда;
import io.cucumber.datatable.DataTable;
import java.util.List;
import ru.sbtqa.tag.pagefactory.html.junit.TableSteps;
import ru.sbtqa.tag.pagefactory.transformer.ClickVariation;

public class TableStepDefs {

    private final TableSteps tableSteps = TableSteps.getInstance();

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) первую по-порядку строку таблицы \"([^\"]*)\"$")
    public void selectRow(ClickVariation clickVariation, String tableName) {
        tableSteps.selectRow(clickVariation, tableName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) первую по-порядку строку таблицы \"([^\"]*)\" в группе \"([^\"]*)\"$")
    public void selectRowInGroup(ClickVariation clickVariation, String tableName, String groupName) {
        tableSteps.selectRowInGroup(clickVariation, tableName, groupName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку с номером \"([^\"]*)\" в таблице \"([^\"]*)\"$")
    public void selectRowByNumber(ClickVariation clickVariation, String rowIndex, String tableName) {
        tableSteps.selectRowByNumber(clickVariation, rowIndex, tableName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку с номером \"([^\"]*)\" в таблице \"([^\"]*)\" в группе \"([^\"]*)\"$")
    public void selectRowByNumberInGroup(ClickVariation clickVariation,
                                         String rowIndex, String tableName, String groupName) {
        tableSteps.selectRowByNumberInGroup(clickVariation, rowIndex, tableName, groupName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\"$")
    public void selectRow(ClickVariation clickVariation,
                          String tableName, String cellText, String columnName) {
        tableSteps.selectRow(clickVariation, tableName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\"$")
    public void selectRowInGroup(ClickVariation clickVariation,
                                 String tableName, String gropName, String cellText, String columnName) {
        tableSteps.selectRowInGroup(clickVariation, tableName, gropName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRow(ClickVariation clickVariation,
                          String tableName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRow(clickVariation, tableName, cellText, columnName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInGroup(ClickVariation clickVariation,
                                 String tableName, String groupName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRowInGroup(clickVariation, tableName, groupName, cellText, columnName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" по элементу \"([^\"]*)\" со значением \"([^\"]*)\"$")
    public void selectRowByElement(ClickVariation clickVariation,
                                   String tableName, String columnName, String elementName, String elementValue) {
        tableSteps.selectRowByElement(clickVariation, tableName, columnName, elementName, elementValue);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку в таблице \"([^\"]*)\" " +
            "в столбце \"([^\"]*)\" по элементу \"([^\"]*)\" со значением \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowByElement(ClickVariation clickVariation,
                                   String tableName, String columnName, String elementName, String elementValue, int serialNumber) {
        tableSteps.selectRowByElement(clickVariation, tableName, columnName, elementName, elementValue, serialNumber);
    }

    @Когда("^(?:пользователь |он )?запоминает номер строки в таблице \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\" в переменную \"([^\"]*)\"$")
    public void saveRowNumber(String tableName, String cellText, String columnName, String stashKey) {
        tableSteps.saveRowNumber(tableName, cellText, columnName, stashKey);
    }

    @Когда("^(?:пользователь |он )?запоминает номер строки в таблице \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\" в переменную \"([^\"]*)\"$")
    public void saveRowNumberByIndex(String tableName, String cellText, String columnNumber, String stashKey) {
        tableSteps.saveRowNumberByIndex(tableName, cellText, columnNumber, stashKey);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void clickButtonByIndex(String buttonName, String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonByIndex(buttonName, tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" и строке с номером \"([^\"]*)\"$")
    public void clickButtonByIndexes(String buttonName, String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonByIndexes(buttonName, tableName, columnNumber, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" и строке с номером \"([^\"]*)\"$")
    public void clickButtonByIndex(String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonByIndex(tableName, columnNumber, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void clickButtonByIndex(String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonByIndex(tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void clickButton(String buttonName, String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(buttonName, tableName, columnWithButtonName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" и строке с номером \"([^\"]*)\"$")
    public void clickButtonByRowIndex(String buttonName, String tableName, String columnName, String rowNumber) {
        tableSteps.clickButtonByRowIndex(buttonName, tableName, columnName, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце \"([^\"]*)\"и строке с номером \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnName, String rowNumber) {
        tableSteps.clickButton(tableName, columnName, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(tableName, columnWithButtonName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\"$")
    public void selectRowInColumn(ClickVariation clickVariation,
                                  String tableName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumn(clickVariation, tableName, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\"$")
    public void selectRowInColumnInGroup(ClickVariation clickVariation,
                                         String tableName, String groupName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumnInGroup(clickVariation, tableName, groupName, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" строку " +
            "со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInColumnByNumber(ClickVariation clickVariation,
                                          String tableName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumber(clickVariation, tableName, cellText, columnNumber, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку " +
            "со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInColumnByNumberInGroup(ClickVariation clickVariation,
                                                 String tableName, String groupName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumberInGroup(clickVariation, tableName, groupName, cellText, columnNumber, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\"$")
    public void selectRow(ClickVariation clickVariation, String cellText, String tableName) {
        tableSteps.selectRow(clickVariation, cellText, tableName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\" в группе \"([^\"]*)\"$")
    public void selectRowInGroup(ClickVariation clickVariation, String cellText, String tableName, String groupName) {
        tableSteps.selectRowInGroup(clickVariation, cellText, tableName, groupName);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRow(ClickVariation clickVariation,
                          String cellText, String tableName, int serialNumber) {
        tableSteps.selectRow(clickVariation, cellText, tableName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\" в группе \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInGroup(ClickVariation clickVariation,
                                 String cellText, String tableName, String groupName, int serialNumber) {
        tableSteps.selectRowInGroup(clickVariation, cellText, tableName, groupName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" строку со значениями$")
    public void selectRow(ClickVariation clickVariation, String tableName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(clickVariation, tableName, row);
    }

    @Когда("^(?:пользователь |он )?выбирает (одинарным кликом|двойным кликом) в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значениями$")
    public void selectRow(ClickVariation clickVariation, String tableName, String groupName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(clickVariation, tableName, groupName, row);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" содержится текст \"([^\"]*)\"$")
    public void containsText(String tableName, String columnName, String cellText) {
        tableSteps.containsText(tableName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" содержится текст \"([^\"]*)\"$")
    public void containsTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.containsTextInGroup(tableName, groupName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" есть текст строго равный \"([^\"]*)\"$")
    public void hasText(String tableName, String columnName, String cellText) {
        tableSteps.hasText(tableName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" есть текст строго равный \"([^\"]*)\"$")
    public void hasTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.hasTextInGroup(tableName, groupName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой ячейке содержится текст \"([^\"]*)\"$")
    public void containsText(String tableName, String cellText) {
        tableSteps.containsText(tableName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой ячейке содержится текст \"([^\"]*)\"$")
    public void containsTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.containsTextInGroup(tableName, groupName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой ячейке есть текст строго равный \"([^\"]*)\"$")
    public void hasText(String tableName, String cellText) {
        tableSteps.hasText(tableName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой ячейке есть текст строго равный \"([^\"]*)\"$")
    public void hasTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.hasTextInGroup(tableName, groupName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void checkRow(String tableName, String cellText, String columnName, DataTable data) {
        tableSteps.checkRow(tableName, cellText, columnName, data);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" в группе \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void checkRowInGroup(String tableName, String groupName, String cellText, String columnName, DataTable data) {
        tableSteps.checkRowInGroup(tableName, groupName, cellText, columnName, data);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void checkRowByIndex(String tableName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkRowByIndex(tableName, cellText, columnNumber, data);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" в группе \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void checkRowByIndexInGroup(String tableName, String groupName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkRowByIndexInGroup(tableName, groupName, cellText, columnNumber, data);
    }
}
