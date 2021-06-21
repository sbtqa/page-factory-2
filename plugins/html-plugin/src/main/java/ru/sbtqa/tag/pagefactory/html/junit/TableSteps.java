package ru.sbtqa.tag.pagefactory.html.junit;

import io.cucumber.datatable.DataTable;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import ru.sbtqa.tag.pagefactory.elements.table.TableAbstract;
import ru.sbtqa.tag.pagefactory.transformer.ClickVariation;
import ru.sbtqa.tag.pagefactory.transformer.SearchStrategy;

import static java.lang.String.format;
import static java.lang.ThreadLocal.withInitial;

public class TableSteps implements Steps {

    static final ThreadLocal<TableSteps> storage = withInitial(TableSteps::new);

    public static TableSteps getInstance() {
        return storage.get();
    }

    private static final String TEXT_NOT_FOUND_TEMPLATE = "The text \"%s\" not found in the table \"%s\"";
    private static final String TEXT_NOT_FOUND_IN_GROUP_TEMPLATE = TEXT_NOT_FOUND_TEMPLATE + " in group \"%s\"";
    private static final String COLUMN_TEXT_NOT_FOUND_TEMPLATE = "The text \"%s\" not found in the column \"%s\" of the table \"%s\"";
    private static final String COLUMN_TEXT_NOT_FOUND_IN_GROUP_TEMPLATE = COLUMN_TEXT_NOT_FOUND_TEMPLATE + " in group \"%s\"";


