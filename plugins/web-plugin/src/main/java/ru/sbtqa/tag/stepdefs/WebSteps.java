package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
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
public class WebSteps extends CoreSteps {

    private static WebSteps instance;

    public WebSteps() {
        WebSetupSteps.initWeb();
    }

    public static WebSteps getInstance() {
        if (instance == null) {
            instance = new WebSteps();
        }
        return instance;
    }

    /**
     * Open a copy for current page in a new browser tab User|he keywords are
     * optional
     */
    public WebSteps openCopyPage() {
        String pageUrl = Environment.getDriverService().getDriver().getCurrentUrl();
        ((JavascriptExecutor) Environment.getDriverService().getDriver()).executeScript("window.open('" + pageUrl + "', '_blank')");
        List<String> tabs = new ArrayList<>(Environment.getDriverService().getDriver().getWindowHandles());
        Environment.getDriverService().getDriver().switchTo().window(tabs.get(tabs.size() - 1));
        return this;
    }

    /**
     * Switch to a neighbour browser tab
     */
    public WebSteps switchesToNextTab() {
        String currentTab = Environment.getDriverService().getDriver().getWindowHandle();
        List<String> tabs = new ArrayList<>(Environment.getDriverService().getDriver().getWindowHandles());
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).equals(currentTab)) {
                Environment.getDriverService().getDriver().switchTo().window(tabs.get(i + 1));
                return this;
            }
        }
        throw new AutotestError("Unable to to find current tab " + currentTab);
    }

    /**
     * Check that current URL matches the inputted one
     *
     * @param url url for comparison
     */
    public WebSteps urlMatches(String url) {
        Assert.assertEquals("URL is different from the expected: ", url, Environment.getDriverService().getDriver().getCurrentUrl());
        return this;
    }

    /**
     * Close current browser tab and open a tab with given name
     *
     * @param title title of the page to open
     */
    public WebSteps closingCurrentWin(String title) {
        Environment.getDriverService().getDriver().close();
        for (String windowHandle : Environment.getDriverService().getDriver().getWindowHandles()) {
            Environment.getDriverService().getDriver().switchTo().window(windowHandle);
            if (Environment.getDriverService().getDriver().getTitle().equals(title)) {
                return this;
            }
        }
        throw new AutotestError("Unable to return to the previously opened page: " + title);
    }

    /**
     * Return to previous location (via browser "back" button)
     */
    public WebSteps backPage() {
        Environment.getDriverService().getDriver().navigate().back();
        return this;
    }

    /**
     * Go to specified url
     *
     * @param url url to go to
     */
    public WebSteps goToUrl(String url) {
        Environment.getDriverService().getDriver().get(url);
        return this;
    }

    /**
     * Initialize a page with corresponding URL
     *
     * @param url value of the
     * {@link ru.sbtqa.tag.pagefactory.annotations.PageEntry#url} to search for
     * @throws PageInitializationException if page with corresponding URL is
     * absent or couldn't be initialized
     */
    public WebSteps goToPageByUrl(String url) throws PageInitializationException {
        PageManager.changeUrlByTitle(url);
        return this;
    }

    /**
     * Refresh browser page
     */
    public WebSteps reInitPage() {
        Environment.getDriverService().getDriver().navigate().refresh();
        return this;
    }

    /**
     * Wait for an alert with specified text, and accept it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     * timeout
     */
    public WebSteps acceptAlert(String text) throws WaitException {
        ((WebPageActions) Environment.getPageActions()).acceptAlert();
        return this;
    }

    /**
     * Wait for an alert with specified text, and dismiss it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     * timeout
     */
    public WebSteps dismissAlert(String text) throws WaitException {
        ((WebPageActions) Environment.getPageActions()).dismissAlert();
        return this;
    }

    /**
     * Wait for appearance of the required text in current DOM model. Text will
     * be space-trimmed, so only non-space characters will matter.
     *
     * @param text text to search
     * @throws WaitException if text didn't appear on the page during the
     * timeout
     */
    public WebSteps checkTextAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, true);
        return this;
    }

    /**
     * Check whether specified text is absent on the page. Text is being
     * space-trimmed before assertion, so only non-space characters will matter
     *
     * @param text text to search for
     */
    public WebSteps checkTextIsNotPresent(String text) {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, false);
        return this;
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
    public WebSteps checkModalWindowAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForModalWindowWithText(text);
        return this;
    }

    /**
     * Perform a check that there is an element with required text on current
     * page
     *
     * @param text a {@link java.lang.String} object.
     */
    public WebSteps checkElementWithTextIsPresent(String text) {
        if (!WebExpectedConditionsUtils.checkElementWithTextIsPresent(text)) {
            throw new AutotestError("Text '" + text + "' is not present");
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps openPage(String title) throws PageInitializationException {
        return (WebSteps) super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps action(String action) throws NoSuchMethodException {
        return (WebSteps) super.action(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps action(String action, String param) throws NoSuchMethodException {
        return (WebSteps) super.action(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps action(String action, String param1, String param2) throws NoSuchMethodException {
        return (WebSteps) super.action(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps action(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        return (WebSteps) super.action(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps action(String action, DataTable dataTable) throws NoSuchMethodException {
        return (WebSteps) super.action(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        return (WebSteps) super.action(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps action(String action, List<String> list) throws NoSuchMethodException {
        return (WebSteps) super.action(action, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps fill(String elementTitle, String text) throws PageException {
        return (WebSteps) super.fill(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps click(String elementTitle) throws PageException {
        return (WebSteps) super.click(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps pressKey(String keyName) {
        return (WebSteps) super.pressKey(keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps pressKey(String keyName, String elementTitle) throws PageException {
        return (WebSteps) super.pressKey(keyName, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps select(String elementTitle, String option) throws PageException {
        return (WebSteps) super.select(elementTitle, option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps setCheckBox(String elementTitle) throws PageException {
        return (WebSteps) super.setCheckBox(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps checkValueIsEqual(String elementTitle, String text) throws PageException {
        return (WebSteps) super.checkValueIsEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        return (WebSteps) super.checkValueIsNotEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps checkNotEmpty(String elementTitle) throws PageException {
        return (WebSteps) super.checkNotEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps checkEmpty(String elementTitle) throws PageException {
        return (WebSteps) super.checkEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps isElementFocused(String element) {
        return (WebSteps) super.isElementFocused(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebSteps userInsertsFragment(String fragmentName) throws FragmentException {
        return (WebSteps) super.userInsertsFragment(fragmentName);
    }
}
