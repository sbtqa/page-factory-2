package ru.sbtqa.tag.pagefactory.exceptions;

public class UnsupportedBrowserException extends Exception {

    /**
     *
     * @param e a {@link Throwable} object.
     */
    public UnsupportedBrowserException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public UnsupportedBrowserException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public UnsupportedBrowserException(String message) {
        super(message);
    }

}
