package ru.sbtqa.tag.mqfactory.interfaces;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;


public interface Kafka<T> extends Mq<T> {

    /**
     * Send request to KafkaImpl topic with key value
     *
     * @param requestMsg message for sending
     * @param key        key value
     * @param topicName  topic name
     */
    void sendRequestTo(String requestMsg, String key, String topicName);

    /**
     * Get appropriate number of the last messages from KafkaImpl topic in
     * corresponding partition label
     *
     * @param topicName        topic name
     * @param partition        partition label
     * @param numberOfMessages number of message from the end of partition
     * @return appropriate number of messages from the end of partition as List
     * of Consumer Records
     */
    List<ConsumerRecord<String, String>> getLastMessagesInPartition(String topicName, int partition, int numberOfMessages);

    /**
     * Browse all messages in KafkaImpl topic at the specified partition
     *
     * @param topicName topic name
     * @param partiton  partition label
     * @return List of ConsumerRecords
     */
    List<ConsumerRecord<String, String>> browseAllMessagesFromPartition(String topicName, int partiton);

}
