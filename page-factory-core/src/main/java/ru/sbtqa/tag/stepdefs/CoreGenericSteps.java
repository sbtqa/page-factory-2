package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import static java.lang.String.format;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;
import ru.sbtqa.tag.pagefactory.utils.Wait;
import ru.sbtqa.tag.qautils.errors.AutotestError;

/**
 * Basic step definitions, that should be available on every project Notations
 * used in this class: Page - a class that extends {@link ru.sbtqa.tag.pagefactory.Page} and has
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation Action -
 * a method with {@link ru.sbtqa.tag.pagefactory.annotations.ActionTitle}
 * annotation in page object List - list of objects's with
 * {@link ru.sbtqa.tag.pagefactory.annotations.ElementTitle} annotation on page
 * object
 * <p>
 * To pass a Cucumber {@link cucumber.api.DataTable} as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | header 1| header 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a {@link cucumber.api.DataTable} object.
 * First line is not enforced to be a header.
 * <p>
 * To pass a list as parameter, use flattened table as follows: | value 1 | }
 * value 2 |
 *
 * @param <T> type of steps - any successor {@code CoreGenericSteps}
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class CoreGenericSteps<T extends CoreGenericSteps<T>> {

    private static final Logger LOG = LoggerFactory.getLogger(CoreGenericSteps.class);
    private static final Configuration PROPERTIES = Configuration.create();

    public CoreGenericSteps() {
        CoreSetupSteps.preSetUp();
        CoreSetupSteps.setUp();
    }

    /**
     * Initialize a page with corresponding title (defined via
     * {@link ru.sbtqa.tag.pagefactory.annotations.PageEntry} annotation)
     * User|he keywords are optional
     *
     * @param title of the page to initialize
     * @return Returns itself 
     * @throws PageInitializationException if page initialization failed
     */
    public T openPage(String title) throws PageInitializationException {
        PageManager.getPage(title);
        return (T) this;
    }

    /**
     * Execute action with no parameters User|he keywords are optional
     *
     * @param action title of the action to execute
     * @return Returns itself 
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public T action(String action) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action);
        return (T) this;
    }

    /**
     * Execute action with one parameter User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @return Returns itself 
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public T action(String action, Object... param) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, param);
        return (T) this;
    }

    /**
     * Execute action with parameters from given {@link DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param dataTable table of parameters
     * @return Returns itself 
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public T action(String action, DataTable dataTable) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, dataTable);
        return (T) this;
    }

    /**
     * Execute action with string parameter and {@link DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @param dataTable table of parameters
     * @return Returns itself 
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public T action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, param, dataTable);
        return (T) this;
    }

    /**
     * Execute action with parameters taken from list User|he keywords are
     * optional
     *
     * @param action title of the action to execute
     * @param list parameters list
     * @return Returns itself 
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public T action(String action, List<String> list) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, list);
        return (T) this;
    }

    /**
     * Fill specified element with text
     *
     * @param <E> element type
     * @param elementTitle element to fill
     * @param text text to enter
     * @return Returns itself 
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public <E> T fill(String elementTitle, String text) throws PageException {
        E element = getElement(elementTitle);
        Environment.getPageActions().fill(element, text);
        return (T) this;
    }

    /**
     * Click specified element
     *
     * @param <E> element type
     * @param elementTitle title of the element to click
     * @return Returns itself 
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public <E> T click(String elementTitle) throws PageException {
        E element = getElement(elementTitle);
        Environment.getPageActions().click(element);
        return (T) this;
    }

    /**
     * Press key on keyboard
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     * @return Returns itself 
     */
    public T pressKey(String keyName) {
        Environment.getPageActions().press(null, keyName);
        return (T) this;
    }

    /**
     * Press key on keyboard with focus on specified element
     *
     * @param <E> element type
     * @param keyName name of the key. See available key names in {@link Keys}
     * @param elementTitle title of element that accepts key commands
     * @return Returns itself 
     * @throws PageException if couldn't find element with required title
     */
    public <E> T pressKey(String keyName, String elementTitle) throws PageException {
        E element = getElement(elementTitle);
        Environment.getPageActions().press(element, keyName);
        return (T) this;
    }

    /**
     * Select specified option in select-element
     *
     * @param <E> element type
     * @param elementTitle element that is supposed to be selectable
     * @param option option to select
     * @return Returns itself 
     * @throws PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    public <E> T select(String elementTitle, String option) throws PageException {
        E element = getElement(elementTitle);
        Environment.getPageActions().select(element, option);
        return (T) this;
    }

    /**
     * Set checkbox element to selected state
     *
     * @param <E> element type
     * @param elementTitle element that is supposed to represent checkbox
     * @return Returns itself 
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public <E> T setCheckBox(String elementTitle) throws PageException {
        E element = getElement(elementTitle);
        Environment.getPageActions().setCheckbox(element, true);
        return (T) this;
    }

    /**
     * Check that the element's value is equal with specified value
     *
     * @param <E> element type
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @return Returns itself 
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException 
     */
    public <E> T checkValueIsEqual(String elementTitle, String text) throws PageException {
        E element = getElement(elementTitle);
        if (!Environment.getPageChecks().checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is not equal with '" + text + "'");
        }
        return (T) this;
    }

    /**
     * Check that the element's value is not equal with specified value
     *
     * @param <E> element type
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @return Returns itself 
     * @throws PageException if current page wasn't initialized, or element with required title was not found
     */
    public <E> T checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        E element = getElement(elementTitle);
        if (Environment.getPageChecks().checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is equal with '" + text + "'");
        }
        return (T) this;
    }

    /**
     * Check that the element's value is not empty
     *
     * @param <E> element type
     * @param elementTitle title of the element to check
     * @return Returns itself 
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    public <E> T checkNotEmpty(String elementTitle) throws PageException {
        E element = getElement(elementTitle);
        if (Environment.getPageChecks().checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is empty");
        }
        return (T) this;
    }

    /**
     * Check that the element's value is empty
     *
     * @param <E> element type
     * @param elementTitle title of the element to check
     * @return Returns itself 
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    public <E> T checkEmpty(String elementTitle) throws PageException {
        E element = getElement(elementTitle);
        if (!Environment.getPageChecks().checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is not empty");
        }
        return (T) this;
    }

    /**
     * Element is focused
     *
     * @param element element to focus on
     * @return Returns itself 
     */
    public T isElementFocused(String element) {
        LOG.warn("Note that isElementFocused method is still an empty!");
        return (T) this;
    }

    /**
     * Current step will be replaced with steps of specified scenario
     *
     * @param fragmentName scenario name to insert instead of this step
     * @return Returns itself 
     * @throws ru.sbtqa.tag.pagefactory.exceptions.FragmentException 
     */
    public T userInsertsFragment(String fragmentName) throws FragmentException {
        throw new FragmentException("The fragment-needed step must be replaced, but this did not happened");
    }
    
    private <E> E getElement(String elementTitle) throws PageException {
        return Environment.getFindUtils().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
    }

    public T appearElement(String elementName) throws PageException {
        appearElement(PROPERTIES.getTimeout(), elementName);
        return (T) this;
    }
    
    public T appearElement(int timeout, String elementName) throws PageException {
        WebElement element = getElement(elementName);
        String message = format("The element '%s' has not appeared from the page within '%s' seconds.", elementName, timeout);
        Wait.visibility(element, message, timeout);
        return (T) this;
    }

    public T waitInvisibility(String elementName) throws PageException {
        waitInvisibility(PROPERTIES.getTimeout(), elementName);
        return (T) this;
    }
    
    public T waitInvisibility(int timeout, String elementName) throws PageException {
        WebElement element = getElement(elementName);
        String message = format("The element '%s' has not disappeared from the page within '%s' seconds.", elementName, timeout);
        Wait.invisibility(element, message, timeout);
        return (T) this;
    }

    public T waitChangeAttribute(String attribute, String elementName, String attributeValue) throws PageException {
        waitChangeAttribute(PROPERTIES.getTimeout(), attribute, elementName, attributeValue);
        return (T) this;
    }

    public T waitChangeAttribute(int timeout, String attribute, String elementName, String attributeValue) throws PageException {
        WebElement element = getElement(elementName);
        String message = format("Attribute '%s' of the element '%s' is not equal to '%s' within "
                + "'%s' seconds. The attribute value: %s", attribute, elementName, attributeValue, timeout, element.getAttribute(attribute));
        Wait.changeAttribute(element, attribute, attributeValue, message, timeout);
        return (T) this;
    }
    
    public T waitAttributeContains(String attribute, String elementName, Condition negation, String partAttributeValue) throws PageException {
        waitAttributeContains(PROPERTIES.getTimeout(), attribute, elementName, negation, partAttributeValue);
        return (T) this;
    }
    
    public T waitAttributeContains(int timeout, String attribute, String elementName, Condition negation, String partAttributeValue) throws PageException {
        boolean isPositive = negation.equals(Condition.POSITIVE);

        WebElement element = getElement(elementName);
        String message = format("After waiting, attribute '%s' of the element '%s' is " + (isPositive ? "not " : "")
                + "contains value '%s'. Attribute value: %s", attribute, elementName, partAttributeValue, element.getAttribute(attribute));

        if (isPositive) {
            Wait.attributeContains(element, attribute, partAttributeValue, message, timeout);
        } else {
            Wait.attributeNotContains(element, attribute, partAttributeValue, message, timeout);
        }
        return (T) this;
    }

    public T waitElementContainsText(String elementName, Condition negation, String text) throws PageException {
        waitElementContainsText(PROPERTIES.getTimeout(), elementName, negation, text);
        return (T) this;
    }

    public T waitElementContainsText(int timeout, String elementName, Condition negation, String text) throws PageException {
        boolean isPositive = negation.equals(Condition.POSITIVE);
        WebElement element = getElement(elementName);
        String message = format("After waiting, text of the element '%s' is " + (isPositive ? "not " : "") 
                + "contains value '%s'. Text of the element: %s", elementName, text, element.getText());

        if (isPositive) {
            Wait.textContains(element, text, message, timeout);
        } else {
            Wait.textNotContains(element, text, message, timeout);
        }
        return (T) this;
    }
    
    public T waitClickability(String elementName) throws PageException {
        waitClickability(PROPERTIES.getTimeout(), elementName);
        return (T) this;
    }

    public T waitClickability(int timeout, String elementName) throws PageException {
        WebElement element = getElement(elementName);
        String message = format("The element '%s' didn't become clickable within %s seconds", elementName, timeout);
        Wait.clickable(element, message, timeout);
        return (T) this;
    }
}
