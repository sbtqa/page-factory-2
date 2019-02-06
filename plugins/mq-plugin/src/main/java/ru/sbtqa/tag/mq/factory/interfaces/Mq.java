package ru.sbtqa.tag.mq.factory.interfaces;

import ru.sbtqa.tag.mq.factory.exception.MqException;


public interface Mq<T> {

    /**
     * Send request to queue
     *
     * @param queueName  Queue name
     * @param requestMsg message for sending
     * @return message ID
     * @throws MqException if can't create session
     */
    String sendRequest(String queueName, String requestMsg) throws MqException;

    /**
     * Get answer from queue
     *
     * @param queueName queue name for connect
     * @return answer from queue as Object
     * @throws MqException if can't receive message or if can't create consumer for message
     */
    T getAnswer(String queueName) throws MqException;

    /**
     * Get text body from received Message or Record
     *
     * @param message message
     * @return text from message as String
     * @throws MqException if can't get text from message
     */
    String getMessageText(T message) throws MqException;

    /**
     * Get message property
     *
     * @param message  message for sending
     * @param property property for return
     * @return property of the message as Object
     * @throws MqException if can't get message property
     */
    String getMessageProperty(T message, String property) throws MqException;


}