    /**
     * Selects the first row of the table in order
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @return Returns itself
     */
    public TableSteps selectRow(ClickVariation clickVariation, String tableName) {
        getTable(tableName).selectRow(0, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects the first in order of the group row
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param groupName group name
     * @return Returns itself
     */
    public TableSteps selectRowInGroup(ClickVariation clickVariation, String tableName, String groupName) {
        getTable(tableName).selectRowInGroup(groupName, 0, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row of a table by its index
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param rowIndex row index
     * @param tableName table name
     * @return Returns itself
     */
    public TableSteps selectRowByNumber(ClickVariation clickVariation, String rowIndex, String tableName) {
        getTable(tableName).selectRow(Integer.parseInt(rowIndex) - 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row of a group by its index
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param rowIndex row index
     * @param tableName table name
     * @param groupName group name
     * @return Returns itself
     */
    public TableSteps selectRowByNumberInGroup(ClickVariation clickVariation, String rowIndex, String tableName, String groupName) {
        getTable(tableName).selectRowInGroup(groupName, Integer.parseInt(rowIndex) - 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects the row in the table by the cell text in the column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param cellText cell text
     * @param columnName column name
     * @return Returns itself
     */
    public TableSteps selectRow(ClickVariation clickVariation, String tableName, String cellText, String columnName) {
        getTable(tableName).selectRow(columnName, cellText, 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects the row in the group by the cell text in the column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param groupName
     * @param cellText cell text
     * @param columnName column name
     * @return Returns itself
     */
    public TableSteps selectRowInGroup(ClickVariation clickVariation, String tableName, String groupName, String cellText, String columnName) {
        getTable(tableName).selectRowInGroup(groupName, columnName, cellText, 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects the row in the table by the cell text in the column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param cellText cell text
     * @param columnName column name
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return Returns itself
     */
    public TableSteps selectRow(ClickVariation clickVariation, String tableName, String cellText, String columnName, int serialNumber) {
        getTable(tableName).selectRow(columnName, cellText, serialNumber, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects the row in the table by the cell text in the column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param groupName group name
     * @param cellText cell text
     * @param columnName column name
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return Returns itself
     */
    public TableSteps selectRowInGroup(ClickVariation clickVariation, String tableName, String groupName, String cellText, String columnName, int serialNumber) {
        getTable(tableName).selectRowInGroup(groupName, columnName, cellText, serialNumber, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row in a table by element value in a column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param columnName column name
     * @param elementName element name
     * @param elementValue element value
     * @return Returns itself
     */
    public TableSteps selectRowByElement(ClickVariation clickVariation, String tableName, String columnName, String elementName, String elementValue) {
        getTable(tableName).selectRowByElement(columnName, elementName, elementValue, 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row in a table by element value in a column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param columnName column name
     * @param elementName element name
     * @param elementValue element value
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return Returns itself
     */
    public TableSteps selectRowByElement(ClickVariation clickVariation, String tableName, String columnName, String elementName, String elementValue, int serialNumber) {
        getTable(tableName).selectRowByElement(columnName, elementName, elementValue, serialNumber, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Saves in Stash the row number in which the specified column contains the passed value
     *
     * @param tableName table name
     * @param cellText cell text
     * @param columnName column name
     * @param stashKey Stash key
     * @return Returns itself
     */
    public TableSteps saveRowNumber(String tableName, String cellText, String columnName, String stashKey) {
        getTable(tableName).saveRowNumber(columnName, cellText, stashKey, 1);
        return this;
    }

    /**
     * Saves in Stash the row number in which the specified column contains the passed value
     *
     * @param tableName table name
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param stashKey Stash key
     * @return Returns itself
     */
    public TableSteps saveRowNumberByIndex(String tableName, String cellText, String columnNumber, String stashKey) {
        getTable(tableName).saveRowNumber(Integer.parseInt(columnNumber) - 1, cellText, stashKey, 1);
        return this;
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified text value
     * and presses the button with the specified name in this line in the column with the index "columnWithButtonIndex"
     *
     * @param buttonName button name
     * @param tableName table name
     * @param columnWithButtonNumber column index in which the button is located
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @return Returns itself
     */
    public TableSteps clickButtonByIndex(String buttonName, String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        getTable(tableName).clickButton(Integer.parseInt(columnNumber) - 1, cellText, buttonName, Integer.parseInt(columnWithButtonNumber) - 1);
        return this;
    }

    /**
     * Press the button with the specified name in the specified cell
     *
     * @param buttonName button name
     * @param tableName table name
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param rowNumber row number
     * @return Returns itself
     */
    public TableSteps clickButtonByIndexes(String buttonName, String tableName, String columnNumber, String rowNumber) {
        getTable(tableName).clickButton(Integer.parseInt(columnNumber) - 1, Integer.parseInt(rowNumber) - 1, buttonName);
        return this;
    }

    /**
     * Presses a button in a given cell
     *
     * @param tableName table name
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param rowNumber row number
     * @return Returns itself
     */
    public TableSteps clickButtonByIndex(String tableName, String columnNumber, String rowNumber) {
        getTable(tableName).clickButton(Integer.parseInt(columnNumber) - 1, Integer.parseInt(rowNumber) - 1);
        return this;
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified
     * text value and presses the button in this line in the column with the index "columnWithButtonIndex"
     *
     * @param tableName table name
     * @param columnWithButtonNumber column index in which the button is located
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @return Returns itself
     */
    public TableSteps clickButtonByIndex(String tableName, String columnWithButtonNumber, String cellText, String columnNumber) {
        getTable(tableName).clickButton(Integer.parseInt(columnNumber) - 1, cellText, Integer.parseInt(columnWithButtonNumber) - 1);
        return this;
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified text value and presses
     * the button with the specified name in this line in the column with the name "columnWithButtonName"
     *
     * @param buttonName button name
     * @param tableName table name
     * @param columnWithButtonName name of the column in which the button is located
     * @param cellText cell text
     * @param columnName column name
     * @return Returns itself
     */
    public TableSteps clickButton(String buttonName, String tableName, String columnWithButtonName, String cellText, String columnName) {
        getTable(tableName).clickButton(columnName, cellText, buttonName, columnWithButtonName);
        return this;
    }

    /**
     * Press the button with the specified name in the specified cell
     *
     * @param buttonName button name
     * @param tableName table name
     * @param columnName column name
     * @param rowNumber row number
     * @return Returns itself
     */
    public TableSteps clickButtonByRowIndex(String buttonName, String tableName, String columnName, String rowNumber) {
        getTable(tableName).clickButton(columnName, Integer.parseInt(rowNumber) - 1, buttonName);
        return this;
    }

    /**
     * Presses a button in a given cell
     *
     * @param tableName table name
     * @param columnName column name
     * @param rowNumber row number
     * @return Returns itself
     */
    public TableSteps clickButton(String tableName, String columnName, String rowNumber) {
        getTable(tableName).clickButton(columnName, Integer.parseInt(rowNumber) - 1);
        return this;
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified text value and
     * presses the button in this line in the column with the name "columnWithButtonName"
     *
     * @param tableName table name
     * @param columnWithButtonName name of the column in which the button is located
     * @param cellText cell text
     * @param columnName column name
     * @return Returns itself
     */
    public TableSteps clickButton(String tableName, String columnWithButtonName, String cellText, String columnName) {
        getTable(tableName).clickButton(columnName, cellText, columnWithButtonName);
        return this;
    }

    /**
     * Selects a row by text in a column with the specified index
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @return Returns itself
     */
    public TableSteps selectRowInColumn(ClickVariation clickVariation, String tableName, String cellText, String columnNumber) {
        getTable(tableName).selectRow(Integer.parseInt(columnNumber) - 1, cellText, 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row in a group by text in a column with the specified index
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param groupName group name
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @return Returns itself
     */
    public TableSteps selectRowInColumnInGroup(ClickVariation clickVariation, String tableName, String groupName, String cellText, String columnNumber) {
        getTable(tableName).selectRowInGroup(groupName, Integer.parseInt(columnNumber) - 1, cellText, 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row by text in a column with the specified index
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return Returns itself
     */
    public TableSteps selectRowInColumnByNumber(ClickVariation clickVariation, String tableName, String cellText, String columnNumber, int serialNumber) {
        getTable(tableName).selectRow(Integer.parseInt(columnNumber) - 1, cellText, serialNumber, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row in a group by text in a column with the specified index
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param groupName group name
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return Returns itself
     */
    public TableSteps selectRowInColumnByNumberInGroup(ClickVariation clickVariation, String tableName, String groupName, String cellText, String columnNumber, int serialNumber) {
        getTable(tableName).selectRowInGroup(groupName, Integer.parseInt(columnNumber) - 1, cellText, serialNumber, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row by text in any column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param cellText cell text
     * @param tableName table name
     * @return Returns itself
     */
    public TableSteps selectRow(ClickVariation clickVariation, String cellText, String tableName) {
        getTable(tableName).selectRow(cellText, 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row in a group by text in any column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param cellText cell text
     * @param tableName table name
     * @param groupName group name
     * @return Returns itself
     */
    public TableSteps selectRowInGroup(ClickVariation clickVariation, String cellText, String tableName, String groupName) {
        getTable(tableName).selectRowInGroup(groupName, cellText, 1, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row by text in any column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param cellText cell text
     * @param tableName table name
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return Returns itself
     */
    public TableSteps selectRow(ClickVariation clickVariation, String cellText, String tableName, int serialNumber) {
        getTable(tableName).selectRow(cellText, serialNumber, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects a row in a group by text in any column
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param cellText cell text
     * @param tableName table name
     * @param groupName group name
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return Returns itself
     */
    public TableSteps selectRowInGroup(ClickVariation clickVariation, String cellText, String tableName, String groupName, int serialNumber) {
        getTable(tableName).selectRowInGroup(groupName, cellText, serialNumber, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects the line that matches the expected line, regardless of the order of the elements
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param data unordered list of line elements
     * @return Returns itself
     */
    public TableSteps selectRow(ClickVariation clickVariation, String tableName, List data) {
        getTable(tableName).selectRow(data, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Selects the row in the group that matches the expected row, regardless of the order of the elements
     *
     * @param clickVariation {@code ClickVariation.DOUBLE_CLICK}, if you need to make a double-click selection,
     * and {@code ClickVariation.CLICK}, if single
     * @param tableName table name
     * @param groupName group name
     * @param data unordered list of line elements
     * @return Returns itself
     */
    public TableSteps selectRow(ClickVariation clickVariation, String tableName, String groupName, List data) {
        getTable(tableName).selectRowInGroup(groupName, data, clickVariation.isDoubleClick());
        return this;
    }

    /**
     * Checks for text in a column
     *
     * @param tableName table name
     * @param columnName column name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps hasText(String tableName, String columnName, String cellText) {
        Assert.assertTrue(format(COLUMN_TEXT_NOT_FOUND_TEMPLATE, cellText, columnName, tableName),
                getTable(tableName).hasText(columnName, cellText, SearchStrategy.EQUALS));
        return this;
    }

    /**
     * Checks for text in a column in a group
     *
     * @param tableName table name
     * @param groupName group name
     * @param columnName column name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps hasTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        Assert.assertTrue(format(COLUMN_TEXT_NOT_FOUND_IN_GROUP_TEMPLATE, cellText, columnName, tableName, groupName),
                getTable(tableName).hasTextInGroup(groupName, columnName, cellText, SearchStrategy.EQUALS));
        return this;
    }

    /**
     * Checks for text in a column
     *
     * @param tableName table name
     * @param columnName column name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps containsText(String tableName, String columnName, String cellText) {
        Assert.assertTrue(format(COLUMN_TEXT_NOT_FOUND_TEMPLATE, cellText, columnName, tableName),
                getTable(tableName).hasText(columnName, cellText, SearchStrategy.CONTAINS));
        return this;
    }

    /**
     * Checks for text in a column in a group
     *
     * @param tableName table name
     * @param groupName group name
     * @param columnName column name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps containsTextInGroup(String tableName, String groupName, String columnName, String cellText) {
        Assert.assertTrue(format(COLUMN_TEXT_NOT_FOUND_IN_GROUP_TEMPLATE, cellText, columnName, tableName, groupName),
                getTable(tableName).hasTextInGroup(groupName, columnName, cellText, SearchStrategy.CONTAINS));
        return this;
    }

    /**
     * Checks for text in a table
     *
     * @param tableName table name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps hasText(String tableName, String cellText) {
        Assert.assertTrue(format(TEXT_NOT_FOUND_TEMPLATE, cellText, tableName),
                getTable(tableName).hasText(cellText, SearchStrategy.EQUALS));
        return this;
    }

    /**
     * Checks for the presence of text in a table in a group
     *
     * @param tableName table name
     * @param groupName group name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps hasTextInGroup(String tableName, String groupName, String cellText) {
        Assert.assertTrue(format(TEXT_NOT_FOUND_IN_GROUP_TEMPLATE, cellText, tableName, groupName),
                getTable(tableName).hasTextInGroup(groupName, cellText, SearchStrategy.EQUALS));
        return this;
    }

    /**
     * Checks for text in a table
     *
     * @param tableName table name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps containsText(String tableName, String cellText) {
        Assert.assertTrue(format(TEXT_NOT_FOUND_TEMPLATE, cellText, tableName),
                getTable(tableName).hasText(cellText, SearchStrategy.CONTAINS));
        return this;
    }

    /**
     * Checks for the presence of text in a table in a group
     *
     * @param tableName table name
     * @param groupName group name
     * @param cellText cell text
     * @return Returns itself
     */
    public TableSteps containsTextInGroup(String tableName, String groupName, String cellText) {
        Assert.assertTrue(format(TEXT_NOT_FOUND_IN_GROUP_TEMPLATE, cellText, tableName, groupName),
                getTable(tableName).hasTextInGroup(groupName, cellText, SearchStrategy.CONTAINS));
        return this;
    }

    /**
     * Finds a row in which the given value contains the passed value and checks for it
     * the correspondence of the values in the columns
     *
     * @param tableName table name
     * @param cellText cell text
     * @param columnName column name
     * @param data set for checking values in the form of "column name - text value". For example:
     * | Column 1 | cell text |
     * | Column 2 | cell text |
     * @return Returns itself
     */
    public TableSteps checkRow(String tableName, String cellText, String columnName, DataTable data) {
        Map<String, String> dataMap = data.asMap(String.class, String.class);
        getTable(tableName).checkRow(columnName, cellText, dataMap);
        return this;
    }

    /**
     * Finds in the group a row that contains the passed value in the specified column and checks for it
     * the correspondence of the values in the columns
     *
     * @param tableName table name
     * @param groupName group name
     * @param cellText cell text
     * @param columnName column name
     * @param data set for checking values in the form of "column name - text value". For example:
     * | Column 1 | cell text |
     * | Column 2 | cell text |
     * @return Returns itself
     */
    public TableSteps checkRowInGroup(String tableName, String groupName, String cellText, String columnName, DataTable data) {
        Map<String, String> dataMap = data.asMap(String.class, String.class);
        getTable(tableName).checkRowInGroup(groupName, columnName, cellText, dataMap);
        return this;
    }

    /**
     * Finds a row that contains the passed value in the specified column and checks for it
     * the correspondence of the values in the columns
     *
     * @param tableName table name
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param data set for checking values in the form of "column number - text value". For example:
     * | 1 | cell text |
     * | 2 | cell text |
     * @return Returns itself
     */
    public TableSteps checkRowByIndex(String tableName, String cellText, String columnNumber, DataTable data) {
        Map<Integer, String> dataMap = data.asMap(Integer.class, String.class);
        getTable(tableName).checkRow(Integer.parseInt(columnNumber), cellText, dataMap);
        return this;
    }

    /**
     * Finds in the group a row that contains the passed value in the specified column and checks for it
     * the correspondence of the values in the columns
     *
     * @param tableName table name
     * @param groupName group name
     * @param cellText cell text
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param data set for checking values in the form of "column number - text value". For example:
     * | 1 | cell text |
     * | 2 | cell text |
     * @return Returns itself
     */
    public TableSteps checkRowByIndexInGroup(String tableName, String groupName, String cellText, String columnNumber, DataTable data) {
        Map<Integer, String> dataMap = data.asMap(Integer.class, String.class);
        getTable(tableName).checkRowInGroup(groupName, Integer.parseInt(columnNumber), cellText, dataMap);
        return this;
    }

    private TableAbstract getTable(String tableName) {
        return getFindUtils().find(tableName, TableAbstract.class);
    }
}
