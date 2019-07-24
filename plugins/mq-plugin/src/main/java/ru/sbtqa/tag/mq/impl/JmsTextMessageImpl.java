package ru.sbtqa.tag.mq.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.mq.factory.MqConfiguration;
import ru.sbtqa.tag.mq.factory.exception.JmsException;
import ru.sbtqa.tag.mq.factory.exception.MqException;
import ru.sbtqa.tag.mq.factory.interfaces.Condition;
import ru.sbtqa.tag.mq.factory.interfaces.Jms;
import ru.sbtqa.tag.mq.factory.interfaces.JmsUpdater;

import javax.jms.*;
import javax.jms.Queue;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class JmsTextMessageImpl implements Jms<TextMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(JmsTextMessageImpl.class);
    private static final MqConfiguration PROPS = MqConfiguration.create();

    public static final String JMS_MESSAGE_ID = "JMSMessageID";
    public static final String JMS_CORRELATION_ID = "JMSCorrelationID";

    private Connection connection;
    private String lastMsgId;

    public JmsTextMessageImpl(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("Connection must be not null!");
        }
        this.connection = connection;

        LOG.info("*** [MQ] Set timeout to {} ms ***", PROPS.getMqTimeout());
    }

    @Override
    public String sendRequest(String queueName, String requestMsg) throws MqException {
        return sendRequest(queueName, message -> {
            try {
                message.setText(requestMsg);
            } catch (JMSException ex) {
                throw new JmsException("Can't set text to message", ex);
            }
            return message;
        });
    }

    @Override
    public String sendAnswer(String queueName, JmsUpdater<TextMessage> prepareMessage) throws MqException {
        return sendRequest(queueName, prepareMessage, true);
    }

    @Override
    public String sendRequest(String queueName, JmsUpdater<TextMessage> prepareMessage) throws MqException {
        return sendRequest(queueName, prepareMessage, false);
    }

    @Override
    public TextMessage getAnswer(String queueName) throws JmsException {
        Session session = createSession();
        Queue queue = createQueue(session, queueName);
        try {
            LOG.info("*** [MQ] Receiving answer ***");
            TextMessage message = receiveMessage(session, queue, JMS_CORRELATION_ID, lastMsgId, PROPS.getMqTimeout());
            if (message != null) {
                lastMsgId = "";
            }
            LOG.info("*** [MQ] Removed last message ID ***");
            return message;
        } finally {
            sessionClose(session);
        }
    }

    @Override
    public TextMessage getMessageById(String queueName, String messageId) throws MqException {
        Session session = createSession();
        Queue queue = createQueue(session, queueName);
        try {
            return receiveMessage(session, queue, JMS_MESSAGE_ID, messageId, PROPS.getMqTimeout());
        } finally {
            sessionClose(session);
        }
    }

    @Override
    public List<TextMessage> getMessagesByParam(String queueName, String paramName, String paramValue) throws JmsException {
        List<TextMessage> messages = new ArrayList<>();
        Session session = createSession();
        try {
            Queue queue = createQueue(session, queueName);
            QueueBrowser browser;
            try {
                browser = session.createBrowser(queue, String.format("%s='%s'", paramName, paramValue));
            } catch (JMSException ex) {
                throw new JmsException("Can't open browser session", ex);
            }

            Enumeration queueEnum;
            long startTime = System.currentTimeMillis();
            Set<String> browsedMessages = new HashSet<>();
            while ((System.currentTimeMillis() - startTime < PROPS.getMqTimeout()) && messages.isEmpty()) {
                try {
                    queueEnum = browser.getEnumeration();
                } catch (JMSException ex) {
                    throw new JmsException("Can't get enumeration of queue", ex);
                }

                while (queueEnum.hasMoreElements()) {
                    TextMessage message = (TextMessage) queueEnum.nextElement();
                    String messageId = getMessageId(message);
                    if (messageId != null && !browsedMessages.contains(messageId)) {
                        browsedMessages.add(messageId);
                        Message receivedMessage = receiveMessage(session, queue, JMS_MESSAGE_ID, messageId, PROPS.getMqTimeout());
                        if (receivedMessage != null) {
                            messages.add((TextMessage) receivedMessage);
                        }
                    }
                } // while has no more elements
            } // while timeout
        } finally {
            sessionClose(session);
        }
        return messages;
    }

    @Override
    public List<TextMessage> browseAllMessages(String queueName) throws JmsException {
        List<TextMessage> messages = new ArrayList<>();
        Session session = createSession();
        try {
            LOG.info("*** [MQ] Browsing messages ***\n --> from Queue: {}", queueName);
            Enumeration queueEnum;
            Queue queue = createQueue(session, queueName);
            try {
                queueEnum = session.createBrowser(queue).getEnumeration();
            } catch (JMSException ex) {
                throw new JmsException("Can't create browser enumeration", ex);
            }
            int count = 0;
            while (queueEnum.hasMoreElements()) {
                TextMessage message = (TextMessage) queueEnum.nextElement();
                messages.add(message);
                count++;
                if (LOG.isDebugEnabled()) {
                    try {
                        LOG.debug(" --> [#{}] browse message {}:\n{}", count, message.getJMSMessageID(), message.getText());
                    } catch (JMSException ex) {
                        LOG.error("", ex);
                    }
                }
            }
            LOG.info("*** [MQ] Browse successfully completed (found {} messages) ***", count);
        } finally {
            sessionClose(session);
        }
        return messages;
    }

    @Override
    public List<TextMessage> findMessagesByFilter(String queueName, Condition<TextMessage> condition) throws JmsException {
        List<TextMessage> messages = new ArrayList<>();
        Session session = createSession();
        try {
            Queue queue = createQueue(session, queueName);
            QueueBrowser browser;
            try {
                browser = session.createBrowser(queue);
            } catch (JMSException ex) {
                throw new JmsException("Can't create browser session", ex);
            }

            LOG.info("*** [MQ] Searching message by filter ***\n --> from Queue: {}\n --> Condition: {}", queueName, condition.getClass().getName());
            Enumeration queueEnum;
            long startTime = System.currentTimeMillis();
            Set<String> browsedMessages = new HashSet<>();
            while ((System.currentTimeMillis() - startTime < PROPS.getMqTimeout()) && messages.isEmpty()) {
                try {
                    queueEnum = browser.getEnumeration();
                } catch (JMSException ex) {
                    throw new JmsException("Can't get enumeration of queue", ex);
                }

                boolean hasElements = queueEnum.hasMoreElements();
                while (queueEnum.hasMoreElements()) {
                    TextMessage message = (TextMessage) queueEnum.nextElement();
                    String messageId;
                    try {
                        messageId = message.getJMSMessageID();
                    } catch (JMSException e) {
                        throw new JmsException("Can't get enumeration of queue", e);
                    }
                    if (messageId != null && !browsedMessages.contains(messageId)) {
                        browsedMessages.add(messageId);
                        if (condition.isSupply(message)) {
                            messages.add(receiveMessage(session, queue, JMS_MESSAGE_ID, messageId, 0));
                        }
                    }
                }

                if (!hasElements) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        LOG.error("", e);
                    }
                }
            }
        } finally {
            sessionClose(session);
        }
        LOG.info("*** [MQ] Search successfully completed (found {} messages) ***", messages.size());
        return messages;
    }

    @Override
    public String getMessageText(TextMessage message) throws JmsException {
        try {
            return message.getText();
        } catch (JMSException ex) {
            throw new JmsException("Can't get text from message", ex);
        }
    }

    @Override
    public String getMessageId(TextMessage message) throws JmsException {
        try {
            return message.getJMSMessageID();
        } catch (JMSException ex) {
            throw new JmsException("Can't get ID message", ex);
        }
    }

    @Override
    public String getMessageProperty(TextMessage message, String property) throws JmsException {
        try {
            return message.getStringProperty(property);
        } catch (JMSException ex) {
            throw new JmsException("Can't get property from message", ex);
        }
    }

    @Override
    public void removeAllMessages(String queueName) throws JmsException {
        Session session = createSession();
        try {
            LOG.info("*** [MQ] Removing messages ***\n --> from Queue: {}", queueName);
            Enumeration queueEnum;
            Queue queue = createQueue(session, queueName);
            try {
                queueEnum = session.createBrowser(queue).getEnumeration();
            } catch (JMSException ex) {
                throw new JmsException("Can't create browser session", ex);
            }
            int count = 0;
            while (queueEnum.hasMoreElements()) {
                TextMessage message = (TextMessage) queueEnum.nextElement();
                String messageId;
                messageId = getMessageId(message);
                if (messageId != null) {
                    try {
                        MessageConsumer consumer = session.createConsumer(queue, String.format("%s='%s'", JMS_MESSAGE_ID, messageId));
                        consumer.receive(PROPS.getMqTimeout());
                        count++;
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(" --> [#{}] remove message {}", count, messageId);
                        }
                    } catch (JMSException e) {
                        //ignore
                    }
                }
            }
            LOG.info("*** [MQ] Remove successfully completed (deleted {} messages) ***", count);
        } finally {
            sessionClose(session);
        }
    }

    @Override
    public void removeMessage(String queueName, String messageId) throws JmsException {
        Session session = createSession();
        Queue queue = createQueue(session, queueName);
        try {
            MessageConsumer consumer = session.createConsumer(queue, String.format("%s='%s'", JMS_MESSAGE_ID, messageId));
            Message message = consumer.receive(PROPS.getMqTimeout());
            if (LOG.isDebugEnabled() && message != null) {
                LOG.debug("*** [MQ] Removed message {} from queue {} ***", messageId, queueName);
            }
        } catch (JMSException e) {
            // ignore
        } finally {
            sessionClose(session);
        }
    }

    private String sendRequest(String queueName, JmsUpdater<TextMessage> prepareMessage, boolean isAnswer) throws JmsException {
        Session session = createSession();
        Queue queue = createQueue(session, queueName);
        try {
            LOG.info("*** [MQ] Publishing message ***\n --> to Queue: {}", queueName);
            MessageProducer producer = session.createProducer(queue);
            TextMessage request = prepareMessage.update(session.createTextMessage());
            if (isAnswer && !lastMsgId.isEmpty()) {
                request.setJMSCorrelationID(lastMsgId);
                LOG.info("*** [MQ] Set Correlation ID: {}", lastMsgId);
            }
            producer.send(request);
            String messageID = request.getJMSMessageID();
            LOG.info("*** [MQ] Published Message {}:\n{}", messageID, request.getText());
            lastMsgId = messageID;
            LOG.info("*** [MQ] Set last message ID: {}", lastMsgId);
            return messageID;
        } catch (Exception ex) {
            throw new JmsException("Can't send message", ex);
        } finally {
            sessionClose(session);
        }
    }

    private TextMessage receiveMessage(Session session, Queue queue, String property, String value, long timeout) throws JmsException {
        try {
            LOG.info("*** [MQ] Receiving message ***\n --> from Queue: {}\n --> by {}: {}", queue.getQueueName(), property, value);
            MessageConsumer receiver = session.createConsumer(queue, String.format("%s='%s'", property, value));
            TextMessage message;
            if (timeout > 0) {
                message = (TextMessage) receiver.receive(timeout);
            } else {
                message = (TextMessage) receiver.receiveNoWait();
            }
            if (message != null) {
                lastMsgId = message.getJMSMessageID();
                LOG.info("*** [MQ] Received Message {}:\n{}", lastMsgId, message.getText());
                LOG.info("*** [MQ] Set last message ID: {}", lastMsgId);
            }
            return message;
        } catch (JMSException e) {
            LOG.error("", e);
            throw new JmsException("Can't receive message", e);
        }
    }

    private Session createSession() throws JmsException {
        try {
            connection.start();
            return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException ex) {
            throw new JmsException("Can't get session", ex);
        }
    }

    private void sessionClose(Session session) throws JmsException {
        try {
            session.close();
            connection.stop();
        } catch (JMSException ex) {
            throw new JmsException("Can't close session", ex);
        }
    }

    private Queue createQueue(Session session, String queueName) throws JmsException {
        try {
            return session.createQueue(queueName);
        } catch (JMSException ex) {
            throw new JmsException("Can't create queue " + queueName, ex);
        }
    }

}
