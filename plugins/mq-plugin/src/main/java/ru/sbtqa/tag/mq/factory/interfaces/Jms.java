package ru.sbtqa.tag.mq.factory.interfaces;

import ru.sbtqa.tag.mq.factory.exception.MqException;

import java.util.List;


public interface Jms<T> extends Mq<T> {

    /**
     * Send request to queue
     *
     * @param queueName      Queue name
     * @param prepareMessage message for sending
     * @return message ID
     * @throws MqException if can't create session
     */
    String sendRequest(String queueName, JmsUpdater<T> prepareMessage) throws MqException;

    /**
     * Send answer to queue
     *
     * @param queueName      Queue name
     * @param prepareMessage message for sending
     * @return message ID
     * @throws MqException if can't create session
     */
    String sendAnswer(String queueName, JmsUpdater<T> prepareMessage) throws MqException;

    /**
     * Get message from queue by parameter and parameter's value
     *
     * @param queueName queue name
     * @param messageId message ID
     * @return message related to parameters
     * @throws MqException if can't get message from queue or can't establish browser session
     */
    T getMessageById(String queueName, String messageId) throws MqException;

    /**
     * Get message from queue by parameter and parameter's value
     *
     * @param queueName  queue name
     * @param paramName  parameter name
     * @param paramValue parameter value
     * @return messages related to parameters
     * @throws MqException if can't get message from queue or can't establish browser session
     */
    List<T> getMessagesByParam(String queueName, String paramName, String paramValue) throws MqException;

    /**
     * Browse all messages from queue
     *
     * @param queueName queue name
     * @return all messages in queue as List of object
     * @throws MqException if can't create browse session
     */
    List<T> browseAllMessages(String queueName) throws MqException;

    /**
     * Find messages by condition
     *
     * @param queueName queue name
     * @param condition condition
     * @return list of messages
     * @throws MqException if can't find messages
     */
    List<T> findMessagesByFilter(String queueName, Condition<T> condition) throws MqException;

    /**
     * Get ID from Message
     *
     * @param message message
     * @return ID message as String
     * @throws MqException if can't get ID message
     */
    String getMessageId(T message) throws MqException;

    void removeAllMessages(String queueName) throws MqException;

    void removeMessage(String queue, String messageId) throws MqException;

}
