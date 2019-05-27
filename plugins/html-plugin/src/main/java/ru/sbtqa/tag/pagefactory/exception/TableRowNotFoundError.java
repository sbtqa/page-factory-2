package ru.sbtqa.tag.pagefactory.exception;

import java.util.List;
import static java.lang.String.format;

public class TableRowNotFoundError extends AssertionError {

    private static final String ERROR_MESSAGE = "No row found in the table.";

    public TableRowNotFoundError() {
        super(ERROR_MESSAGE);
    }

    public TableRowNotFoundError(String rowName) {
        super(ERROR_MESSAGE + " Name: " + rowName);
    }

    public TableRowNotFoundError(int rowIndex) {
        super(ERROR_MESSAGE + " Index: " + rowIndex);
    }

    public TableRowNotFoundError(List<String> rowValues) {
        super(ERROR_MESSAGE + " Row values: " + String.join(", ", rowValues));
    }

    public TableRowNotFoundError(int columnIndex, String cellText) {
        super(format(ERROR_MESSAGE + " Column index: %s. Cell text: %s", columnIndex, cellText));
    }

    public TableRowNotFoundError(int columnIndex, String cellText, int serialNumber){
        super(format(ERROR_MESSAGE + " Column index: %s. Cell text: %s. Serial number: %s.", columnIndex, cellText, serialNumber));
    }

    public TableRowNotFoundError(Throwable e) {
        super(ERROR_MESSAGE, e);
    }

    public TableRowNotFoundError(String rowName, Throwable e) {
        super(ERROR_MESSAGE + " Name: " + rowName, e);
    }

    public TableRowNotFoundError(int rowIndex, Throwable e) {
        super(ERROR_MESSAGE + " Index: " + rowIndex, e);
    }
}
