package ru.sbtqa.tag.apifactory.exception;

/**
 *
 *
 */
public class ApiSoapException extends ApiException {

    /**
     * Constructor for ApiSoapException.
     *
     * @param message a {@link java.lang.String} object.
     * @param e a {@link java.lang.Throwable} object.
     */
    public ApiSoapException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor for ApiSoapException.
     *
     * @param message a {@link java.lang.String} object.
     */
    public ApiSoapException(String message) {
        super(message);
    }

}
