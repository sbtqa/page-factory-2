package ru.sbtqa.tag.pagefactory.web.junit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.junit.CoreSteps;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import ru.sbtqa.tag.pagefactory.web.utils.WebWait;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.util.ArrayList;
import java.util.List;

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
 * @param <T> type of steps - any successor {@code WebStepsImpl}
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class WebStepsImpl<T extends WebStepsImpl<T>> extends CoreSteps<T> {

    public WebStepsImpl() {
        WebSetupSteps.initWeb();
    }

    /**
     * Open a copy for current page in a new browser tab User|he keywords are
     * optional
     *
     * @return Returns itself
     */
    public T openCopyPage() {
        String pageUrl = Environment.getDriverService().getDriver().getCurrentUrl();
        ((JavascriptExecutor) Environment.getDriverService().getDriver()).executeScript("window.open('" + pageUrl + "', '_blank')");
        List<String> tabs = new ArrayList<>(Environment.getDriverService().getDriver().getWindowHandles());
        Environment.getDriverService().getDriver().switchTo().window(tabs.get(tabs.size() - 1));
        return (T) this;
    }

    /**
     * Switch to a neighbour browser tab
     *
     * @return Returns itself
     */
    public T switchesToNextTab() {
        String currentTab = Environment.getDriverService().getDriver().getWindowHandle();
        List<String> tabs = new ArrayList<>(Environment.getDriverService().getDriver().getWindowHandles());
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).equals(currentTab)) {
                Environment.getDriverService().getDriver().switchTo().window(tabs.get(i + 1));
                return (T) this;
            }
        }
        throw new AutotestError("Unable to to find current tab " + currentTab);
    }

    /**
     * Check that current URL matches the inputted one
     *
     * @param url url for comparison
     * @return Returns itself
     */
    public T urlMatches(String url) {
        Assert.assertEquals("URL is different from the expected: ", url, Environment.getDriverService().getDriver().getCurrentUrl());
        return (T) this;
    }

    /**
     * Close current browser tab and open a tab with given name
     *
     * @param title title of the page to open
     * @return Returns itself
     */
    public T closingCurrentWin(String title) {
        Environment.getDriverService().getDriver().close();
        for (String windowHandle : Environment.getDriverService().getDriver().getWindowHandles()) {
            Environment.getDriverService().getDriver().switchTo().window(windowHandle);
            if (Environment.getDriverService().getDriver().getTitle().equals(title)) {
                return (T) this;
            }
        }
        throw new AutotestError("Unable to return to the previously opened page: " + title);
    }

    /**
     * Return to previous location (via browser "back" button)
     *
     * @return Returns itself
     */
    public T backPage() {
        Environment.getDriverService().getDriver().navigate().back();
        return (T) this;
    }

    /**
     * Go to specified url
     *
     * @param url url to go to
     * @return Returns itself
     */
    public T goToUrl(String url) {
        Environment.getDriverService().getDriver().get(url);
        return (T) this;
    }

    /**
     * Refresh browser page
     *
     * @return Returns itself
     */
    public T reInitPage() {
        Environment.getDriverService().getDriver().navigate().refresh();
        return (T) this;
    }

    /**
     * Wait for appearance of the required text in current DOM model. Text will
     * be space-trimmed, so only non-space characters will matter.
     *
     * @param text text to search
     * @return Returns itself
     * @throws WaitException if text didn't appear on the page during the
     * timeout
     * @throws java.lang.InterruptedException if the element is missing
     */
    public T checkTextAppears(String text) throws WaitException, InterruptedException {
        WebWait.waitForTextPresenceInPageSource(text, true);
        return (T) this;
    }

    /**
     * Check whether specified text is absent on the page. Text is being
     * space-trimmed before assertion, so only non-space characters will matter
     *
     * @param text text to search for
     * @return Returns itself
     * @throws java.lang.InterruptedException if the element is missing
     */
    public T checkTextIsNotPresent(String text) throws InterruptedException {
        WebWait.waitForTextPresenceInPageSource(text, false);
        return (T) this;
    }

    /**
     * Wait for a new browser window, then wait for a specific text inside the
     * appeared window List of previously opened windows is being saved before
     * each click, so if modal window appears without click, this method won't
     * catch it. Text is being waited by {@link #checkTextAppears}, so it will
     * be space-trimmed as well
     *
     * @param text text that will be searched inside of the window
     * @return Returns itself
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException if
     */
    public T checkModalWindowAppears(String text) throws WaitException {
        WebWait.waitForModalWindowWithText(text);
        return (T) this;
    }

    /**
     * Perform a check that there is an element with required text on current
     * page
     *
     * @param text a {@link java.lang.String} object
     * @return Returns itself
     */
    public T checkElementWithTextIsPresent(String text) {
        WebWait.visibility(By.xpath("//*[contains(text(), '" + text + "')]"), "Text \"" + text + "\" is not present");
        return (T) this;
    }

    public T putElementValueInStash(String elementName, String variableName) throws PageException {
        Page currentPage = PageContext.getCurrentPage();
        WebElement element = Environment.getFindUtils().getElementByTitle(currentPage, elementName);
        Stash.put(variableName, ElementUtils.getWebElementValue(element));
        return (T) this;
    }
}
