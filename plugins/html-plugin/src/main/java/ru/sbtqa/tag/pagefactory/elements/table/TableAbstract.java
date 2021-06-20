package ru.sbtqa.tag.pagefactory.elements.table;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.elements.BaseElement;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exception.ButtonNotFoundError;
import ru.sbtqa.tag.pagefactory.exception.TableNotContainTextError;
import ru.sbtqa.tag.pagefactory.exception.TableRowNotFoundError;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.transformer.SearchStrategy;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import static java.lang.String.format;

public abstract class TableAbstract extends TypifiedElement {

    private static final String XPATH_INDEX_TEMPLATE = "[%s]";
    private static final String COLUMN_RELATIVE = "./ancestor::tr//td" + XPATH_INDEX_TEMPLATE;
    private static final String COMPOSITE_CELL_TEXT = "./ancestor::td//*[text()]";
    private static final String CONTAINS_TEXT_XPATH_TEMPLATE = "//*[contains(text(),'%s')]";
    private static final String BUTTON_XPATH = ".//button";
    private static final String TEXT_PART_XPATH_TEMPLATE = "[text()='%s']";
    private static final String TEXT_XPATH = "//*" + TEXT_PART_XPATH_TEMPLATE;

    private static final Logger LOG = LoggerFactory.getLogger(TableAbstract.class);

