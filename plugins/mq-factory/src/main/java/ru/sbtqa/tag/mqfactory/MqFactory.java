package ru.sbtqa.tag.mqfactory;

import ru.sbtqa.tag.mqfactory.error.MqError;
import ru.sbtqa.tag.mqfactory.interfaces.Jms;
import ru.sbtqa.tag.mqfactory.interfaces.Kafka;
import ru.sbtqa.tag.mqfactory.interfaces.Mq;
import ru.sbtqa.tag.mqimpl.JmsTextMessageImpl;
import ru.sbtqa.tag.mqimpl.KafkaImpl;

import javax.jms.Connection;
import java.util.Properties;

/**
 * @author sbt-polosov-va
 */
public class MqFactory {

    public static final String MQ_TYPE = "mq.type";
    public static final String JMS_CONNECTION = "jms.connection";
    public static final String PRODUCER_PROPERTIES = "producer.properties";
    public static final String CONSUMER_PROPERTIES = "consumer.properties";

    private MqFactory() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Get Mq instance for
     *
     * @param connProps connection properties must have - connection.type and
     *                  jms.connection for jms instances - producer.properties and
     *                  consumer.properties for kafka instances
     *                  <p>
     *                  Connection should be implement and return as javax.jms.Connection and
     *                  include all necessary connection properties in it. The number of
     *                  parameters you define by yourself.
     *                  <p>
     *                  Connection types that supports now is: - websphere - activeMq - kafka
     * @return mq instance as Mq
     */
    public static <T extends Mq> T getMq(Properties connProps) {
        T mqRealisation;
        String connectionType;
        if (!connProps.containsKey(MQ_TYPE)) {
            throw new MqError("Mq type didn't set in property " + MQ_TYPE);
        }
        connectionType = (String) connProps.get(MQ_TYPE);

        switch (connectionType) {
            case "websphere":
            case "activeMq":
                mqRealisation = (T) jmsConnection(connProps);
                break;
            case "kafka":
                mqRealisation = (T) kafkaConnection(connProps);
                break;
            default:
                throw new UnsupportedOperationException("Request method " + connectionType + " is not support");
        }
        return mqRealisation;
    }

    private static Jms jmsConnection(Properties connProps) {
        if (!connProps.containsKey(JMS_CONNECTION)) {
            throw new MqError("Connection didn't set in property " + JMS_CONNECTION);
        }
        return new JmsTextMessageImpl((Connection) connProps.get(JMS_CONNECTION));
    }

    private static Kafka kafkaConnection(Properties connProps) {
        if (!connProps.containsKey(PRODUCER_PROPERTIES)) {
            throw new MqError("Producer properties didn't set in property " + PRODUCER_PROPERTIES);
        }
        if (!connProps.containsKey(CONSUMER_PROPERTIES)) {
            throw new MqError("Consumer properties didn't set in property " + CONSUMER_PROPERTIES);
        }
        return new KafkaImpl((Properties) connProps.get(PRODUCER_PROPERTIES), (Properties) connProps.get(CONSUMER_PROPERTIES));
    }
}
