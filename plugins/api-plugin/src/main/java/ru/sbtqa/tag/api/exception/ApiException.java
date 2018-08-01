package ru.sbtqa.tag.api.exception;

/**
 *
 *
 */
public class ApiException extends Exception {

    /**
     * Constructor for ApiException.
     *
     * @param message a {@link java.lang.String} object.
     * @param e a {@link java.lang.Throwable} object.
     */
    public ApiException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor for ApiException.
     *
     * @param message a {@link java.lang.String} object.
     */
    public ApiException(String message) {
        super(message);
    }

}   