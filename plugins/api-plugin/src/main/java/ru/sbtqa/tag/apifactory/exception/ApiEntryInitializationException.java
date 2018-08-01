package ru.sbtqa.tag.apifactory.exception;

/**
 *
 *
 */
public class ApiEntryInitializationException extends ApiException {

    /**
     * Constructor for ApiEntryInitializationException.
     *
     * @param message a {@link java.lang.String} object.
     * @param e a {@link java.lang.Throwable} object.
     */
    public ApiEntryInitializationException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor for ApiEntryInitializationException.
     *
     * @param message a {@link java.lang.String} object.
     */
    public ApiEntryInitializationException(String message) {
        super(message);
    }

}
