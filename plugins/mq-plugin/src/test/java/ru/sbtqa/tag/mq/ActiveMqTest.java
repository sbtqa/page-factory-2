package ru.sbtqa.tag.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import ru.sbtqa.tag.mq.factory.MqFactory;
import ru.sbtqa.tag.mq.factory.exception.JmsException;
import ru.sbtqa.tag.mq.factory.exception.MqException;
import ru.sbtqa.tag.mq.factory.interfaces.Jms;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * To start tests type in command line:
 * mvn -pl plugins/mq-plugin -Dtest=ActiveMqTest test
 */
public class ActiveMqTest {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveMqTest.class);
    public static final String AMQ_BROKER_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "testQueue";
    public static final int MESSAGE_COUNT = 3;

    private StopWatch mStopWatch;
    private QueueConnection mQueueConnection;
    private Jms<TextMessage> mqService;

    @Before
    public void beforeTest() {
        mQueueConnection = null;
        mqService = createConnection();
    }

    @After
    public void afterTest() throws MqException, JMSException {
        mqService.removeAllMessages(QUEUE_NAME);

        if (mQueueConnection != null) {
            mQueueConnection.close();
        }
    }

    @Test
    public void browseMessages() throws MqException {
        mStopWatch = new StopWatch("browseMessages()");
        mStopWatch.start("Sending messages");
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            sendMQ(i, "Correlation_" + i);
        }
        mStopWatch.stop();

        mStopWatch.start("Browsing messages");
        List<TextMessage> listAfterAdd = mqService.browseAllMessages(QUEUE_NAME);
        for (TextMessage message : listAfterAdd) {
            LOG.info(message.toString());
        }
        mStopWatch.stop();
        Assert.assertEquals(MESSAGE_COUNT, listAfterAdd.size());

        mStopWatch.start("Remove all messages");
        mqService.removeAllMessages(QUEUE_NAME);
        mStopWatch.stop();
        Assert.assertEquals(0, mqService.browseAllMessages(QUEUE_NAME).size());
        LOG.info("\n{}", mStopWatch.prettyPrint());
    }

    @Test
    public void getMessagesById() throws MqException {
        mStopWatch = new StopWatch("getMessagesById()");
        mStopWatch.start("Sending messages");
        List<String> messageIdList = new ArrayList<>();
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            messageIdList.add(sendMQ(i, "Correlation_" + i));
        }
        mStopWatch.stop();
        mStopWatch.start("Browsing messages");
        Assert.assertEquals(MESSAGE_COUNT, mqService.browseAllMessages(QUEUE_NAME).size());
        mStopWatch.stop();

        mStopWatch.start("Searching messages by ID");
        for (String messId : messageIdList) {
            mqService.getMessageById(QUEUE_NAME, messId);
        }
        mStopWatch.stop();
        mStopWatch.start("Browsing messages");
        Assert.assertEquals(0, mqService.browseAllMessages(QUEUE_NAME).size());
        mStopWatch.stop();
        LOG.info("\n{}", mStopWatch.prettyPrint());
    }

    @Test
    public void getMessageByParam() throws MqException {
        mStopWatch = new StopWatch("getMessageByParam()");
        mStopWatch.start("Sending messages");
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            sendMQ(i, "Correlation_" + i);
        }
        sendMQ(100500, "Correlation_100500");
        mStopWatch.stop();
        mStopWatch.start("Browse all messages");
        Assert.assertEquals(MESSAGE_COUNT + 1, mqService.browseAllMessages(QUEUE_NAME).size());
        mStopWatch.stop();

        mStopWatch.start("Getting message by param");
        List<TextMessage> messByCorrelList = mqService.getMessagesByParam(QUEUE_NAME, "JMSCorrelationID", "ID:Correlation_100500");
        mStopWatch.stop();
        LOG.info("List of JMSMessageID:");
        for (TextMessage message : messByCorrelList) {
            try {
                LOG.info("---> Message ID: {}; Message text: {}", message.getJMSMessageID(), message.getText());
            } catch (JMSException e) {
                LOG.error("", e);
            }
        }
        LOG.info("----- End getting message by param -----");
        mStopWatch.start("Browse all messages");
        Assert.assertEquals(MESSAGE_COUNT, mqService.browseAllMessages(QUEUE_NAME).size());
        mStopWatch.stop();
        LOG.info("\n{}", mStopWatch.prettyPrint());
    }

    @Test
    public void answerTest() throws MqException {
        mStopWatch = new StopWatch("answerTest()");
        mStopWatch.start("Send message");
        String messageId = sendMQ(200500, "Correlation_Answer_Test");
        mStopWatch.stop();
        mStopWatch.start("Send answer");
        String updatedText = "Updated text";
        String answerId = mqService.sendAnswer(QUEUE_NAME, message -> {
            try {
                message.setText(updatedText);
            } catch (JMSException e) {
                LOG.error("Can't update text into test message!");
            }
            return message;
        });
        mStopWatch.stop();

        mStopWatch.start("Get message by ID");
        TextMessage messageById = mqService.getMessageById(QUEUE_NAME, answerId);
        mStopWatch.stop();
        Assert.assertTrue(messageById != null &&
                messageId.equals(getCorrelationId(messageById)) && updatedText.equals(mqService.getMessageText(messageById)));
        LOG.info("\n{}", mStopWatch.prettyPrint());
    }

    private String getCorrelationId(TextMessage messageById) {
        try {
            return messageById.getJMSCorrelationID();
        } catch (JMSException e) {
            LOG.error("Can't get correlation ID", e);
        }
        return "";
    }

    private String sendMQ(final int idx, final String correlationId) throws MqException {
        return mqService.sendRequest(QUEUE_NAME, message -> {
            try {
                message.setText("Test MQ " + idx);
                message.setJMSCorrelationID("ID:" + correlationId);
            } catch (JMSException e) {
                throw new JmsException("Can't set text to message");
            }
            return message;
        });
    }

    private Jms<TextMessage> createConnection() {
        QueueConnection queueConnection = null;
        try {
            ActiveMQConnectionFactory mqCF = new ActiveMQConnectionFactory(AMQ_BROKER_URL);
            queueConnection = mqCF.createQueueConnection();
        } catch (JMSException ex) {
            LOG.error("", ex);
        }

        Properties connProps = new Properties();
        connProps.put(MqFactory.MQ_TYPE, "activeMq");
        connProps.put(MqFactory.JMS_CONNECTION, queueConnection);
        mQueueConnection = queueConnection;
        return MqFactory.getMq(connProps);
    }
}
