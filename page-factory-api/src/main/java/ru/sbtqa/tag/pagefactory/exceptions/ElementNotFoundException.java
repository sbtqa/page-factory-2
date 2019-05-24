package ru.sbtqa.tag.pagefactory.exceptions;

public class ElementNotFoundException extends RuntimeException {

    /**
     * 
     * @param e a {@link Throwable} object.
     */
    public ElementNotFoundException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public ElementNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public ElementNotFoundException(String message) {
        super(message);
    }

}
