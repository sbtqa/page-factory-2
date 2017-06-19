package ru.sbtqa.tag.pagefactory.htmlstepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.util.PageFactoryUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.*;

import static ru.sbtqa.tag.pagefactory.PageReflectUtil.*;
import static ru.sbtqa.tag.pagefactory.util.PageFactoryUtils.getElementByTitle;

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
public class HTMLStepDefs {

    /**
     * Execute action with no parameters inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @throws PageInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     * @throws NoSuchElementException if block with given name couldn't be found
     */
    @And("ru.sbtqa.tag.pagefactory.userActionInBlockNoParams")
    public void userActionInBlockNoParams(String block, String action) throws PageInitializationException,
            NoSuchMethodException, NoSuchElementException {
        executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action);
    }

    /**
     * Execute action with parameters taken from specified {@link DataTable}
     * inside block element User|he keywords are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param dataTable table of parameters
     * @throws PageInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     * @throws NoSuchElementException if block with given name couldn't be found
     */
    @And("ru.sbtqa.tag.pagefactory.userActionInBlockTableParam")
    public void userActionInBlockTableParam(String block, String action, DataTable dataTable) throws PageInitializationException, NoSuchMethodException {
        executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, dataTable);
    }
    
    
    
    /**
     * Execute action with one parameter inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param param parameter
     * @throws PageInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     * @throws NoSuchElementException if block with given name couldn't be found
     */
    @And("ru.sbtqa.tag.pagefactory.userActionInBlockOneParam")
    public void userActionInBlockOneParam(String block, String action, String param) throws PageInitializationException, NoSuchMethodException {
        executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, param);
    }

    /**
     * Execute action with two parameters inside block element User|he keywords
     * are optional
     *
     * @param block path or name of the block
     * @param action title of the action to execute
     * @param param1 first parameter
     * @param param2 second parameter
     * @throws PageInitializationException if current page is not initialized
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     * @throws NoSuchElementException if block with given name couldn't be found
     */
    @And("ru.sbtqa.tag.pagefactory.userActionInBlockTwoParams")
    public void userActionInBlockTwoParams(String block, String action, String param1, String param2) throws PageInitializationException, NoSuchMethodException {
        executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, param1, param2);
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
    @And("ru.sbtqa.tag.pagefactory.findElementInBlock")
    public void findElementInBlock(String block, String elementType, String elementTitle) throws PageException {
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
            case "радиобатон":
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
        findElementInBlockByTitle(PageContext.getCurrentPage(), block, elementTitle, clazz);
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
    @And("ru.sbtqa.tag.pagefactory.findElementInList")
    public void findElementInList(String listTitle, String value) throws PageException {
        boolean found = false;
        for (WebElement webElement : findListOfElements(PageContext.getCurrentPage(), listTitle)) {
            if (webElement.getText().equals(value)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AutotestError(String.format("ru.sbtqa.tag.pagefactory.Element with text '%s' is absent in list '%s'", value, listTitle));
        }
    }
    
    /**
     * Find web element with corresponding title, if it is a check box, select
     * it If it's a WebElement instance, check whether it is already selected,
     * and click if it's not Add corresponding parameter to allure report
     *
     * @param elementTitle WebElement that is supposed to represent checkbox
     *
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if page was not initialized, or required element
     *                                                           couldn't be found
     */
    @And("ru.sbtqa.tag.pagefactory.select.checkBox")
    public void setCheckBox(String elementTitle) throws PageException {
        WebElement targetElement = getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        if (targetElement.getClass().isAssignableFrom(CheckBox.class)) {
            ((CheckBox) targetElement).select();
        } else {
            setCheckBoxState(targetElement, true);
        }
        PageFactoryUtils.addToReport(elementTitle, " is checked");
    }
    
    /**
     * Check whether specified element is selected, if it isn't, click it
     * isSelected() doesn't guarantee correct behavior if given element is not a
     * selectable (checkbox,dropdown,radiobtn)
     *
     * @param webElement a WebElemet object.
     * @param state a boolean object.
     */
    private void setCheckBoxState(WebElement webElement, Boolean state) {
        if (null != state) {
            if (webElement.isSelected() != state) {
                webElement.click();
            }
        }
    }
    
}
