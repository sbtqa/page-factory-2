package ru.sbtqa.tag.pagefactory.web.support;

import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.web.utils.WebExpectedConditionsUtils;

public class Alert {

    private static final Logger LOG = LoggerFactory.getLogger(WebExpectedConditionsUtils.class);
    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    private org.openqa.selenium.Alert alert;

    public Alert() {
        alert = switchToAlert();
    }

    private org.openqa.selenium.Alert switchToAlert() {
        long timeoutTime = System.currentTimeMillis() + PROPERTIES.getTimeout() * 1000;

        while (timeoutTime > System.currentTimeMillis()) {
            try {
                return PageContext.getCurrentPage().getDriver().switchTo().alert();
            } catch (Exception e) {
                LOG.debug("Alert has not appeared yet", e);
            }

            sleep(1);
        }

        throw new WaitException("Timed out after '" + PROPERTIES.getTimeout() + "' seconds waiting for alert to accept");
    }

    private void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            LOG.warn("Error while thread is sleeping", e);
            Thread.currentThread().interrupt();
        }
    }

    public boolean checkValue(String expectedValue) {
         return expectedValue.equals(alert.getText());
    }

    public void accept() {
         alert.accept();
    }

    public void dismiss() {
         alert.dismiss();
    }
}
