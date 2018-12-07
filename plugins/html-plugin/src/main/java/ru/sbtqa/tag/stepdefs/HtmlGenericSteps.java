package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.*;

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
 *
 *
 *
 *
 * Step Definitions for html-plugin.
 * Common action with pages describes by html-elements.
 */
public class HtmlGenericSteps<T extends HtmlGenericSteps<T>> extends WebGenericSteps<T> {

    public HtmlGenericSteps() {
        HtmlSetupSteps.initHtml();
    }

    /**
     * Execute action with no parameters inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    public T actionInBlock(String block, String action) throws NoSuchMethodException {
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action);
        return (T) this;
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
    public T actionInBlock(String block, String action, DataTable dataTable) throws NoSuchMethodException {
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, dataTable);
        return (T) this;
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
    public T actionInBlock(String block, String action, String... param) throws NoSuchMethodException {
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, param);
        return (T) this;
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
    public T find(String block, String elementType, String elementTitle) throws PageException {
        Class<? extends WebElement> clazz;
        switch (elementType) {
            case "element":
            case "элемент":
                clazz = WebElement.class;
                break;
            case "textinput":
            case "текстовое поле":
                clazz = TextInput.class;
                break;
            case "checkbox":
            case "чекбокс":
                clazz = CheckBox.class;
                break;
            case "radiobutton":
            case "радиокнопка":
                clazz = Radio.class;
                break;
            case "table":
            case "таблицу":
                clazz = Table.class;
                break;
            case "header":
            case "заголовок":
                clazz = TextBlock.class;
                break;
            case "button":
            case "кнопку":
                clazz = Button.class;
                break;
            case "link":
            case "ссылку":
                clazz = Link.class;
                break;
            case "image":
            case "изображение":
                clazz = Image.class;
                break;
            default:
                clazz = WebElement.class;
        }
        ((HtmlReflection) Environment.getReflection()).findElementInBlockByTitle(PageContext.getCurrentPage(), block, elementTitle, clazz);
        return (T) this;
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
    public T find(String listTitle, String value) throws PageException {
        boolean found = false;
        for (WebElement webElement : ((HtmlReflection) Environment.getReflection()).findListOfElements(PageContext.getCurrentPage(), listTitle)) {
            if (webElement.getText().equals(value)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AutotestError(String.format("Element with text '%s' is absent in list '%s'", value, listTitle));
        }
        return (T) this;
    }
}
