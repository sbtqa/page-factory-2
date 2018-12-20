package ru.sbtqa.tag.pagefactory.exceptions;

public class ElementDisabledException extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public ElementDisabledException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public ElementDisabledException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public ElementDisabledException(String message) {
        super(message);
    }
}
