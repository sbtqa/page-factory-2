package ru.sbtqa.tag.pagefactory.html.junit;

import cucumber.api.DataTable;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.HTMLPage;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.elements.radiogroup.RadioGroupAbstract;
import ru.sbtqa.tag.pagefactory.elements.select.SelectAbstract;
import ru.sbtqa.tag.pagefactory.elements.select.SelectValue;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;
import ru.sbtqa.tag.pagefactory.transformer.enums.Presence;
import ru.sbtqa.tag.pagefactory.transformer.enums.SearchStrategy;
import ru.sbtqa.tag.pagefactory.utils.Wait;
import ru.sbtqa.tag.pagefactory.web.junit.WebStepsImpl;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextInput;
import static ru.sbtqa.tag.pagefactory.utils.HtmlElementUtils.getWebElement;

/**
 * Basic step definitions, that should be available on every project Notations
 * used in this class: Block - a class that extends {@link HtmlElement} and has
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
 * @param <T> type of steps - any successor {@code HtmlStepsImpl}
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 * <p>
 * <p>
 * Step Definitions for html-plugin.
 * Common action with pages describes by html-elements.
 */
public class HtmlStepsImpl<T extends HtmlStepsImpl<T>> extends WebStepsImpl<T> implements Steps {

    private static final String ERROR_LIST_EMPTY = "List of the elements is empty";
    private static final String ERROR_ELEMENT_NOT_FOUND = "Element not found: ";

    private AccordionSteps accordionSteps;
    private ControlSteps controlSteps;
    private TableSteps tableSteps;
    private PanelSteps panelSteps;
    private HintSteps hintSteps;
    private SelectSteps selectSteps;

    public HtmlStepsImpl() {
        HtmlSetupSteps.initHtml();
    }

    public AccordionSteps accordionSteps() {
        return accordionSteps == null ? new AccordionSteps() : accordionSteps;
    }

    public ControlSteps controlSteps() {
        return controlSteps == null ? new ControlSteps() : controlSteps;
    }

    public TableSteps tableSteps() {
        return tableSteps == null ? new TableSteps() : tableSteps;
    }

    public PanelSteps panelSteps() {
        return panelSteps == null ? new PanelSteps() : panelSteps;
    }

    public HintSteps hintCoreSteps() {
        return hintSteps == null ? new HintSteps() : hintSteps;
    }

    public SelectSteps selectSteps() {
        return selectSteps == null ? new SelectSteps() : selectSteps;
    }

    /**
     * Execute action with no parameters inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @return Returns itself
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public T actionInBlock(String block, String action) throws NoSuchMethodException {
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(block, action);
        return (T) this;
    }

    /**
     * Execute action with parameters taken from specified {@link DataTable}
     * inside block element User|he keywords are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param dataTable table of parameters
     * @return Returns itself
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public T actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(block, action, dataTable);
        return (T) this;
    }

    /**
     * Execute action with one parameter inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param param parameter
     * @return Returns itself
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public T actionInBlock(String block, String action, String... param) throws NoSuchMethodException {
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(block, action, param);
        return (T) this;
    }

    /**
     * Find element with given value in specified list User|he keywords are
     * optional
     *
     * @param listTitle title of the list to search for
     * @param value required value of the element. for text elements value is
     * being checked via getText() method
     * @return Returns itself
     * @throws PageException if page wasn't initialized of required list wasn't
     * found
     */
    public T find(String listTitle, String value) throws PageException {
        List<WebElement> elements = ((HtmlFindUtils) Environment.getFindUtils()).findList(null, value);
        WebElement element = ElementUtils.getElementByTextWithEmptyResult(elements, value);
        Assert.assertNotNull(String.format("Element with text '%s' is absent in list '%s'", value, listTitle), element);
        return (T) this;
    }

    public T clearField(String fieldName) {
        getFindUtils().find(fieldName, TextInput.class).clear();
        return (T) this;
    }

    public T fillForm(DataTable dataTable) {
        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
        dataMap.forEach(this::setFormElementValue);
        return (T) this;
    }

    public T setFormElementValue(String key, String value) {
        WebElement element = getElement(key);
        if (element instanceof TextInput) {
            element.clear();
            element.sendKeys(value);
        } else {
            if (element instanceof SelectValue) {
                ((SelectValue) element).selectByValue(value);
            } else {
                if (element instanceof CheckBox) {
                    ((CheckBox) element).set(Boolean.valueOf(value));
                } else {
                    throw new AutotestError("Incorrect element type: " + key);
                }
            }
        }
        return (T) this;
    }

