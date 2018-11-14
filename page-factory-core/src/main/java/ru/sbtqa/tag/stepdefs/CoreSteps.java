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
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class CoreSteps {

    private static final Logger LOG = LoggerFactory.getLogger(CoreSteps.class);

    private static CoreSteps instance;

    public CoreSteps() {
        CoreSetupSteps.preSetUp();
        CoreSetupSteps.setUp(null);
    }

    public static CoreSteps getInstance() {
        if (instance == null) {
            instance = new CoreSteps();

        }
        return instance;
    }

    /**
     * Initialize a page with corresponding title (defined via
     * {@link ru.sbtqa.tag.pagefactory.annotations.PageEntry} annotation)
     * User|he keywords are optional
     *
     * @param title of the page to initialize
     * @throws PageInitializationException if page initialization failed
     */
    public CoreSteps openPage(String title) throws PageInitializationException {
        PageManager.getPage(title);
        return this;
    }

    /**
     * Execute action with no parameters User|he keywords are optional
     *
     * @param action title of the action to execute
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreSteps action(String action) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action);
        return this;
    }

    /**
     * Execute action with one parameter User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreSteps action(String action, String param) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, param);
        return this;
    }

    /**
     * Execute action with two parameters User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreSteps action(String action, String param1, String param2) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, param1, param2);
        return this;
    }

    /**
     * Execute action with three parameters User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @param param3 third parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreSteps action(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, param1, param2, param3);
        return this;
    }

    /**
     * Execute action with parameters from given {@link DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param dataTable table of parameters
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreSteps action(String action, DataTable dataTable) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, dataTable);
        return this;
    }

    /**
     * Execute action with string parameter and {@link DataTable}
     * User|he keywords are optional
     *
     * @param action title of the action to execute
     * @param param parameter
     * @param dataTable table of parameters
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreSteps action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, param, dataTable);
        return this;
    }

    /**
     * Execute action with parameters taken from list User|he keywords are
     * optional
     *
     * @param action title of the action to execute
     * @param list parameters list
     * @throws NoSuchMethodException if corresponding method doesn't exist
     */
    public CoreSteps action(String action, List<String> list) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, list);
        return this;
    }

    /**
     * Fill specified element with text
     *
     * @param elementTitle element to fill
     * @param text text to enter
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public CoreSteps fill(String elementTitle, String text) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        Environment.getPageActions().fill(element, text);
        return this;
    }

    /**
     * Click specified element
     *
     * @param elementTitle title of the element to click
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public CoreSteps click(String elementTitle) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        Environment.getPageActions().click(element);
        return this;
    }

    /**
     * Press key on keyboard
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     */
    public CoreSteps pressKey(String keyName) {
        Environment.getPageActions().press(null, keyName);
        return this;
    }

    /**
     * Press key on keyboard with focus on specified element
     *
     * @param keyName name of the key. See available key names in {@link Keys}
     * @param elementTitle title of element that accepts key commands
     * @throws PageException if couldn't find element with required title
     */
    public CoreSteps pressKey(String keyName, String elementTitle) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        Environment.getPageActions().press(element, keyName);
        return this;
    }

    /**
     * Select specified option in select-element
     *
     * @param elementTitle element that is supposed to be selectable
     * @param option option to select
     * @throws PageException if required
     * element couldn't be found, or current page isn't initialized
     */
    public CoreSteps select(String elementTitle, String option) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        Environment.getPageActions().select(element, option);
        return this;
    }

    /**
     * Set checkbox element to selected state
     *
     * @param elementTitle element that is supposed to represent checkbox
     * @throws PageException if page was not initialized, or required element couldn't be found
     */
    public CoreSteps setCheckBox(String elementTitle) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        Environment.getPageActions().setCheckbox(element, true);
        return this;
    }

    /**
     * Check that the element's value is equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     */
    public CoreSteps checkValueIsEqual(String elementTitle, String text) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!Environment.getPageChecks().checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is not equal with '" + text + "'");
        }
        return this;
    }

    /**
     * Check that the element's value is not equal with specified value
     *
     * @param text value for comparison
     * @param elementTitle title of the element to search
     * @throws PageException if current page wasn't initialized, or element with required title was not found
     */
    public CoreSteps checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (Environment.getPageChecks().checkEquality(element, text)) {
            throw new AutotestError("'" + elementTitle + "' value is equal with '" + text + "'");
        }
        return this;
    }

    /**
     * Check that the element's value is not empty
     *
     * @param elementTitle title of the element to check
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    public CoreSteps checkNotEmpty(String elementTitle) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (Environment.getPageChecks().checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is empty");
        }
        return this;
    }

    /**
     * Check that the element's value is empty
     *
     * @param elementTitle title of the element to check
     * @throws PageException if current page was not initialized, or element wasn't found on the page
     */
    public CoreSteps checkEmpty(String elementTitle) throws PageException {
        Object element = Environment.getReflection().getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (!Environment.getPageChecks().checkEmptiness(element)) {
            throw new AutotestError("'" + elementTitle + "' value is not empty");
        }
        return this;
    }

    /**
     * Element is focused
     *
     * @param element element to focus on
     */
    public CoreSteps isElementFocused(String element) {
        LOG.warn("Note that isElementFocused method is still an empty!");
        return this;
    }

    /**
     * Current step will be replaced with steps of specified scenario
     *
     * @param fragmentName scenario name to insert instead of this step
     */
    public CoreSteps userInsertsFragment(String fragmentName) throws FragmentException {
        throw new FragmentException("The fragment-needed step must be replaced, but this did not happened");
    }
}
