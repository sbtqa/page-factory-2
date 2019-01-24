package ru.sbtqa.tag.mqfactory.exception;

public class JmsException extends MqException {

    /**
     * Constructor for JmsException.
     *
     * @param message a {@link java.lang.String} object.
     * @param e       a {@link java.lang.Throwable} object.
     */
    public JmsException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor for WebsphereException.
     *
     * @param message a {@link java.lang.String} object.
     */
    public JmsException(String message) {
        super(message);
    }

}
