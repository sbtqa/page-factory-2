package ru.sbtqa.tag.mqfactory;


//import com.ibm.jms.JMSTextMessage;
//import com.ibm.mq.jms.JMSC;
//import com.ibm.mq.jms.MQQueueConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.mqfactory.exception.MqException;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

//import org.testng.Assert;
//import org.testng.annotations.Test;

/**
 * @author Vadim Trofimov
 * @since 22.12.2017
 */
public class MqTest {

    private static final Logger LOG = LoggerFactory.getLogger(MqTest.class);
    public static final String UFS_RESPONSE = "UFS.RESPONSE";
    //    private Jms<JMSTextMessage> mq;
    private List<String> messageIdList = new ArrayList<>();

    //    @Test
    public void createConnection() throws MqException, JMSException {
//        mq = getJmsTextMessage();
//        Assert.assertNotNull(mq);
    }

    //    @Test
    public void sendMessages() throws MqException {
//        int size = mq.browseAllMessages(UFS_RESPONSE).size();
//        for (int i = 1; i <= 2; i++) {
//            final int ii = i;
//            messageIdList.add(mq.sendRequest(UFS_RESPONSE, message -> {
//                try {
//                    message.setText("Test MQ " + ii);
//                    message.setJMSCorrelationID("ID:111D11111D11111E111D1E111C1111111B1F1E1A1111EF11");
//                } catch (JMSException e) {
//                    throw new JmsException("Can't set text to message");
//                }
//                return message;
//            }));
//        }
//        Assert.assertEquals(size + messageIdList.size(), mq.browseAllMessages(UFS_RESPONSE).size());
    }

    //    @Test
    public void browseMessages() throws MqException {
//        List<JMSTextMessage> list = mq.browseAllMessages(UFS_RESPONSE);
//        Assert.assertNotNull(list);
//        for (JMSTextMessage obj : list) {
//            LOG.info(obj.toString());
//        }
//        // check, that first browse did not remove messages
//        Assert.assertEquals(list.size(), mq.browseAllMessages(UFS_RESPONSE).size());
    }

    //    @Test
    public void getByIdWithRemoveMessages() throws MqException, JMSException {
//        List<JMSTextMessage> list = mq.browseAllMessages(UFS_RESPONSE);
//        Assert.assertNotNull(list);
//        for (JMSTextMessage obj : list) {
//            LOG.info(obj.toString());
//        }
//
//        LOG.info("----- Delete messages -----");
//        for (String messId : messageIdList) {
//            LOG.info("---> try remove message '{}'", messId);
//            JMSTextMessage message = mq.getMessageById(UFS_RESPONSE, messId);
//            Assert.assertNotNull(message);
//            Assert.assertEquals(message.getJMSMessageID(), messId);
//        }
//
//        Assert.assertEquals(list.size() - messageIdList.size(), mq.browseAllMessages(UFS_RESPONSE).size());
//        messageIdList.clear();
    }


    //    @Test
    public void getByParamWithRemoveMessages() throws MqException, JMSException {
//        List<JMSTextMessage> list = mq.browseAllMessages(UFS_RESPONSE);
//        Assert.assertNotNull(list);
//        for (JMSTextMessage obj : list) {
//            LOG.info(obj.toString());
//        }
//
//        LOG.info("----- Browse and delete -----");
//        List<JMSTextMessage> messByCorrelList = mq.getMessagesByParam(UFS_RESPONSE, "JMSCorrelationID", "ID:111D11111D11111E111D1E111C1111111B1F1E1A1111EF11");
//        Assert.assertNotNull(messByCorrelList);
//        LOG.info("List of JMSMessageID:");
//        for (JMSTextMessage obj : messByCorrelList) {
//            LOG.info("---> Message ID: {}; Message text: {}", obj.getJMSMessageID(), obj.getText());
//        }
//        Assert.assertEquals(list.size() - messageIdList.size(), mq.browseAllMessages(UFS_RESPONSE).size());
//        messageIdList.clear();
    }

//    private Jms<JMSTextMessage> getJmsTextMessage() {
//        QueueConnection queueConnection = null;
//        try {
//            MQQueueConnectionFactory mqCF = new MQQueueConnectionFactory();
//            mqCF.setHostName("10.116.93.208");
//            mqCF.setPort(13012);
//            mqCF.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
//            mqCF.setChannel("SC.TEST");
////        mqCF.setQueueManager("");
//            queueConnection = mqCF.createQueueConnection();
//        } catch (JMSException ex) {
//            LOG.error("", ex);
//        }
//
//        Assert.assertNotNull(queueConnection);
//        Properties connProps = new Properties();
//        connProps.put(MqFactory.MQ_TYPE, "activeMq");
//        connProps.put(MqFactory.JMS_CONNECTION, queueConnection);
//        return MqFactory.getMq(connProps);
//    }

}
