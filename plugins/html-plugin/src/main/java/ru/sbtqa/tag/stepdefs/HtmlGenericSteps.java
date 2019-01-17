package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import io.qameta.allure.Step;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

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
 * @param <T> type of steps - any successor {@code HtmlGenericSteps}
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
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
     * @return Returns itself 
     * @throws NoSuchMethodException if corresponding method doesn't exist in
     * specified block
     */
    @Step("user in block \"{block}\" ({action})")
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
    @Step("user in block \"{block}\" ({action}) with the parameters of table {dataTable}")
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
    @Step("user in block \"{block}\" ({action}) with the parameters {param}")
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
    @Step("user in list \"{listTitle}\" finds the value element \"{value}\"")
    public T find(String listTitle, String value) throws PageException {
        List<WebElement> elements = ((HtmlFindUtils) Environment.getFindUtils()).findList(null, value);
        WebElement element = ElementUtils.getElementByTextWithEmptyResult(elements, value);
        Assert.assertNotNull(String.format("Element with text '%s' is absent in list '%s'", value, listTitle), element);
        return (T) this;
    }
}
