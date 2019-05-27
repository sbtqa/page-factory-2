package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.ru.Когда;
import java.util.List;
import ru.sbtqa.tag.pagefactory.html.junit.TableSteps;
import ru.sbtqa.tag.pagefactory.transformer.ClickVariationTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.ClickVariation;

public class TableStepDefs {

    private final TableSteps tableSteps = TableSteps.getInstance();

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?первую по-порядку строку таблицы \"([^\"]*)\"$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName) {
        tableSteps.selectRow(doubleClick, tableName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?первую по-порядку строку таблицы \"([^\"]*)\" в группе \"([^\"]*)\"$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName, String groupName) {
        tableSteps.selectRowInGroup(doubleClick, tableName, groupName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку с номером \"([^\"]*)\" в таблице \"([^\"]*)\"$")
    public void selectRowByNumber(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String rowIndex, String tableName) {
        tableSteps.selectRowByNumber(doubleClick, rowIndex, tableName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку с номером \"([^\"]*)\" в таблице \"([^\"]*)\" в группе \"([^\"]*)\"$")
    public void selectRowByNumberInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                   String rowIndex, String tableName, String groupName) {
        tableSteps.selectRowByNumberInGroup(doubleClick, rowIndex, tableName, groupName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\"$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                    String tableName, String cellText, String columnName) {
        tableSteps.selectRow(doubleClick, tableName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\"$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                           String tableName, String gropName, String cellText, String columnName) {
        tableSteps.selectRowInGroup(doubleClick, tableName, gropName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                    String tableName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRow(doubleClick, tableName, cellText, columnName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                           String tableName, String groupName, String cellText, String columnName, int serialNumber) {
        tableSteps.selectRowInGroup(doubleClick, tableName, groupName, cellText, columnName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" по элементу \"([^\"]*)\" со значением \"([^\"]*)\"$")
    public void selectRowByElement(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                             String tableName, String columnName, String elementName, String elementValue) {
        tableSteps.selectRowByElement(doubleClick, tableName, columnName, elementName, elementValue);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку в таблице \"([^\"]*)\" " +
            "в столбце \"([^\"]*)\" по элементу \"([^\"]*)\" со значением \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowByElement(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                             String tableName, String columnName, String elementName, String elementValue, int serialNumber) {
        tableSteps.selectRowByElement(doubleClick, tableName, columnName, elementName, elementValue, serialNumber);
    }

    @Когда("^(?:пользователь |он )?запоминает номер строки в таблице \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\" в переменную \"([^\"]*)\"$")
    public void saveRowNumber(String tableName, String cellText, String columnName, String stashKey) {
        tableSteps.saveRowNumber(tableName, cellText, columnName, stashKey);
    }

    @Когда("^(?:пользователь |он )?запоминает номер строки в таблице \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\" в переменную \"([^\"]*)\"$")
    public void saveRowNumberViaColumnIndex(String tableName, String cellText, String columnNumber, String stashKey) {
        tableSteps.saveRowNumberViaColumnIndex(tableName, cellText, columnNumber, stashKey);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void clickButtonViaColumnIndex(String buttonName, String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonViaColumnIndex(buttonName, tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" и строке с номером \"([^\"]*)\"$")
    public void clickButtonViaIndexes(String buttonName, String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonViaIndexes(buttonName, tableName, columnNumber, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" и строке с номером \"([^\"]*)\"$")
    public void clickButtonViaColumnIndex(String tableName, String columnNumber, String rowNumber) {
        tableSteps.clickButtonViaColumnIndex(tableName, columnNumber, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце с номером \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void clickButtonViaColumnIndex(String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        tableSteps.clickButtonViaColumnIndex(tableName, columnWithButtonNumber, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void clickButton(String buttonName, String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(buttonName, tableName, columnWithButtonName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку \"([^\"]*)\" в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" и строке с номером \"([^\"]*)\"$")
    public void clickButtonViaRowIndex(String buttonName, String tableName, String columnName, String rowNumber) {
        tableSteps.clickButtonViaRowIndex(buttonName, tableName, columnName, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце \"([^\"]*)\"и строке с номером \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnName, String rowNumber) {
        tableSteps.clickButton(tableName, columnName, rowNumber);
    }

    @Когда("^(?:пользователь |он )?нажимает кнопку в таблице \"([^\"]*)\" в столбце \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void clickButton(String tableName, String columnWithButtonName, String cellText, String columnName) {
        tableSteps.clickButton(tableName, columnWithButtonName, cellText, columnName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\"$")
    public void selectRowInColumn(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                            String tableName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumn(doubleClick, tableName, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\"$")
    public void selectRowInColumnInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                   String tableName, String groupName, String cellText, String columnNumber) {
        tableSteps.selectRowInColumnInGroup(doubleClick, tableName, groupName, cellText, columnNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" строку " +
            "со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInColumnByNumber(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                    String tableName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumber(doubleClick, tableName, cellText, columnNumber, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку " +
            "со значением \"([^\"]*)\" в столбце с номером \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInColumnByNumberInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                                           String tableName, String groupName, String cellText, String columnNumber, int serialNumber) {
        tableSteps.selectRowInColumnByNumberInGroup(doubleClick, tableName, groupName, cellText, columnNumber, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\"$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String cellText, String tableName) {
        tableSteps.selectRow(doubleClick, cellText, tableName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\" в группе \"([^\"]*)\"$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String cellText, String tableName, String groupName) {
        tableSteps.selectRowInGroup(doubleClick, cellText, tableName, groupName);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                    String cellText, String tableName, int serialNumber) {
        tableSteps.selectRow(doubleClick, cellText, tableName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?строку с текстом в любой ячейке \"([^\"]*)\" в таблице \"([^\"]*)\" в группе \"([^\"]*)\" (\\d+) по-порядку$")
    public void selectRowInGroup(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick,
                                           String cellText, String tableName, String groupName, int serialNumber) {
        tableSteps.selectRowInGroup(doubleClick, cellText, tableName, groupName, serialNumber);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" строку со значениями$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(doubleClick, tableName, row);
    }

    @Когда("^(?:пользователь |он )?выбирает (двойным кликом )?в таблице \"([^\"]*)\" в группе \"([^\"]*)\" строку со значениями$")
    public void selectRow(@Transform(ClickVariationTransformer.class) ClickVariation doubleClick, String tableName, String groupName, DataTable data) {
        List<String> row = data.asList(String.class);
        tableSteps.selectRow(doubleClick, tableName, groupName, row);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" содержится текст \"([^\"]*)\"$")
    public void tableHasText(String tableName, String columnName, String cellText) {
        tableSteps.tableHasText(tableName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" содержится текст \"([^\"]*)\"$")
    public void tableHasTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.tableHasTextInGroup(tableName, groupName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" есть текст строго равный \"([^\"]*)\"$")
    public void tableContainsText(String tableName, String columnName, String cellText) {
        tableSteps.tableContainsText(tableName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой строке столбца \"([^\"]*)\" есть текст строго равный \"([^\"]*)\"$")
    public void tableContainsTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        tableSteps.tableContainsTextInGroup(tableName, groupName, columnName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой ячейке содержится текст \"([^\"]*)\"$")
    public void tableCellHasText(String tableName, String cellText) {
        tableSteps.tableCellHasText(tableName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой ячейке содержится текст \"([^\"]*)\"$")
    public void tableCellHasTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.tableCellHasTextInGroup(tableName, groupName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в любой ячейке есть текст строго равный \"([^\"]*)\"$")
    public void tableCellContainsText(String tableName, String cellText) {
        tableSteps.tableCellContainsText(tableName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет, что в таблице \"([^\"]*)\" в группе \"([^\"]*)\" в любой ячейке есть текст строго равный \"([^\"]*)\"$")
    public void tableCellContainsTextInGroup(String tableName, String groupName, String cellText) {
        tableSteps.tableCellContainsTextInGroup(tableName, groupName, cellText);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void checkTableRow(String tableName, String cellText, String columnName, DataTable data) {
        tableSteps.checkTableRow(tableName, cellText, columnName, data);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" в группе \"([^\"]*)\" для значения \"([^\"]*)\" столбца \"([^\"]*)\"$")
    public void checkTableRowInGroup(String tableName, String groupName, String cellText, String columnName, DataTable data) {
        tableSteps.checkTableRowInGroup(tableName, groupName, cellText, columnName, data);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void checkTableRowViaColumnIndexes(String tableName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkTableRowViaColumnIndexes(tableName, cellText, columnNumber, data);
    }

    @Когда("^(?:пользователь |он )?проверяет ячейки строки таблицы \"([^\"]*)\" в группе \"([^\"]*)\" для значения \"([^\"]*)\" столбца с номером \"([^\"]*)\"$")
    public void checkTableRowViaColumnIndexesInGroup(String tableName, String groupName, String cellText, String columnNumber, DataTable data) {
        tableSteps.checkTableRowViaColumnIndexesInGroup(tableName, groupName, cellText, columnNumber, data);
    }
}