    public T elementAlwaysPresent(Presence present, String elementName) {
        boolean isPresent = getCurrentPage().isElementPresent(elementName);
        boolean flag = present.equals(Presence.POSITIVE);
        Assert.assertEquals((!flag ? "Element is present: " : ERROR_ELEMENT_NOT_FOUND) + elementName, flag, isPresent);
        return (T) this;
    }

    public T elementsAlwaysPresent(Presence present, List<String> elementNames) {
        Assert.assertFalse(ERROR_LIST_EMPTY, elementNames.isEmpty());
        elementNames.forEach((element) -> {
            elementAlwaysPresent(present, element);
        });
        return (T) this;
    }

    public T elementsPresentAndDisabled(Condition condition, List<String> elementNames) {
        boolean result;
        boolean isPositive = condition.equals(Condition.POSITIVE);
        for (String element : elementNames) {
            result = getElement(element).isEnabled();
            Assert.assertEquals("Field is " + (isPositive ? "" : "not ") + "editable.", isPositive, result);
        }
        return (T) this;
    }

    public T elementPresentAndDisabled(String elementName, Condition negation) {
        boolean isPositive = negation.equals(Condition.POSITIVE);
        Assert.assertEquals("Field is " + (isPositive ? "" : "not ") + "editable",
                isPositive, getElement(elementName).isEnabled());
        return (T) this;
    }

    public T elementsPresentAndSelected(Condition condition, String elementName) {
        boolean isPositive = condition.equals(Condition.POSITIVE);
        boolean result = getFindUtils().find(elementName, Button.class).isSelected();
        Assert.assertEquals("Button is " + (isPositive ? "" : "not ") + "selected", isPositive, result);
        return (T) this;
    }

    public T putInStash(String elementName, String variableName) {
        Stash.put(variableName, getText(elementName));
        return (T) this;
    }

    protected String getText(String elementName) {
        WebElement element = getElement(elementName);
        if (element instanceof CheckBox) {
            return Boolean.toString(element.isSelected());
        } else if (element instanceof SelectAbstract) {
            return ((SelectAbstract) element).getSelectedOptionValue();
        } else if (element instanceof RadioGroupAbstract) {
            return ((RadioGroupAbstract) element).getSelectedValue();
        } else {
            return element.getText();
        }
    }

    public T moveToElement(String elementName) {
        Actions actions = new Actions((WebDriver) Environment.getDriverService().getDriver());
        actions.moveToElement(getWebElement(getElement(elementName))).perform();
        return (T) this;
    }

    public T checkTextList(List<String> textList) {
        textList.forEach(this::checkText);
        return (T) this;
    }

    public T checkText(String expectedText) {
        WebDriver driver = Environment.getDriverService().getDriver();
        String[] texts = expectedText.split("\n");
        for (String text : texts) {
            List<WebElement> textElements = driver.findElements(By.xpath("//*[contains(text(),'" + text.trim() + "')]"));
            Assert.assertFalse("Text not found on page: \"" + text + "\""
                    + ". Check if the search text is in several page elements at the same time", textElements.isEmpty());
        }
        return (T) this;
    }

    public T openPage(String pageName, String elementName) throws PageInitializationException {
        PageManager.getPage(pageName);
        Wait.visibility(elementName);
        return (T) this;
    }

    public T checkForm(DataTable dataTable) {
        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
        dataMap.forEach((key, value) ->
                Assert.assertEquals("Element value is not as expected.", getText(key), value)
        );
        return (T) this;
    }

    public T checkElementText(String elementName, String expectedText) {
        String elementText = getElement(elementName).getText();
        ElementUtils.checkText(elementText, expectedText, SearchStrategy.CONTAINS);
        return (T) this;
    }

    public T checkTextEquals(String elementName, String expectedText) {
        String elementText = getElement(elementName).getText();
        ElementUtils.checkText(elementText, expectedText, SearchStrategy.EQUALS);
        return (T) this;
    }

    public T checkInputMask(String elementName, String mask) {
        getCurrentPage().checkInputMask(elementName, mask);
        return (T) this;
    }

    public T clickByOverlapElement(String elementName) {
        WebElement element = getWebElement(getElement(elementName));
        ((JavascriptExecutor) Environment.getDriverService().getDriver()).executeScript("arguments[0].click();", element);
        return (T) this;
    }

    private WebElement getElement(String elementName) {
        return getFindUtils().find(elementName);
    }

    private HTMLPage getCurrentPage() {
        HTMLPage page = (HTMLPage) PageContext.getCurrentPage();
        if (null == PageContext.getCurrentPage()) {
            throw new AutotestError("Error retrieving current page");
        } else {
            return page;
        }
    }
}
