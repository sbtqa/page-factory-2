package ru.sbtqa.tag.pagefactory;

import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.util.ActionsExt;
import ru.sbtqa.tag.pagefactory.util.ExpectedConditionsExt;
import ru.sbtqa.tag.pagefactory.util.PageFactoryUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.strategies.MatchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Contains basic actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class WebElementsPage extends Page {

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
        ActionsExt.fill(webElement, text);
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
        WebElement webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        ActionsExt.click(webElement);
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
        Actions actions = new Actions((WebDriver) PageContext.getCurrentPage());
        actions.sendKeys(key).perform();
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
        Actions actions = new Actions((WebDriver) PageContext.getCurrentPage());
        actions.moveToElement(PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle));
        actions.click();
        actions.sendKeys(key);
        actions.build().perform();
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
     * Find element with required title, perform
     * {@link #select(WebElement, String, MatchStrategy)} on found element Use
     * given match strategy
     *
     * @param elementTitle the title of SELECT element to interact
     * @param option the value to match against
     * @param strategy the strategy to match value
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    protected void select(String elementTitle, String option, MatchStrategy strategy) throws PageException {
        WebElement webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        select(webElement, option, strategy);
    }

    /**
     * Try to extract selectable options form given WebElement, and select
     * required one Add corresponding parameter to allure report
     *
     * @param webElement WebElement for interaction. Element is supposed to be
     * selectable, i.e. have select options
     * @param option the value to match against
     * @param strategy the strategy to match value. See {@link MatchStrategy}
     * for available values
     */
    @SuppressWarnings("unchecked")
    protected void select(WebElement webElement, String option, MatchStrategy strategy) {
        String jsString = ""
                + "var content=[]; "
                + "var options = arguments[0].getElementsByTagName('option'); "
                + " for (var i=0; i<options.length;i++){"
                + " content.push(options[i].text)"
                + "}"
                + "return content";
        List<String> options = (ArrayList<String>) ((JavascriptExecutor) PageContext.getCurrentPage()).
                executeScript(jsString, webElement);

        boolean isSelectionMade = false;
        for (int index = 0; index < options.size(); index++) {
            boolean isCurrentOption = false;
            String optionText = options.get(index).replaceAll("\\s+", "");
            String needOptionText = option.replaceAll("\\s+", "");

            if (strategy.equals(MatchStrategy.CONTAINS)) {
                isCurrentOption = optionText.contains(needOptionText);
            } else if (strategy.equals(MatchStrategy.EXACT)) {
                isCurrentOption = optionText.equals(needOptionText);
            }

            if (isCurrentOption) {
                Select select = new Select(webElement);
                select.selectByIndex(index);
                isSelectionMade = true;
                break;
            }
        }

        if (!isSelectionMade) {
            throw new AutotestError("There is no element '" + option + "' in " + ReflectionUtil.getElementTitle(PageContext.getCurrentPage(), webElement));
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
        ExpectedConditionsExt.acceptAlert(TestEnvironment.getDriverService().getDriver());
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
        ExpectedConditionsExt.dismissAlert(TestEnvironment.getDriverService().getDriver());
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
                TestEnvironment.getDriverService().getDriver().switchTo().window(popupHandle);
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
     * Perform {@link #checkValue(String, WebElement, MatchStrategy)} for the
     * specified WebElement. Use exact match strategy
     *
     * @param text string value that will be searched inside of the element
     * @param webElement WebElement to check
     */
    public void checkValue(String text, WebElement webElement) {
        checkValue(text, webElement, MatchStrategy.EXACT);
    }

    /**
     * Define a type of given WebElement, and check whether it either contains,
     * or exactly matches given text in its value. Currently supported elements
     * are text input and select box TODO: use HtmlElements here, to define
     * which element we are dealing with
     *
     * @param text string value that will be searched inside of the element
     * @param webElement WebElement to check
     * @param searchStrategy match strategy. See available strategies in
     * {@link MatchStrategy}
     */
    protected void checkValue(String text, WebElement webElement, MatchStrategy searchStrategy) {
        String value = "";
        switch (searchStrategy) {
            case EXACT:
                try {
                    switch (webElement.getTagName()) {
                        case "input":
                            value = webElement.getAttribute("value");
                            Assert.assertEquals(text.replaceAll("\\s+", ""), value.replaceAll("\\s+", ""));
                            break;
                        case "select":
                            value = webElement.getAttribute("title");
                            if (value.isEmpty() || !value.replaceAll("\\s+", "").equals(text.replaceAll("\\s+", ""))) {
                                value = webElement.getText();
                            }
                            Assert.assertEquals(text.replaceAll("\\s+", ""), value.replaceAll("\\s+", ""));
                            break;
                        default:
                            value = webElement.getText();
                            Assert.assertEquals(text.replaceAll("\\s+", ""), value.replaceAll("\\s+", ""));
                            break;
                    }
                } catch (Exception | AssertionError exception) {
                    throw new AutotestError("The actual value '" + value + "' of WebElement '" + webElement + "' are not equal to expected text '" + text + "'", exception);
                }
                break;
            case CONTAINS:
                try {
                    switch (webElement.getTagName()) {
                        case "input":
                            value = webElement.getAttribute("value");
                            Assert.assertTrue(value.replaceAll("\\s+", "").contains(text.replaceAll("\\s+", "")));
                            break;
                        case "select":
                            value = webElement.getAttribute("title");
                            if (value.isEmpty() || !value.replaceAll("\\s+", "").contains(text.replaceAll("\\s+", ""))) {
                                value = webElement.getText();
                            }
                            Assert.assertTrue(value.replaceAll("\\s+", "").contains(text.replaceAll("\\s+", "")));
                            break;
                        default:
                            value = webElement.getText();
                            Assert.assertTrue(value.replaceAll("\\s+", "").contains(text.replaceAll("\\s+", "")));
                            break;
                    }
                } catch (Exception | AssertionError exception) {
                    throw new AutotestError("The actual value '" + value + "' of WebElement '" + webElement + "' are not equal to expected text '" + text + "'", exception);
                }
                break;
        }

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
     * Check that given WebElement has a value attribute, and it is not empty
     *
     * @param webElement WebElement to check
     */
    protected void checkFieldIsNotEmpty(WebElement webElement) {
        String value = webElement.getText();
        if (value.isEmpty()) {
            value = webElement.getAttribute("value");
        }
        try {
            Assert.assertFalse(value.replaceAll("\\s+", "").isEmpty());
        } catch (Exception | AssertionError e) {
            throw new AutotestError("The field" + ReflectionUtil.getElementTitle(PageContext.getCurrentPage(), webElement) + " is empty", e);
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
    public void checkValuesAreNotEqual(String text, String elementTitle) throws PageException {
        WebElement webElement = PageFactoryUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (checkValuesAreNotEqual(text, webElement)) {
            throw new AutotestError("'" + text + "' is equal with '" + elementTitle + "' value");
        }
    }

    /**
     * Extract value from the given WebElement, and compare the it with the
     * given text Text, as well as element value are being space-trimmed before
     * comparison, so only non-space characters matter
     *
     * @param text a {@link java.lang.String} object.
     * @param webElement a {@link org.openqa.selenium.WebElement} object.
     * @return a boolean.
     */
    protected boolean checkValuesAreNotEqual(String text, WebElement webElement) {
        if ("input".equals(webElement.getTagName())) {
            return webElement.getAttribute("value").replaceAll("\\s+", "").equals(text.replaceAll("\\s+", ""));
        } else {
            return webElement.getText().replaceAll("\\s+", "").equals(text.replaceAll("\\s+", ""));
        }
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
        if (!ExpectedConditionsExt.checkElementWithTextIsPresent(TestEnvironment.getDriverService().getDriver(), text, PageFactory.getTimeOutInSeconds())) {
            throw new AutotestError("Text '" + text + "' is not present");
        }
    }

}