    public TableAbstract(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public abstract String getColumnsXpath();

    public abstract String getRowsXpath();

    public abstract String getHeadersXpath();

    public String getHeadlinesXpath() {
        throw new UnsupportedOperationException("No path to group headers in table");
    }

    public String getGroupXpathTemplate() {
        throw new UnsupportedOperationException("No path to group in table");
    }

    /**
     * Returns a column of a table by its index
     *
     * @param columnIndex column index
     * @return Returns a column in the form of cells
     */
    public List<WebElement> getColumn(int columnIndex) {
        return findElements(By.xpath(getColumnXpath(columnIndex)));
    }

    /**
     * Returns a column of a table by its header
     *
     * @param columnName column header
     * @return Returns a column in the form of cells
     */
    public List<WebElement> getColumn(String columnName) {
        int columnIndex = getColumnNumberByName(columnName);
        return findElements(By.xpath(getColumnXpath(columnIndex)));
    }

    /**
     * Returns a row of a table by index in the form of cells.
     *
     * @param rowIndex row index
     * @return row cells
     */
    public List<WebElement> getRowAsCells(int rowIndex) {
        return findElements(By.xpath(getRowXpath(rowIndex) + "//td"));
    }

    /**
     * Returns a table row by index
     *
     * @param rowIndex row index
     * @return Table Row
     */
    public WebElement getRow(int rowIndex) {
        List<WebElement> rows = findElements(By.xpath(getRowXpath(rowIndex)));
        if (rows.isEmpty()) {
            throw new TableRowNotFoundError(rowIndex);
        }
        return rows.get(0);
    }

    private String getColumnXpath(int index) {
        return getColumnsXpath() + format(XPATH_INDEX_TEMPLATE, index + 1);
    }

    private String getRowXpath(int index) {
        return "(" + getRowsXpath() + ")" + format(XPATH_INDEX_TEMPLATE, index + 1);
    }

    /**
     * Returns table headers
     *
     * @return Table headers
     */
    public List<WebElement> getHeaders() {
        return findElements(By.xpath(getHeadersXpath()));
    }

    /**
     * Returns group headers
     *
     * @return group headers
     */
    public List<WebElement> getHeadlines() {
        return findElements(By.xpath(getHeadlinesXpath()));
    }

    /**
     * Gets the text values ​​of all table headers
     *
     * @return list of table header text values
     */
    public List<String> getHeadersAsString() {
        return findElements(By.xpath(getHeadersXpath())).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Selects a row of a table by its index
     *
     * @param rowIndex row index
     * @param doubleClick {@code true} if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRow(int rowIndex, boolean doubleClick) {
        selectRow(getWrappedElement(), rowIndex, doubleClick);
    }

    /**
     * Selects a row of a table in a group by its index
     *
     * @param groupName group name
     * @param rowIndex row index
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRowInGroup(String groupName, int rowIndex, boolean doubleClick) {
        selectRow(getGroup(groupName), rowIndex, doubleClick);
    }

    private void selectRow(WebElement context, int rowIndex, boolean doubleClick) {
        List<WebElement> rows = context.findElements(By.xpath(getRowXpath(rowIndex)));
        if (rows.isEmpty()) {
            throw new TableRowNotFoundError(rowIndex);
        }
        click(rows.get(0), doubleClick);
    }

    /**
     * Selects the row in the table by the cell text in the column. If several cells with the
     * search text are found, the row with the specified number will be selected. If the cell
     * with the specified text and number is not found, an exception will be thrown
     * <p>
     * If there are several text elements in the cell, the search will be successful
     * if the text is in one of these elements
     *
     * @param columnName column name
     * @param cellText search text
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @param doubleClick {@code true} if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRow(String columnName, String cellText, int serialNumber, boolean doubleClick) {
        int columnIndex = getColumnIndexByName(columnName);
        selectRow(columnIndex, cellText, serialNumber, doubleClick);
    }

    /**
     * Selects a row in a group in a table by text cell in a column. If several cells with the search
     * text are found, the row with the specified number will be selected.
     * If the cell with the specified text and number is not found, an exception will be thrown
     * <p>
     * If there are several text elements in the cell, the search will be successful
     * if the text is in one of these elements
     *
     * @param groupName group name
     * @param columnName column name
     * @param cellText search text
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @param doubleClick {@code true} if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRowInGroup(String groupName, String columnName, String cellText, int serialNumber, boolean doubleClick) {
        int columnIndex = getColumnIndexByName(columnName);
        selectRowInGroup(groupName, columnIndex, cellText, serialNumber, doubleClick);
    }

    /**
     * Selects a row by text in a column with the specified index. If several elements are found with
     * the search text, the element with the specified number will be selected. If an element with the
     * specified text and number is not found, an exception will be thrown
     *
     * @param columnIndex column index
     * @param cellText search text
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false}, if single
     */
    public void selectRow(int columnIndex, String cellText, int serialNumber, boolean doubleClick) {
        selectRow(getWrappedElement(), columnIndex, cellText, serialNumber, doubleClick);
    }

    /**
     * Selects a row in a group by text in a column with the specified index. If several elements are found with
     * the search text, the element with the specified number will be selected. If an element with the
     * specified text and number is not found, an exception will be thrown
     *
     * @param groupName group name
     * @param columnIndex column index
     * @param cellText search text
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false}, if single
     */
    public void selectRowInGroup(String groupName, int columnIndex, String cellText, int serialNumber, boolean doubleClick) {
        selectRow(getGroup(groupName), columnIndex, cellText, serialNumber, doubleClick);
    }

    private void selectRow(WebElement context, int columnIndex, String cellText, int serialNumber, boolean doubleClick) {
        List<WebElement> cells = context.findElements(By.xpath(getColumnXpath(columnIndex) + format(TEXT_XPATH, cellText)));
        try {
            click(ElementUtils.getElementByIndex(cells, serialNumber - 1), doubleClick);
        } catch (ElementNotFoundException ex) {
            throw new TableNotContainTextError(cellText, columnIndex);
        }
    }

    /**
     * Selects a row by text in any column. If several elements are found with the search text,
     * the element with the specified number will be selected. If an element with the specified
     * text and number is not found, an exception will be thrown
     *
     * @param cellText search text
     * @param serialNumber element number
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false}, if single
     */
    public void selectRow(String cellText, int serialNumber, boolean doubleClick) {
        selectRow(cellText, SearchStrategy.EQUALS, serialNumber, doubleClick);
    }

    /**
     * Selects a row in a group by text in any column
     * <p>
     * If several elements are found with the search text, the element with the specified number will be selected.
     * If an element with the specified text and number is not found, an exception will be thrown
     *
     * @param groupName group name
     * @param cellText search text
     * @param serialNumber element number
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRowInGroup(String groupName, String cellText, int serialNumber, boolean doubleClick) {
        selectRowInGroup(groupName, cellText, SearchStrategy.EQUALS, serialNumber, doubleClick);
    }

    /**
     * Selects the line that matches the expected line, regardless of the order of the elements
     *
     * @param cellsText unordered list of line elements
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRow(List<String> cellsText, boolean doubleClick) {
        selectRow(getWrappedElement(), cellsText, doubleClick);
    }

    /**
     * Selects the row in the group that matches the expected row, regardless of the order of the elements
     *
     * @param groupName group name
     * @param cellsText unordered list of line elements
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRowInGroup(String groupName, List<String> cellsText, boolean doubleClick) {
        selectRow(getGroup(groupName), cellsText, doubleClick);
    }

    private void selectRow(WebElement context, List<String> cellsText, boolean doubleClick) {
        List<WebElement> rows = findRows(context, cellsText);

        if (rows.isEmpty()) {
            throw new TableRowNotFoundError(cellsText);
        }
        if (rows.size() > 1) {
            LOG.warn("The table contains 2 or more matches per line");
        }
        click(rows.get(0), doubleClick);
    }

    /**
     * Selects a string with text depending on the search strategy:
     * {@code SearchStrategy.EQUALS} - by exact match
     * {@code SearchStrategy.CONTAINS} - by partial match
     * <p>
     * If several elements are found with the search text, the element with the specified number will be selected
     *
     * @param cellText search text
     * @param searchStrategy search strategy
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRow(String cellText, SearchStrategy searchStrategy, int serialNumber, boolean doubleClick) {
        selectRow(getWrappedElement(), cellText, searchStrategy, serialNumber, doubleClick);
    }

    /**
     * Selects the line in the group with the text depending on the search strategy:
     * {@code SearchStrategy.EQUALS} - by exact match
     * {@code SearchStrategy.CONTAINS} - by partial match
     * <p>
     * If several elements are found with the search text, the element with the specified number will be selected
     *
     * @param cellText search text
     * @param searchStrategy search strategy
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRowInGroup(String groupName, String cellText, SearchStrategy searchStrategy, int serialNumber, boolean doubleClick) {
        selectRow(getGroup(groupName), cellText, searchStrategy, serialNumber, doubleClick);
    }

    private void selectRow(WebElement context, String cellText, SearchStrategy searchStrategy, int serialNumber, boolean doubleClick) {
        String searchFragment = getSearchFragment(cellText, searchStrategy);
        List<WebElement> cells = context.findElements(By.xpath(getColumnsXpath() + searchFragment));

        try {
            WebElement cell = ElementUtils.getElementByIndex(cells, serialNumber - 1);
            if (isSimpleCell(cell)) {
                LOG.warn("Cell text is complex. A match was found among one of the cell elements");
            }
            click(cell, doubleClick);
        } catch (ElementNotFoundException ex) {
            throw new TableNotContainTextError(cellText);
        }
    }

    /**
     * Selects a row in a table by element value in a column. Checks all the fields with
     * the specified element name in order, until the search text is found in one of them.
     * If several elements are found with the search text, the element with the specified number will be selected.
     * If an element with the specified text and number is not found, an exception will be thrown.
     *
     * @param columnName column name
     * @param elementName element name
     * @param elementValue search text
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @param doubleClick {@code true}, if you need to make a double-click selection,
     * and {@code false} if single
     */
    public void selectRowByElement(String columnName, String elementName, String elementValue, int serialNumber, boolean doubleClick) {
        int columnIndex = getColumnIndexByName(columnName);
        HtmlFindUtils findUtils = Environment.getFindUtils();
        try {
            List<BaseElement> cells = findUtils.findList(this, columnName);
            if (!cells.isEmpty()) {

                Class<?> cellType = cells.get(0).getClass();
                List<Field> fields = FieldUtils.getFieldsListWithAnnotation(cellType, ElementTitle.class);

                String elementXpath = fields.stream()
                        .filter(field -> field.getAnnotation(ElementTitle.class).value().equals(elementName))
                        .findFirst()
                        .orElseThrow(() -> new AutotestError("No cell element with name found: " + elementName))
                        .getAnnotation(FindBy.class).xpath();

                // If the element's xpath is relative or with an index, then you need
                // to glue it under special conditions, excluding these symbols
                if (elementXpath.startsWith(".") || elementXpath.startsWith("(")) {
                    elementXpath = elementXpath.replaceFirst("\\.?\\(?\\.?", "")
                            .replaceFirst("\\) ?\\[", "[");
                }

                String elementWithTextXpath = getColumnXpath(columnIndex)
                        + elementXpath + format(TEXT_PART_XPATH_TEMPLATE, elementValue);
                List<WebElement> elements = this.findElements(By.xpath(elementWithTextXpath));

                click(ElementUtils.getElementByIndex(elements, serialNumber - 1), doubleClick);
            } else {
                throw new AutotestError("No column in the table was found: " + columnName);
            }
        } catch (ElementDescriptionException ex) {
            throw new AutotestError("Error finding element in table");
        } catch (ElementNotFoundException e) {
            throw new AutotestError(format("No cell element found with value: %s. Number: %s", elementValue, serialNumber));
        }
    }

    /**
     * Checks for text in a column
     *
     * @param columnName column name
     * @param cellText text
     * @param strategy search strategy.
     * {@code SearchStrategy.EQUALS} - by strict compliance,
     * {@code SearchStrategy.CONTAINS} - by partial
     * @return Returns {@code true} if the text was found in the column and
     * {@code false} if not
     */
    public boolean hasText(String columnName, String cellText, SearchStrategy strategy) {
        return hasText(getWrappedElement(), columnName, cellText, strategy);
    }

    /**
     * Checks for text in a column in a group
     *
     * @param columnName column name
     * @param cellText text
     * @param strategy search strategy.
     * {@code SearchStrategy.EQUALS} - by strict compliance,
     * {@code SearchStrategy.CONTAINS} - by partial
     * @return Returns {@code true} if the text was found in the column and
     * {@code false} if not
     */
    public boolean hasTextInGroup(String groupName, String columnName, String cellText, SearchStrategy strategy) {
        return hasText(getGroup(groupName), columnName, cellText, strategy);
    }

    private boolean hasText(WebElement context, String columnName, String cellText, SearchStrategy strategy) {
        String cellsXpath = getColumnXpath(getColumnIndexByName(columnName)) + getSearchFragment(cellText, strategy);
        List<WebElement> cells = context.findElements(By.xpath(cellsXpath));
        return !cells.isEmpty();
    }

    /**
     * Checks for text in a table
     *
     * @param cellText text
     * @param strategy search strategy.
     * {@code SearchStrategy.EQUALS} - by strict compliance,
     * {@code SearchStrategy.CONTAINS} - by partial
     * @return Returns {@code true} if the text was found and {@code false} if not
     */
    public boolean hasText(String cellText, SearchStrategy strategy) {
        return hasText(getWrappedElement(), cellText, strategy);
    }

    /**
     * Checks for the presence of text in a table in a group
     *
     * @param groupName group name
     * @param cellText text
     * @param strategy search strategy.
     * {@code SearchStrategy.EQUALS} - by strict compliance,
     * {@code SearchStrategy.CONTAINS} - by partial
     * @return Returns {@code true} if the text was found and {@code false} if not
     */
    public boolean hasTextInGroup(String groupName, String cellText, SearchStrategy strategy) {
        return hasText(getGroup(groupName), cellText, strategy);
    }

    private boolean hasText(WebElement context, String cellText, SearchStrategy strategy) {
        String cellsXpath = getColumnsXpath() + getSearchFragment(cellText, strategy);
        List<WebElement> cells = context.findElements(By.xpath(cellsXpath));
        return !cells.isEmpty();
    }

    /**
     * Finds a row in which the given value contains the passed value and checks for it
     * the correspondence of the values in the columns
     *
     * @param columnName column name
     * @param cellText cell text
     * @param dataMap set for checking values in the form of "column name - text value". For example:
     * | Column 1 | cell text |
     * | Column 2 | cell text |
     */
    public void checkRow(String columnName, String cellText, Map<String, String> dataMap) {
        checkRow(getWrappedElement(), columnName, cellText, dataMap);
    }

    /**
     * Finds in the group a row that contains the passed value in the specified column and checks for it
     * the correspondence of the values in the columns
     *
     * @param groupName group name
     * @param columnName column name
     * @param cellText cell text
     * @param dataMap set for checking values in the form of "column name - text value". For example:
     * | Column 1 | cell text |
     * | Column 2 | cell text |
     */
    public void checkRowInGroup(String groupName, String columnName, String cellText, Map<String, String> dataMap) {
        checkRow(getGroup(groupName), columnName, cellText, dataMap);
    }

    private void checkRow(WebElement context, String columnName, String cellText, Map<String, String> dataMap) {
        int columnNumber = getColumnIndexByName(columnName);
        WebElement row = getRowAsCells(context, columnNumber, cellText);
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            columnNumber = getColumnNumberByName(entry.getKey());
            checkCell(row, columnNumber, entry.getValue());
        }
    }

    /**
     * Finds a row that contains the passed value in the specified column and checks for it
     * the correspondence of the values in the columns
     *
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param cellText cell text
     * @param dataMap set for checking values in the form of "column name - text value". For example:
     * | 1 | cell text |
     * | 2 | cell text |
     */
    public void checkRow(int columnNumber, String cellText, Map<Integer, String> dataMap) {
        checkRow(getWrappedElement(), columnNumber, cellText, dataMap);
    }

    /**
     * Finds in the group a row that contains the passed value in the specified column and checks for it
     * the correspondence of the values in the columns
     *
     * @param groupName group name
     * @param columnNumber column ordinal (1, 2, 3...)
     * @param cellText cell text
     * @param dataMap set for checking values in the form of "column name - text value". For example:
     * | 1 | cell text |
     * | 2 | cell text |
     * | 2 | cell text |
     */
    public void checkRowInGroup(String groupName, int columnNumber, String cellText, Map<Integer, String> dataMap) {
        checkRow(getGroup(groupName), columnNumber, cellText, dataMap);
    }

    private void checkRow(WebElement context, int columnNumber, String cellText, Map<Integer, String> dataMap) {
        WebElement row = getRowAsCells(context, columnNumber - 1, cellText);
        for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
            checkCell(row, entry.getKey(), entry.getValue());
        }
    }

