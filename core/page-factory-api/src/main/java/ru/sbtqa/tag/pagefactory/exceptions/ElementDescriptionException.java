package ru.sbtqa.tag.pagefactory.exceptions;

public class ElementDescriptionException extends PageException {

    /**
     * 
     * @param e a {@link Throwable} object.
     */
    public ElementDescriptionException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public ElementDescriptionException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public ElementDescriptionException(String message) {
        super(message);
    }

}
