package ru.sbtqa.tag.pagefactory.util;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;

public class ActionsExt {

    private static final Logger LOG = LoggerFactory.getLogger(ActionsExt.class);

    public static void fill(WebElement webElement, String text) {
        click(webElement);
        if (null != text) {
            try {
                webElement.clear();
            } catch (InvalidElementStateException | NullPointerException e) {
                LOG.debug("Failed to clear web element {}", webElement, e);
            }
        }
        webElement.sendKeys(text);
    }

    public static void click(WebElement webElement) {
        ExpectedConditionsExt.waitForElementGetEnabled(webElement, PageFactory.getTimeOut());
        webElement.click();
    }

    public void pressKey(WebElement webElement, Keys keyName) {
        webElement.sendKeys(keyName);
    }
}
