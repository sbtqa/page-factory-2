package ru.sbtqa.tag.mqimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.mqfactory.interfaces.Condition;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class SubstringCondition implements Condition<TextMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(SubstringCondition.class);
    private final String rquid;

    public SubstringCondition(String rquid) {
        this.rquid = rquid;
    }

    @Override
    public boolean isSupply(TextMessage message) {
        if (rquid != null && !rquid.trim().isEmpty()) {
            try {
                return message.getText().contains(rquid);
            } catch (JMSException e) {
                LOG.error("", e);
            }
        }
        return false;
    }
}
