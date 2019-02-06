package ru.sbtqa.tag.mq.factory.exception;

public class MqException extends Exception {

    /**
     * Constructor for MqException.
     *
     * @param message a {@link java.lang.String} object.
     * @param e       a {@link java.lang.Throwable} object.
     */
    public MqException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor for MqException.
     *
     * @param message a {@link java.lang.String} object.
     */
    public MqException(String message) {
        super(message);
    }

}
