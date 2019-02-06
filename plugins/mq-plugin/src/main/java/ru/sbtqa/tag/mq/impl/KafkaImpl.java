package ru.sbtqa.tag.mq.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.mq.factory.MqConfiguration;
import ru.sbtqa.tag.mq.factory.enumeration.MessagePropertyType;
import ru.sbtqa.tag.mq.factory.exception.KafkaException;
import ru.sbtqa.tag.mq.factory.interfaces.Kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaImpl implements Kafka<ConsumerRecord> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaImpl.class);
    private static final MqConfiguration PROPS = MqConfiguration.create();

    private Properties producerProperties;
    private Properties consumerProperties;

    public KafkaImpl(Properties producerProperties, Properties consumerProperties) {
        this.producerProperties = producerProperties;
        this.consumerProperties = consumerProperties;
    }

    @Override
    public String sendRequest(String topicName, String requestMsg) throws KafkaException {
        Producer<String, String> producer = new KafkaProducer<>(producerProperties);
        producer.send(new ProducerRecord<>(topicName, requestMsg));
        producer.close();
        return "ID return did not implemented";
    }

    @Override
    public void sendRequestTo(String requestMsg, String key, String topicName) {
        Producer<String, String> producer = new KafkaProducer<>(producerProperties);
        producer.send(new ProducerRecord<>(topicName, key, requestMsg));
        producer.close();
    }

    @Override
    public ConsumerRecord<String, String> getAnswer(String queueName) throws KafkaException {
        return getLastMessagesInPartition(queueName, 0, 0).iterator().next();
    }

    @Override
    public List<ConsumerRecord<String, String>> getLastMessagesInPartition(String topicName, int partition, int numberOfMessages) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        TopicPartition tp = new TopicPartition(topicName, partition);
        consumer.assign(Collections.singletonList(tp));
        consumer.seek(tp, consumer.position(tp) - numberOfMessages);
        ConsumerRecords<String, String> records = consumer.poll(PROPS.getKafkaTimeout());
        for (ConsumerRecord<String, String> rec : records) {
            buffer.add(rec);
        }
        consumer.close();
        return buffer;
    }

    @Override
    public List<ConsumerRecord<String, String>> browseAllMessagesFromPartition(String topicName, int partiton) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        TopicPartition tp = new TopicPartition(topicName, partiton);
        List<TopicPartition> partitions = Collections.singletonList(tp);
        consumer.assign(partitions);
        consumer.seekToBeginning(partitions);
        ConsumerRecords<String, String> records = consumer.poll(PROPS.getKafkaTimeout());
        for (ConsumerRecord<String, String> rec : records) {
            buffer.add(rec);
        }
        consumer.close();
        return buffer;
    }

    @Override
    public String getMessageText(ConsumerRecord message) throws KafkaException {
        return message.value().toString();
    }

    @Override
    public String getMessageProperty(ConsumerRecord message, String property) throws KafkaException {
        try {
            MessagePropertyType type = MessagePropertyType.get(property);
            switch (type) {
                case TOPIC:
                    return message.topic();
                case PARTITION:
                    return String.valueOf(message.partition());
                case OFFSET:
                    return String.valueOf(message.offset());
                case KEY:
                    return message.key().toString();
                default:
                    throw new UnsupportedOperationException("Did not implemented property type: " + type);
            }
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return "null";
    }

}
