package ru.sbtqa.tag.pagefactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.drivers.TagMobileDriver;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.extensions.DriverExtension;
import ru.sbtqa.tag.pagefactory.extensions.WebExtension;
import ru.sbtqa.tag.pagefactory.support.AdbConsole;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.pagefactory.util.PageFactoryUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

/**
 * Contains basic actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class MobilePage extends Page {

    private static final Logger LOG = LoggerFactory.getLogger(MobilePage.class);

    /**
     * Find element with specified title annotation, and fill it with given text
     * Add elementTitle-&gt;text as parameter-&gt;value to corresponding step in
     * allure report
     *
     * @param elementTitle element to fill
     * @param text text to enter
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if page was not
     * initialized, or required element couldn't be found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.fill.field")
    public void fillField(String elementTitle, String text) throws PageException {
        WebElement webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        webElement.click();

        if (PageFactory.getEnvironment() == Environment.WEB) {
            webElement.clear();
        }

        if (PageFactory.getEnvironment() == Environment.MOBILE && TagMobileDriver.getAppiumClickAdb()) {
            // set ADBKeyBoard as default
            AdbConsole.execute("ime set com.android.adbkeyboard/.AdbIME");
            // send broadcast intent via adb
            AdbConsole.execute(String.format("am broadcast -a ADB_INPUT_TEXT --es msg '%s'", text));
        } else {
            webElement.sendKeys(text);
        }

        ParamsHelper.addParam(elementTitle, text);
    }

    /**
     * Find specified WebElement on a page, and click it Add corresponding
     * parameter to allure report
     *
     * @param elementTitle title of the element to click
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if page was not
     * initialized, or required element couldn't be found
     */
    @ActionTitles({
            @ActionTitle("ru.sbtqa.tag.pagefactory.click.link"),
            @ActionTitle("ru.sbtqa.tag.pagefactory.click.button")})
    public void clickElementByTitle(String elementTitle) throws PageException {
        WebElement webElement;
        try {
            webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
            DriverExtension.waitForElementGetEnabled(webElement, PageFactory.getTimeOut());
        } catch (NoSuchElementException | WaitException | ElementNotFoundException e) {
            LOG.warn("Failed to find element by title {}", elementTitle, e);
            webElement = DriverExtension.waitUntilElementAppearsInDom(By.partialLinkText(elementTitle));
        }
        clickWebElement(webElement);
    }

    /**
     * Press specified key on a keyboard Add corresponding parameter to allure
     * report
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.press.key")
    public void pressKey(String keyName) {
        Keys key = Keys.valueOf(keyName.toUpperCase());
        Actions actions = PageFactory.getActions();
        actions.sendKeys(key).perform();
        ParamsHelper.addParam(keyName, " is pressed");
    }

    /**
     * Focus a WebElement, and send specified key into it Add corresponding
     * parameter to allure report
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     * @param elementTitle title of WebElement that accepts key commands
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException if
     * couldn't find element with required title
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.press.key")
    public void pressKey(String keyName, String elementTitle) throws PageException {
        Keys key = Keys.valueOf(keyName.toUpperCase());
        Actions actions = PageFactory.getActions();
        actions.moveToElement(PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle));
        actions.click();
        actions.sendKeys(key);
        actions.build().perform();
        ParamsHelper.addParam(keyName, " is pressed on element " + elementTitle + "'");
    }

    /**
     * Find element with required title, perform
     * {@link #select(WebElement, String, MatchStrategy)} on found element Use
     * exact match strategy
     *
     * @param elementTitle WebElement that is supposed to be selectable
     * @param option option to select
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.select")
    public void select(String elementTitle, String option) throws PageException {
        WebElement webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (null != option) {
            select(webElement, option, MatchStrategy.EXACT);
        }
    }


    /**
     * Wait for appearance of the required text in current DOM model. Text will
     * be space-trimmed, so only non-space characters will matter.
     *
     * @param text text to search
     * @throws WaitException if text didn't appear on the page during the
     * timeout
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.text.appears.on.page")
    public void assertTextAppears(String text) throws WaitException {
        WebExtension.waitForTextPresenceInPageSource(text, true);
    }

    /**
     * Check whether specified text is absent on the page. Text is being
     * space-trimmed before assertion, so only non-space characters will matter
     *
     * @param text text to search for
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.text.absent.on.page")
    public void assertTextIsNotPresent(String text) {
        WebExtension.waitForTextPresenceInPageSource(text, false);
    }

    /**
     * Wait for a new browser window, then wait for a specific text inside the
     * appeared window List of previously opened windows is being saved before
     * each click, so if modal window appears without click, this method won't
     * catch it. Text is being waited by {@link #assertTextAppears}, so it will
     * be space-trimmed as well
     *
     * @param text text that will be searched inside of the window
     * @throws ru.sbtqa.tag.pagefactory.exceptions.WaitException if
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.modal.window.with.text.appears")
    public void assertModalWindowAppears(String text) throws WaitException {
        try {
            String popupHandle = WebExtension.findNewWindowHandle((Set<String>) Stash.getValue("beforeClickHandles"));
            if (null != popupHandle && !popupHandle.isEmpty()) {
                PageFactory.getWebDriver().switchTo().window(popupHandle);
            }
            assertTextAppears(text);
        } catch (Exception ex) {
            throw new WaitException("Modal window with text '" + text + "' didn't appear during timeout", ex);
        }
    }

    /**
     * Perform {@link #checkValue(String, WebElement, MatchStrategy)} for the
     * WebElement with corresponding title on a current page. Use exact match
     * strategy
     *
     * @param text string value that will be searched inside of the element
     * @param elementTitle title of the element to search
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException if
     * couldn't find element by given title, or current page isn't initialized
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.value")
    public void checkValue(String elementTitle, String text) throws PageException {
        checkValue(text, PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle), MatchStrategy.EXACT);
    }


    /**
     * Find element by given title, and check whether it is not empty See
     * {@link #checkFieldIsNotEmpty(WebElement)} for details
     *
     * @param elementTitle title of the element to check
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if current page
     * was not initialized, or element wasn't found on the page
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.field.not.empty")
    public void checkFieldIsNotEmpty(String elementTitle) throws PageException {
        WebElement webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        checkFieldIsNotEmpty(webElement);
    }

    /**
     * Find element with corresponding title, and make sure that its value is
     * not equal to given text Text, as well as element value are being
     * space-trimmed before comparison, so only non-space characters matter
     *
     * @param text element value for comparison
     * @param elementTitle title of the element to search
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if current page
     * wasn't initialized, or element with required title was not found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.values.not.equal")
    public void checkValuesAreNotEqual(String text, String elementTitle) throws PageException {
        WebElement webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (checkValuesAreNotEqual(text, webElement)) {
            throw new AutotestError("'" + text + "' is equal with '" + elementTitle + "' value");
        }
    }

    /**
     * Perform a check that there is an element with required text on current
     * page
     *
     * @param text a {@link java.lang.String} object.
     */
    @ActionTitles({
            @ActionTitle("ru.sbtqa.tag.pagefactory.check.element.with.text.present")
            ,
            @ActionTitle("ru.sbtqa.tag.pagefactory.check.text.visible")})
    public void checkElementWithTextIsPresent(String text) {
        if (!DriverExtension.checkElementWithTextIsPresent(text, PageFactory.getTimeOutInSeconds())) {
            throw new AutotestError("Text '" + text + "' is not present");
        }
    }

}