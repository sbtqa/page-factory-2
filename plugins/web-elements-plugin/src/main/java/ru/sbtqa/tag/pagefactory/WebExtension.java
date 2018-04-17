package ru.sbtqa.tag.pagefactory;

import java.util.Set;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ExpectedConditionsUtils;
import ru.sbtqa.tag.qautils.managers.DateManager;

public class WebExtension {

    private static final Logger LOG = LoggerFactory.getLogger(WebExtension.class);
    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    /**
     * Get outer element text. Used for get text from checkboxes and radio
     * buttons
     *
     * @param webElement TODO
     * @return text of element
     */
    public static String getElementValue(WebElement webElement) throws IllegalArgumentException {
        String elementValue = "Cannot parse element";
        String elementId = webElement.getAttribute("id");

        if (elementId == null) {
            throw new IllegalArgumentException("Getting value is not support in element without id");
        }

        WebElement possibleTextMatcher = PageContext.getCurrentPage().getDriver().findElement(By.xpath("//*[@id='" + elementId + "']/.."));
        if (possibleTextMatcher.getText().isEmpty()) {
            possibleTextMatcher = PageContext.getCurrentPage().getDriver().findElement(By.xpath("//*[@id='" + elementId + "']/../.."));
            if ("tr".equals(possibleTextMatcher.getTagName())) {
                elementValue = possibleTextMatcher.getText();
            }
        } else {
            elementValue = possibleTextMatcher.getText();
        }
        return elementValue;
    }

    /**
     * Wait for page prepared with javascript
     *
     * @param stopRecursion TODO
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException TODO
     */
    public static void waitForPageToLoad(boolean... stopRecursion) throws WaitException {

        long timeoutTime = System.currentTimeMillis() + PROPERTIES.getTimeout() / 1000;
        while (timeoutTime > System.currentTimeMillis()) {
            try {
                if ("complete".equals((String) ((JavascriptExecutor) PageContext.getCurrentPage().getDriver()).executeScript("return document.readyState"))) {
                    return;
                }
                sleep(1);
            } catch (Exception | AssertionError e) {
                LOG.debug("WebPage does not become to ready state", e);
                PageContext.getCurrentPage().getDriver().navigate().refresh();
                LOG.debug("WebPage refreshed");
                if ((stopRecursion.length == 0) || (stopRecursion.length > 0 && !stopRecursion[0])) {
                    waitForPageToLoad(true);
                }
            }
        }

        throw new WaitException("Timed out after " + PROPERTIES.getTimeout() + " seconds waiting for preparedness of page");
    }

    /**
     * @param webElement a {@link org.openqa.selenium.WebElement} object.
     * @param timeout in milliseconds
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException TODO
     */
    public static void waitForTextInInputExists(WebElement webElement, long timeout) throws WaitException {
        long timeoutTime = DateManager.getCurrentTimestamp() + timeout;
        while (timeoutTime > DateManager.getCurrentTimestamp()) {
            sleep(1);
            if (!webElement.getAttribute("value").isEmpty()) {
                return;
            }
        }
        throw new WaitException("Timed out after '" + timeout + "' milliseconds waiting for existence of '" + webElement + "'");
    }

    /**
     * Wait until specified text either appears, or disappears from page source
     *
     * @param text                text to search in page source
     * @param shouldTextBePresent boolean, self explanatory
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException TODO
     */
    public static void waitForTextPresenceInPageSource(String text, boolean shouldTextBePresent) throws WaitException {
        long timeoutTime = System.currentTimeMillis() + PROPERTIES.getTimeout() / 1000;
        WebElement body = ExpectedConditionsUtils.waitUntilElementAppearsInDom(By.tagName("body"));
        while (timeoutTime > System.currentTimeMillis()) {
            sleep(1);
            if (body.getText().replaceAll("\\s+", "").contains(text.replaceAll("\\s+", "")) == shouldTextBePresent) {
                return;
            }
        }
        throw new WaitException("Timed out after '" + PROPERTIES.getTimeout() + "' seconds waiting for presence of '" + text + "' in page source");
    }

    /**
     * @param existingHandles TODO
     * @param timeout         TODO
     * @return TODO
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException TODO
     */
    public static String findNewWindowHandle(Set<String> existingHandles, int timeout) throws WaitException {
        long timeoutTime = System.currentTimeMillis() + timeout;

        while (timeoutTime > System.currentTimeMillis()) {
            Set<String> currentHandles = PageContext.getCurrentPage().getDriver().getWindowHandles();

            if (currentHandles.size() != existingHandles.size()
                    || (currentHandles.size() == existingHandles.size() && !currentHandles.equals(existingHandles))) {
                for (String currentHandle : currentHandles) {
                    if (!existingHandles.contains(currentHandle)) {
                        return currentHandle;
                    }
                }
            }
            sleep(1);
        }

        throw new WaitException("Timed out after '" + timeout + "' milliseconds waiting for new modal window");
    }

    /**
     * @param existingHandles TODO
     * @return TODO
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException TODO
     */
    public static String findNewWindowHandle(Set<String> existingHandles) throws WaitException {
        return findNewWindowHandle(existingHandles, PROPERTIES.getTimeout() / 1000);
    }


    public static String getElementBorderStyle(WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) PageContext.getCurrentPage().getDriver();
        return (String) js.executeScript("return arguments[0].style.border", webElement);
    }

    /**
     * Turn on element highlight
     *
     * @param webElement TODO
     * @return initial element style
     */
    public static void highlightElementOn(WebElement webElement) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) PageContext.getCurrentPage().getDriver();
            js.executeScript("arguments[0].style.border='3px solid red'", webElement);
        } catch (Exception e) {
            LOG.warn("Something went wrong with element highlight", e);
        }
    }

    /**
     * Turn off element highlight
     *
     * @param webElement TODO
     * @param originalStyle      element style to set
     */
    public static void highlightElementOff(WebElement webElement, String originalStyle) {
        originalStyle = (originalStyle == null) ? "" : originalStyle;
        try {
            JavascriptExecutor js = (JavascriptExecutor) PageContext.getCurrentPage().getDriver();
            js.executeScript("arguments[0].style.border='" + originalStyle + "'", webElement);
        } catch (Exception e) {
            LOG.debug("Something went wrong with element highlight", e);
        }
    }

    /**
     * @param sec a int.
     */
    private static void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            LOG.warn("Error while thread is sleeping", e);
            Thread.currentThread().interrupt();
        }
    }
}
