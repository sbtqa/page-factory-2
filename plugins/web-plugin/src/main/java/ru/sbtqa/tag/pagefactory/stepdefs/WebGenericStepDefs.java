package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.DataTable;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
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
 * To pass a Cucumber {@link DataTable} as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | header 1| header 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a {@link DataTable} object.
 * First line is not enforced to be a header.
 * <p>
 * To pass a list as parameter, use flattened table as follows: | value 1 | }
 * value 2 |
 *
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class WebGenericStepDefs extends WebSetupSteps{

    private static final Logger LOG = LoggerFactory.getLogger(WebGenericStepDefs.class);
    
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
     * Element is focused
     *
     * @param element element to focus on
     */
    public void isElementFocused(String element) {
        LOG.warn("Note that isElementFocused method is still an empty!");
    }
}
