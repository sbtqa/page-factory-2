package ru.sbtqa.tag.mqfactory.error;

public class MqError extends Error {

    /**
     * Constructor for MqError.
     *
     * @param message a {@link java.lang.String} object.
     * @param e       a {@link java.lang.Throwable} object.
     */
    public MqError(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor for MqError.
     *
     * @param message a {@link java.lang.String} object.
     */
    public MqError(String message) {
        super(message);
    }

}
