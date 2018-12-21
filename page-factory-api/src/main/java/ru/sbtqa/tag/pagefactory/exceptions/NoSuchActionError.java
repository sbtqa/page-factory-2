package ru.sbtqa.tag.pagefactory.exceptions;

public class NoSuchActionError extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public NoSuchActionError(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public NoSuchActionError(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public NoSuchActionError(String message) {
        super(message);
    }
}