    private void checkCell(WebElement row, int columnNumber, String expectedCellText) {
        List<WebElement> elements = row.findElements(By.xpath(format(COLUMN_RELATIVE + TEXT_XPATH, columnNumber, expectedCellText)));
        if (elements.isEmpty()) {
            elements = row.findElements(By.xpath(format(COLUMN_RELATIVE, columnNumber)));
            if (elements.isEmpty()) {
                throw new AutotestError("Could not find column number " + columnNumber);
            }
            throw new AutotestError(format("The value of the column with the number '%s' does not match " +
                    "expected. Expected: '%s'. Actual: '%s'", columnNumber, expectedCellText, elements.get(0).getText()));
        }
    }

    private WebElement getRowAsCells(WebElement context, int columnIndex, String cellText) {
        String searchFragment = getSearchFragment(cellText, SearchStrategy.EQUALS);
        List<WebElement> cells = context.findElements(By.xpath(getColumnXpath(columnIndex) + searchFragment));

        try {
            WebElement cell = ElementUtils.getElementByIndex(cells, 0);
            if (isSimpleCell(cell)) {
                LOG.warn("Cell text is complex. A match was found among one of the cell elements");
            }
            return cell;
        } catch (ElementNotFoundException ex) {
            throw new TableNotContainTextError(cellText);
        }
    }

