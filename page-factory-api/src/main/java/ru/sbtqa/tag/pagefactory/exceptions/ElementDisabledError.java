package ru.sbtqa.tag.pagefactory.exceptions;

public class ElementDisabledError extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public ElementDisabledError(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public ElementDisabledError(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public ElementDisabledError(String message) {
        super(message);
    }
}
