package ru.sbtqa.tag.pagefactory.exceptions;

public class PageException extends Exception {

    /**
     * 
     * @param e  TODO
     */
    public PageException(Throwable e) {
        super(e);
    }
    
    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public PageException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public PageException(String message) {
        super(message);
    }

}