    private String getSearchFragment(String text, SearchStrategy strategy) {
        return format(strategy.equals(SearchStrategy.EQUALS)
                ? TEXT_XPATH : CONTAINS_TEXT_XPATH_TEMPLATE, text);
    }

    /**
     * Saves in Stash the row number in which the specified column contains the passed value
     *
     * @param columnIndex column index
     * @param cellText cell text
     * @param stashKey Stash key
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     */
    public void saveRowNumber(int columnIndex, String cellText, String stashKey, int serialNumber) {
        Stash.put(stashKey, String.valueOf(getRowNumber(columnIndex, cellText, serialNumber)));
    }


    /**
     * Returns the ordinal number of the row (1, 2, 3 ...) by the text value in the column
     *
     * @param columnIndex column index
     * @param cellText cell text
     * @return is the ordinal number of the line (1, 2, 3 ...)
     */
    public int getRowNumber(int columnIndex, String cellText) {
        return getRowNumber(columnIndex, cellText, 1);
    }

    /**
     * Returns the ordinal number of the row (1, 2, 3 ...) by the text value in the column
     *
     * @param columnIndex column index
     * @param cellText cell text
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return is the ordinal number of the line (1, 2, 3 ...)
     */
    public int getRowNumber(int columnIndex, String cellText, int serialNumber) {
        return getRowIndex(columnIndex, cellText, serialNumber) + 1;
    }

