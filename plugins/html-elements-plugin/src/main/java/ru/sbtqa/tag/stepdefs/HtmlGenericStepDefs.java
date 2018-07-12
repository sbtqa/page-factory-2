package ru.sbtqa.tag.stepdefs;

import java.util.Locale;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.html.utils.HtmlReflectionUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.i18n.I18N;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Image;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Radio;
import ru.yandex.qatools.htmlelements.element.Table;
import ru.yandex.qatools.htmlelements.element.TextBlock;
import ru.yandex.qatools.htmlelements.element.TextInput;

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
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */


/**
 * Step Definitions for html-elements-plugin.
 * Common action with pages describes by html-elements.
 */
public class HtmlGenericStepDefs extends HtmlSetupSteps{

    /**
     * Execute action with no parameters inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public void userActionInBlockNoParams(String block, String action) throws NoSuchMethodException {
        HtmlReflectionUtils.executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action);
    }

    /**
     * Execute action with parameters taken from specified {@link DataTable}
     * inside block element User|he keywords are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param dataTable table of parameters
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public void userActionInBlockTableParam(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        HtmlReflectionUtils.executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, dataTable);
    }

    /**
     * Execute action with one parameter inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param param parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public void userActionInBlockOneParam(String block, String action, String param) throws NoSuchMethodException {
        HtmlReflectionUtils.executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, param);
    }

    /**
     * Execute action with two parameters inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public void userActionInBlockTwoParams(String block, String action, String param1, String param2) throws NoSuchMethodException {
        HtmlReflectionUtils.executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, param1, param2);
    }

    /**
     * Find element inside given block. Element name itself is a parameter, and
     * defines type of the element to search for User|he keywords are optional
     *
     * @param block path or name of the block
     * @param elementType type of the searched element. Could be one of Yandex
     * element types types
     * @param elementTitle title of the element to search
     * @throws PageException if current page is not initialized, or element
     * wasn't found
     */
    public void findElementInBlock(String block, String elementType, String elementTitle) throws PageException {
        String[] packages = this.getClass().getCanonicalName().split("\\.");
        String currentLanguage = packages[packages.length - 2];
        I18N i18n = I18N.getI18n(this.getClass(), new Locale(currentLanguage));
        String key = i18n.getKey(elementType);
        Class<? extends WebElement> clazz;
        switch (key) {
            case "ru.sbtqa.tag.pagefactory.type.element":
                clazz = WebElement.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.textinput":
                clazz = TextInput.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.checkbox":
                clazz = CheckBox.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.radiobutton":
                clazz = Radio.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.table":
                clazz = Table.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.header":
                clazz = TextBlock.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.button":
                clazz = Button.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.link":
                clazz = Link.class;
                break;
            case "ru.sbtqa.tag.pagefactory.type.image":
                clazz = Image.class;
                break;
            default:
                clazz = WebElement.class;
        }
        HtmlReflectionUtils.findElementInBlockByTitle(PageContext.getCurrentPage(), block, elementTitle, clazz);
    }

    /**
     * Find element with given value in specified list User|he keywords are
     * optional
     *
     * @param listTitle title of the list to search for
     * @param value required value of the element. for text elements value is
     * being checked via getText() method
     * @throws PageException if page wasn't initialized of required list wasn't
     * found
     */
    public void findElementInList(String listTitle, String value) throws PageException {
        boolean found = false;
        for (WebElement webElement : HtmlReflectionUtils.findListOfElements(PageContext.getCurrentPage(), listTitle)) {
            if (webElement.getText().equals(value)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AutotestError(String.format("Element with text '%s' is absent in list '%s'", value, listTitle));
        }
    }

}
