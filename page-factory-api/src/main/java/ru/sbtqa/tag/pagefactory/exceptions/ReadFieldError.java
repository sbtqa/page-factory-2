package ru.sbtqa.tag.pagefactory.exceptions;

public class ReadFieldError extends AssertionError {

    /**
     * @param e a {@link Throwable} object.
     */
    public ReadFieldError(Throwable e) {
        super(e);
    }

    /**
     * @param message a {@link String} object.
     * @param e       a {@link Throwable} object.
     */
    public ReadFieldError(String message, Throwable e) {
        super(message, e);
    }

    /**
     * @param message a {@link String} object.
     */
    public ReadFieldError(String message) {
        super(message);
    }
}
