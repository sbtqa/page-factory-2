package ru.sbtqa.tag.pagefactory.exception;

public class ButtonNotFoundError extends AssertionError {

    private static final String ERROR_MESSAGE = "Button not found";

    public ButtonNotFoundError() {
        super(ERROR_MESSAGE);
    }

    public ButtonNotFoundError(String buttonName) {
        super(ERROR_MESSAGE + " " + buttonName);
    }
}
