package ru.sbtqa.tag.pagefactory.exception;

public class IncorrectElementTypeError extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public IncorrectElementTypeError(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public IncorrectElementTypeError(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public IncorrectElementTypeError(String message) {
        super(message);
    }
}
