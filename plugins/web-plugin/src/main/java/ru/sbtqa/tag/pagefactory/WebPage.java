package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.sbtqa.tag.pagefactory.web.utils.WebExpectedConditionsUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;

/**
 * Contains basic actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class WebPage<T extends Object> extends Page {

    PageActions pageActions = Environment.getPageActions();
    PageChecks pageChecks = Environment.getPageChecks();

    public WebPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public WebPage(WebDriver driver, FieldDecorator decorator) {
        super(driver);
        PageFactory.initElements(decorator, this);
    }
    
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
    public void fill(String elementTitle, String text) throws PageException {
        T element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.fill(element, text);
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
    public void click(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.click(element);
    }

    /**
     * Press specified key on a keyboard Add corresponding parameter to allure
     * report
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.press.key")
    public void pressKey(String keyName) {
        pageActions.press(null, keyName);
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
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.press(element, keyName);
    }

    /**
     * TODO
     *
     * @param elementTitle WebElement that is supposed to be selectable
     * @param option option to select
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.select")
    public void select(String elementTitle, String option) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.select(element, option);
    }

    /**
     * Find web element with corresponding title, if it is a check box, select
     * it If it's a WebElement instance, check whether it is already selected,
     * and click if it's not Add corresponding parameter to allure report
     *
     * @param elementTitle WebElement that is supposed to represent checkbox
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if page was not
     * initialized, or required element couldn't be found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.select.checkbox")
    public void setCheckBox(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.setCheckbox(element, true);
    }


    /**
     * TODO
     *
     * @param text string value that will be searched inside of the element
     * @param elementTitle title of the element to search
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException if
     * couldn't find element by given title, or current page isn't initialized
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.value")
    public void checkValueIsEqual(String elementTitle, String text) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!pageChecks.checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is not equal with '" + text + "'");
        }
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
    public void checkValueIsNotEqual(String text, String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (pageChecks.checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is equal with '" + text + "'");
        }
    }

    /**
     * TODO
     *
     * @param elementTitle title of the element to check
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if current page
     * was not initialized, or element wasn't found on the page
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.field.not.empty")
    public void checkNotEmpty(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (pageChecks.checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is empty");
        }
    }

    @ActionTitle("ru.sbtqa.tag.pagefactory.check.field.empty")
    public void checkEmpty(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!pageChecks.checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is not empty");
        }
    }


    /**
     * Wait for an alert with specified text, and accept it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     * timeout
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.accept.alert")
    public void acceptAlert(String text) throws WaitException {
        ((WebPageActions) pageActions).acceptAlert();
    }

    /**
     * Wait for an alert with specified text, and dismiss it
     *
     * @param text alert message
     * @throws WaitException in case if alert didn't appear during default wait
     * timeout
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.dismiss.alert")
    public void dismissAlert(String text) throws WaitException {
        ((WebPageActions) pageActions).dismissAlert();
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
    public void checkTextAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, true);
    }

    /**
     * Check whether specified text is absent on the page. Text is being
     * space-trimmed before assertion, so only non-space characters will matter
     *
     * @param text text to search for
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.text.absent.on.page")
    public void checkTextIsNotPresent(String text) {
        WebExpectedConditionsUtils.waitForTextPresenceInPageSource(text, false);
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
    public void checkModalWindowAppears(String text) throws WaitException {
        WebExpectedConditionsUtils.waitForModalWindowWithText(text);
    }

    /**
     * Perform a check that there is an element with required text on current
     * page
     *
     * @param text a {@link java.lang.String} object.
     */
    @ActionTitles({
            @ActionTitle("ru.sbtqa.tag.pagefactory.check.element.with.text.present"),
            @ActionTitle("ru.sbtqa.tag.pagefactory.check.text.visible")})
    public void checkElementWithTextIsPresent(String text) {
        if (!WebExpectedConditionsUtils.checkElementWithTextIsPresent(text)) {
            throw new AutotestError("Text '" + text + "' is not present");
        }
    }
}