    /**
     * Returns the index of a row by text in a column
     *
     * @param columnIndex column index
     * @param cellText cell text
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     * @return line index
     */
    public int getRowIndex(int columnIndex, String cellText, int serialNumber) {
        List<String> column = ElementUtils.getElementsText(getColumn(columnIndex));
        int foundValueCounter = 1;
        boolean isFound = false;

        int rowIndex = 0;
        for (; rowIndex < column.size() && !isFound; rowIndex++) {
            if (column.get(rowIndex).equals(cellText)) {
                if (foundValueCounter == serialNumber) {
                    isFound = true;
                } else {
                    foundValueCounter++;
                }
            }
        }
        if (!isFound) {
            throw new TableRowNotFoundError(columnIndex, cellText, serialNumber);
        }
        return rowIndex - 1;
    }

    /**
     * Saves in Stash the row number in which the specified column contains the passed value
     *
     * @param columnName column name
     * @param cellText cell text
     * @param stashKey Stash key
     * @param serialNumber sequence number for selection of matches found (1, 2, 3 ...)
     */
    public void saveRowNumber(String columnName, String cellText, String stashKey, int serialNumber) {
        saveRowNumber(getColumnIndexByName(columnName), cellText, stashKey, serialNumber);
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified text value
     * and presses the button with the specified name in this line in the column with the index "columnWithButtonIndex"
     *
     * @param columnIndex column index by which textual search will be performed
     * @param cellText search text
     * @param buttonName button name
     * @param columnWithButtonIndex column index in which the button is located
     */
    public void clickButton(int columnIndex, String cellText, String buttonName, int columnWithButtonIndex) {
        clickButton(format(BUTTON_XPATH + TEXT_PART_XPATH_TEMPLATE, buttonName), getRowIndex(columnIndex, cellText, 1), columnWithButtonIndex);
    }

    /**
     * Press the button with the specified name in the specified cell
     *
     * @param columnIndex column index
     * @param rowIndex row index
     * @param buttonName button name
     */
    public void clickButton(int columnIndex, int rowIndex, String buttonName) {
        clickButton(format(BUTTON_XPATH + TEXT_PART_XPATH_TEMPLATE, buttonName), rowIndex, columnIndex);
    }

    /**
     * Presses a button in a given cell
     *
     * @param columnIndex column index
     * @param rowIndex row index
     */
    public void clickButton(int columnIndex, int rowIndex) {
        clickButton(BUTTON_XPATH, rowIndex, columnIndex);
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified
     * text value and presses the button in this line in the column with the index "columnWithButtonIndex"
     *
     * @param columnIndex column index by which textual search will be performed
     * @param cellText search text
     * @param columnWithButtonIndex column index in which the button is located
     */
    public void clickButton(int columnIndex, String cellText, int columnWithButtonIndex) {
        clickButton(BUTTON_XPATH, getRowIndex(columnIndex, cellText, 1), columnWithButtonIndex);
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified text value and presses
     * the button with the specified name in this line in the column with the name "columnWithButtonName"
     *
     * @param columnName is the name of the column that will be searched for text value.
     * @param cellText search text
     * @param buttonName button name
     * @param columnWithButtonName name of the column in which the button is located
     */
    public void clickButton(String columnName, String cellText, String buttonName, String columnWithButtonName) {
        int rowNumber = getRowIndex(getColumnIndexByName(columnName), cellText, 1);
        int columnWithButtonIndex = getColumnIndexByName(columnWithButtonName);
        clickButton(format(BUTTON_XPATH + TEXT_PART_XPATH_TEMPLATE, buttonName), rowNumber, columnWithButtonIndex);
    }

    /**
     * Press the button with the specified name in the specified cell
     *
     * @param columnName column name
     * @param rowIndex row index
     * @param buttonName button name
     */
    public void clickButton(String columnName, int rowIndex, String buttonName) {
        int columnIndex = getColumnIndexByName(columnName);
        clickButton(format(BUTTON_XPATH + TEXT_PART_XPATH_TEMPLATE, buttonName), rowIndex, columnIndex);
    }

    /**
     * Presses a button in a given cell
     *
     * @param columnName column name
     * @param rowIndex row index
     */
    public void clickButton(String columnName, int rowIndex) {
        int columnIndex = getColumnIndexByName(columnName);
        clickButton(BUTTON_XPATH, rowIndex, columnIndex);
    }

    /**
     * Finds in the column with the index "columnIndex" a line with the specified text value and
     * presses the button in this line in the column with the name "columnWithButtonName"
     *
     * @param columnName is the name of the column that will be searched for text value.
     * @param cellText search text
     * @param columnWithButtonName name of the column in which the button is located
     */
    public void clickButton(String columnName, String cellText, String columnWithButtonName) {
        int rowNumber = getRowIndex(getColumnIndexByName(columnName), cellText, 1);
        int columnWithButtonIndex = getColumnIndexByName(columnWithButtonName);
        clickButton(BUTTON_XPATH, rowNumber, columnWithButtonIndex);
    }

    /**
     * Gets the ordinal number of a column by its name
     *
     * @param columnName column name
     * @return is the ordinal number of the column from the found matches (1, 2, 3 ...)
     */
    public int getColumnNumberByName(String columnName) {
        return getColumnIndexByName(columnName) + 1;
    }


    /**
     * Get column index by name
     *
     * @param columnName column name
     * @return column index
     */
    public int getColumnIndexByName(String columnName) {
        List<String> headers = findElements(By.xpath(getHeadersXpath())).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        int index = headers.indexOf(columnName);

        if (index < 0) {
            throw new AutotestError("No header found in table: " + columnName);
        }
        return index;
    }

    protected void clickButton(String buttonXpath, int rowIndex, int columnIndex) {
        List<WebElement> buttons = getRowAsCells(rowIndex).get(columnIndex)
                .findElements(By.xpath(buttonXpath));

        if (buttons.isEmpty()) {
            throw new ButtonNotFoundError();
        }
        buttons.get(0).click();
    }

    protected void click(WebElement cell, boolean isDouble) {
        if (isDouble) {
            ElementUtils.doubleClick(cell);
        } else {
            cell.click();
        }
    }

    protected boolean isSimpleCell(WebElement cell) {
        return !(cell.findElements(By.xpath(COMPOSITE_CELL_TEXT)).size() > 1);
    }

    protected List<WebElement> findRows(WebElement context, List<String> cellsText) {
        StringBuilder searchFragment = new StringBuilder(getRowsXpath() + "[");
        String cellPathFormat = ".//*[text()='%s']";

        for (String cellText : cellsText) {
            searchFragment.append(format(cellPathFormat, cellText));
            searchFragment.append(" and ");
        }
        searchFragment.append("]");
        String rowPath = searchFragment.toString().replace(" and ]", "]");
        return context.findElements(By.xpath(rowPath));
    }

    protected WebElement getGroup(String groupName) {
        List<WebElement> groups = findElements(By.xpath(format(getGroupXpathTemplate(), groupName)));
        if (groups.isEmpty()) {
            throw new AutotestError("No group found with name: " + groupName);
        }
        return groups.get(0);
    }
}
