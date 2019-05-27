package ru.sbtqa.tag.pagefactory.exception;

import static java.lang.String.format;

public class TableNotContainTextError extends AssertionError {

    private static final String ERROR_MESSAGE = "The table does not contain text \"%s\"";

    public TableNotContainTextError(String cellText) {
        super(format(ERROR_MESSAGE, cellText));
    }

    public TableNotContainTextError(String cellText, int columnIndex) {
        super(format(ERROR_MESSAGE, cellText) + "in a column with index: " + columnIndex);
    }
}
