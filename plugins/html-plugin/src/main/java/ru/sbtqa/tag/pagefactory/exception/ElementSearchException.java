package ru.sbtqa.tag.pagefactory.exception;

public class ElementSearchException extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public ElementSearchException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public ElementSearchException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public ElementSearchException(String message) {
        super(message);
    }
}
