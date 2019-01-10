package ru.sbtqa.tag.pagefactory.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.web.utils.WebWait;

public class Alert {

    private static final Logger LOG = LoggerFactory.getLogger(WebWait.class);
    private static final Configuration PROPERTIES = Configuration.create();

    private org.openqa.selenium.Alert alert;

    public Alert() {
        this(PROPERTIES.getTimeout());
    }

    public Alert(int timeout) {
        if (!Environment.isDriverEmpty()) {
            alert = switchToAlert(timeout);
        }
    }

    private org.openqa.selenium.Alert switchToAlert(int timeout) {
        long timeoutTime = System.currentTimeMillis() + timeout * 1000;

        while (timeoutTime > System.currentTimeMillis()) {
            try {
                return Environment.getDriverService().getDriver().switchTo().alert();
            } catch (Exception e) {
                LOG.debug("Alert has not appeared yet", e);
            }

            sleep(1);
        }

        throw new WaitException("Timed out after '" + timeout + "' seconds waiting for alert to accept");
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
