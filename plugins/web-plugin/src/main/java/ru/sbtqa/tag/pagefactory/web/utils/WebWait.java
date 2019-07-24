package ru.sbtqa.tag.pagefactory.web.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.utils.Wait;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

public class WebWait extends Wait {

    private static final Logger LOG = LoggerFactory.getLogger(WebWait.class);
    private static final WebConfiguration PROPERTIES = WebConfiguration.create();

    /**
     * Wait for page prepared with javascript
     *
     * @param stopRecursion the end-waiting flag
     * @throws WaitException in case if page didn't load
     */
    public static void waitForPageToLoad(boolean... stopRecursion) throws WaitException {
        long timeoutTime = System.currentTimeMillis() + PROPERTIES.getTimeout() * 1000;
        while (timeoutTime > System.currentTimeMillis()) {
            try {
                if ("complete".equals(((JavascriptExecutor) Environment.getDriverService().getDriver()).executeScript("return document.readyState"))) {
                    return;
                }
                Thread.sleep(1000);
            } catch (Exception | AssertionError e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("WebPage does not become to ready state", e);
                }
                Environment.getDriverService().getDriver().navigate().refresh();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("WebPage refreshed");
                }
                if ((stopRecursion.length == 0) || !stopRecursion[0]) {
                    waitForPageToLoad(true);
                }
            }
        }

        throw new WaitException("Timed out after " + PROPERTIES.getTimeout() + " seconds waiting for preparedness of page");
    }

    /**
     * Wait until specified text either appears, or disappears from page source
     *
     * @param text text to search in page source
     * @param shouldTextBePresent boolean, self explanatory
     * @throws WaitException in case if text didn't load in the page source
     */
    public static void waitForTextPresenceInPageSource(String text, boolean shouldTextBePresent) throws WaitException, InterruptedException {
        long timeoutTime = System.currentTimeMillis() + PROPERTIES.getTimeout() * 1000;
        Wait.presence(By.tagName("body"), "Element \"body\" did not appear after timeout");
        while (timeoutTime > System.currentTimeMillis()) {
            WebElement body = Environment.getDriverService().getDriver().findElement(By.tagName("body"));
            if (body.getText().replaceAll("\\s+", "").contains(text.replaceAll("\\s+", "")) == shouldTextBePresent) {
                return;
            }
            Thread.sleep(1000);
        }
        throw new WaitException("Timed out after '" + PROPERTIES.getTimeout() + "' seconds waiting for presence of '" + text + "' in page source");
    }

    /**
     * Wait for a modal window with text to appear
     * @param text expected text in modal window
     * @throws WaitException in case if window was not found
     */
    public static void waitForModalWindowWithText(String text) throws WaitException {
        try {
            String popupHandle = WindowHandles.findNewWindowHandle(Stash.getValue("beforeClickHandles"));
            if (null != popupHandle && !popupHandle.isEmpty()) {
                Environment.getDriverService().getDriver().switchTo().window(popupHandle);
            }
            waitForTextPresenceInPageSource(text, true);
        } catch (Exception ex) {
            throw new WaitException("Modal window with text '" + text + "' didn't appear during timeout", ex);
        }
    }
}
