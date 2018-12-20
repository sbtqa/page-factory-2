package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import java.util.List;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.find.FindUtils;
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
 * @param <T>
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class CoreGenericSteps<T extends CoreGenericSteps<T>> {

    private static final Logger LOG = LoggerFactory.getLogger(CoreGenericSteps.class);

    public CoreGenericSteps() {
        CoreSetupSteps.preSetUp();
        CoreSetupSteps.setUp(null);
    }

    /**
     * Initialize a page with corresponding title (defined via
     * {@link ru.sbtqa.tag.pagefactory.annotations.PageEntry} annotation)
     * User|he keywords are optional
     *
     * @param title of the page to initialize
     * @return 
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
     * @return 
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
     * @return 
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
     * @return 
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
     * @return 
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
     * @return 
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public T action(String action, List<String> list) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, list);
        return (T) this;
    }

    /**
     * Fill specified element with text
     *
     * @param elementTitle element to fill
     * @param text text to enter
     * @return 
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public T fill(String elementTitle, String text) throws PageException {
        Object element = getElement(elementTitle);
        Environment.getPageActions().fill(element, text);
        return (T) this;
    }

    /**
     * Click specified element
     *
     * @param elementTitle title of the element to click
     * @return 
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public T click(String elementTitle) throws PageException {
        Object element = getElement(elementTitle);
        Environment.getPageActions().click(element);
        return (T) this;
    }

    /**
     * Press key on keyboard
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     * @return 
     */
    public T pressKey(String keyName) {
        Environment.getPageActions().press(null, keyName);
        return (T) this;
    }

    /**
     * Press key on keyboard with focus on specified element
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     * @param elementTitle title of element that accepts key commands
     * @return 
     * @throws PageException if couldn't find element with required title
     */
    public T pressKey(String keyName, String elementTitle) throws PageException {
        Object element = getElement(elementTitle);
        Environment.getPageActions().press(element, keyName);
        return (T) this;
    }

    /**
     * Select specified option in select-element
     *
     * @param elementTitle element that is supposed to be selectable
     * @param option option to select
     * @return 
     * @throws PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    public T select(String elementTitle, String option) throws PageException {
        Object element = getElement(elementTitle);
        Environment.getPageActions().select(element, option);
        return (T) this;
    }

    /**
     * Set checkbox element to selected state
     *
     * @param elementTitle element that is supposed to represent checkbox
     * @return 
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public T setCheckBox(String elementTitle) throws PageException {
        Object element = getElement(elementTitle);
        Environment.getPageActions().setCheckbox(element, true);
        return (T) this;
    }

    /**
     * Check that the element's value is equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @return 
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException 
     */
    public T checkValueIsEqual(String elementTitle, String text) throws PageException {
        Object element = getElement(elementTitle);
        if (!Environment.getPageChecks().checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is not equal with '" + text + "'");
        }
        return (T) this;
    }

    /**
     * Check that the element's value is not equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @return 
     * @throws PageException if current page wasn't initialized, or element with required title was not found
     */
    public T checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        Object element = getElement(elementTitle);
        if (Environment.getPageChecks().checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is equal with '" + text + "'");
        }
        return (T) this;
    }

    /**
     * Check that the element's value is not empty
     *
     * @param elementTitle title of the element to check
     * @return 
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    public T checkNotEmpty(String elementTitle) throws PageException {
        Object element = getElement(elementTitle);
        if (Environment.getPageChecks().checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is empty");
        }
        return (T) this;
    }

    /**
     * Check that the element's value is empty
     *
     * @param elementTitle title of the element to check
     * @return 
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    public T checkEmpty(String elementTitle) throws PageException {
        Object element = getElement(elementTitle);
        if (!Environment.getPageChecks().checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is not empty");
        }
        return (T) this;
    }

    /**
     * Element is focused
     *
     * @param element element to focus on
     * @return 
     */
    public T isElementFocused(String element) {
        LOG.warn("Note that isElementFocused method is still an empty!");
        return (T) this;
    }

    /**
     * Current step will be replaced with steps of specified scenario
     *
     * @param fragmentName scenario name to insert instead of this step
     * @return 
     * @throws ru.sbtqa.tag.pagefactory.exceptions.FragmentException 
     */
    public T userInsertsFragment(String fragmentName) throws FragmentException {
        throw new FragmentException("The fragment-needed step must be replaced, but this did not happened");
    }
    
    private Object getElement(String elementTitle) throws PageException {
        return Environment.getFindUtils().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
    }
            
}
