package ru.sbtqa.tag.pagefactory.exception;

public class ElementSearchError extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public ElementSearchError(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public ElementSearchError(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public ElementSearchError(String message) {
        super(message);
    }
}
