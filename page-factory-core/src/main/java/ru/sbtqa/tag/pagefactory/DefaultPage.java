package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;

public abstract class DefaultPage extends Page {

    PageActions pageActions = Environment.getPageActions();
    PageChecks pageChecks = Environment.getPageChecks();

    public DefaultPage(WebDriver driver) {
        super(driver);
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
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
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
}
