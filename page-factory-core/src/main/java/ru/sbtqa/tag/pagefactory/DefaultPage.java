package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;

public abstract class DefaultPage extends Page {

    PageActions pageActions;
    PageChecks pageChecks;

    public DefaultPage(WebDriver driver) {
        super(driver);
        pageActions = Environment.getPageActions();
        pageChecks = Environment.getPageChecks();
    }

    /**
     * Fill specified element with text
     *
     * @param elementTitle element to fill
     * @param text text to enter
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.fill.field")
    public void fill(String elementTitle, String text) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.fill(element, text);
    }

    /**
     * Click specified element
     *
     * @param elementTitle title of the element to click
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    @ActionTitles({
            @ActionTitle("ru.sbtqa.tag.pagefactory.click.link"),
            @ActionTitle("ru.sbtqa.tag.pagefactory.click.button")})
    public void click(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.click(element);
    }

    /**
     * Press key on keyboard
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.press.key")
    public void pressKey(String keyName) {
        pageActions.press(null, keyName);
    }

    /**
     * Press key on keyboard with focus on specified element
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     * @param elementTitle title of element that accepts key commands
     * @throws PageException if couldn't find element with required title
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.press.key")
    public void pressKey(String keyName, String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.press(element, keyName);
    }

    /**
     * Select specified option in select-element
     *
     * @param elementTitle element that is supposed to be selectable
     * @param option option to select
     * @throws PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.select")
    public void select(String elementTitle, String option) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.select(element, option);
    }

    /**
     * Set checkbox element to selected state
     *
     * @param elementTitle element that is supposed to represent checkbox
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.select.checkbox")
    public void setCheckBox(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        pageActions.setCheckbox(element, true);
    }


    /**
     * Check that the element's value is equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @throws ElementNotFoundException if couldn't find element by given title, or current page isn't initialized
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.value")
    public void checkValueIsEqual(String elementTitle, String text) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!pageChecks.checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is not equal with '" + text + "'");
        }
    }

    /**
     * Check that the element's value is not equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @throws PageException if current page wasn't initialized, or element with required title was not found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.values.not.equal")
    public void checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (pageChecks.checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is equal with '" + text + "'");
        }
    }

    /**
     * Check that the element's value is not empty
     *
     * @param elementTitle title of the element to check
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.field.not.empty")
    public void checkNotEmpty(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (pageChecks.checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is empty");
        }
    }

    /**
     * Check that the element's value is empty
     *
     * @param elementTitle title of the element to check
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.check.field.empty")
    public void checkEmpty(String elementTitle) throws PageException {
        Object element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!pageChecks.checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is not empty");
        }
    }
}
