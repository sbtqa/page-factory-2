package ru.sbtqa.tag.pagefactory.exceptions;

public class WaitException extends AssertionError {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public WaitException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public WaitException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public WaitException(String message) {
        super(message);
    }

}
