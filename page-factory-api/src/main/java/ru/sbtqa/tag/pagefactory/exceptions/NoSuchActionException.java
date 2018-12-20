package ru.sbtqa.tag.pagefactory.exceptions;

public class NoSuchActionException extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public NoSuchActionException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public NoSuchActionException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public NoSuchActionException(String message) {
        super(message);
    }
}
