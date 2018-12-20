package ru.sbtqa.tag.pagefactory.exception;

public class IncorrectElementTypeException extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public IncorrectElementTypeException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public IncorrectElementTypeException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public IncorrectElementTypeException(String message) {
        super(message);
    }
}
