package ru.sbtqa.tag.stepdefs;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.sbtqa.tag.pagefactory.web.utils.WebExpectedConditionsUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;

/**
 * Basic step definitions, that should be available on every project Notations
 * used in this class: Page - a class that extends {@link ru.sbtqa.tag.pagefactory.Page} and has
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation Action -
 * a method with {@link ru.sbtqa.tag.pagefactory.annotations.ActionTitle}
 * annotation in page object List - list of {@link WebElement}'s with
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation on page
 * object
 * <p>
 * To pass a Cucumber DataTable as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | header 1| header 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a DataTable object.
 * First line is not enforced to be a header.
 * <p>
 * To pass a list as parameter, use flattened table as follows: | value 1 | }
 * value 2 |
 *
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class WebGenericSteps extends WebSetupSteps {

    private static final Logger LOG = LoggerFactory.getLogger(WebGenericSteps.class);
    
    /**
     * Open a copy for current page in a new browser tab User|he keywords are
     * optional
     */
    public void openCopyPage() {
        String pageUrl = Environment.getDriverService().getDriver().getCurrentUrl();
        ((JavascriptExecutor) Environment.getDriverService().getDriver()).executeScript("window.open('" + pageUrl + "', '_blank')");
        List<String> tabs = new ArrayList<>(Environment.getDriverService().getDriver().getWindowHandles());
        Environment.getDriverService().getDriver().switchTo().window(tabs.get(tabs.size() - 1));
    }

    /**
     * Switch to a neighbour browser tab
     */
    public void switchesToNextTab() {
        String currentTab = Environment.getDriverService().getDriver().getWindowHandle();
        List<String> tabs = new ArrayList<>(Environment.getDriverService().getDriver().getWindowHandles());
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).equals(currentTab)) {
                Environment.getDriverService().getDriver().switchTo().window(tabs.get(i + 1));
                return;
            }
        }
    }

    /**
     * Check that current URL matches the inputted one
     *
     * @param url url for comparison
     */
    public void urlMatches(String url) {
        Assert.assertEquals("URL is different from the expected: ", url, Environment.getDriverService().getDriver().getCurrentUrl());
    }

    /**
     * Close current browser tab and open a tab with given name
     *
     * @param title title of the page to open
     */
    public void closingCurrentWin(String title) {
        Environment.getDriverService().getDriver().close();
        for (String windowHandle : Environment.getDriverService().getDriver().getWindowHandles()) {
            Environment.getDriverService().getDriver().switchTo().window(windowHandle);
            if (Environment.getDriverService().getDriver().getTitle().equals(title)) {
                return;
            }
        }
        throw new AutotestError("Unable to return to the previously opened page: " + title);
    }

    /**
     * Return to previous location (via browser "back" button)
     */
    public void backPage() {
        Environment.getDriverService().getDriver().navigate().back();
    }

    /**
     * Go to specified url
     *
     * @param url url to go to
     */
    public void goToUrl(String url) {
        Environment.getDriverService().getDriver().get(url);
    }

    /**
     * Initialize a page with corresponding URL
     *
     * @param url value of the
     * {@link ru.sbtqa.tag.pagefactory.annotations.PageEntry#url} to search for
     * @throws PageInitializationException if page with corresponding URL is
     * absent or couldn't be initialized
     */
    public void goToPageByUrl(String url) throws PageInitializationException {
        PageManager.changeUrlByTitle(url);
    }

    /**
     * Refresh browser page
     */
    public void reInitPage() {
        Environment.getDriverService().getDriver().navigate().refresh();
    }

    /**
     * Wait for an alert with specified text, and accept it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     * timeout
     */
    public void acceptAlert(String text) throws WaitException {
        ((WebPageActions) Environment.getPageActions()).acceptAlert();
    }

    /**
     * Wait for an alert with specified text, and dismiss it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     * timeout
     */
    public void dismissAlert(String text) throws WaitException {
        ((WebPageActions) Environment.getPageActions()).dismissAlert();
    }

    /**
     * Wait for appearance of the required text in current DOM model. Text will
     * be space-trimmed, so only non-space characters will matter.
     *
     * @param text text to search
     * @throws WaitException if text didn't appear on the page during the
     * timeout
     */
    public void checkTextAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, true);
    }

    /**
     * Check whether specified text is absent on the page. Text is being
     * space-trimmed before assertion, so only non-space characters will matter
     *
     * @param text text to search for
     */
    public void checkTextIsNotPresent(String text) {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, false);
    }

    /**
     * Wait for a new browser window, then wait for a specific text inside the
     * appeared window List of previously opened windows is being saved before
     * each click, so if modal window appears without click, this method won't
     * catch it. Text is being waited by {@link #checkTextAppears}, so it will
     * be space-trimmed as well
     *
     * @param text text that will be searched inside of the window
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException if
     */
    public void checkModalWindowAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForModalWindowWithText(text);
    }

    /**
     * Perform a check that there is an element with required text on current
     * page
     *
     * @param text a {@link java.lang.String} object.
     */
    public void checkElementWithTextIsPresent(String text) {
        if (!WebExpectedConditionsUtils.checkElementWithTextIsPresent(text)) {
            throw new AutotestError("Text '" + text + "' is not present");
        }
    }

    /**
     * Element is focused
     *
     * @param element element to focus on
     */
    public void isElementFocused(String element) {
        LOG.warn("Note that isElementFocused method is still an empty!");
    }
}
