package ru.sbtqa.tag.mq.factory.exception;

public class KafkaException extends MqException {

    /**
     * Constructor for KafkaException.
     *
     * @param message a {@link java.lang.String} object.
     * @param e       a {@link java.lang.Throwable} object.
     */
    public KafkaException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Constructor for KafkaException.
     *
     * @param message a {@link java.lang.String} object.
     */
    public KafkaException(String message) {
        super(message);
    }

}
