package ru.sbtqa.tag.pagefactory.exceptions;

public class SwipeException extends PageException {

    /**
     * 
     * @param e a {@link Throwable} object.
     */
    public SwipeException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public SwipeException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public SwipeException(String message) {
        super(message);
    }

}
