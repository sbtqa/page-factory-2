package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.FragmentException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.PageInitializationException;
import ru.sbtqa.tag.pagefactory.exceptions.WaitException;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
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
public class HtmlSteps extends WebSteps {

    private static HtmlSteps instance;

    public HtmlSteps() {
        HtmlSetupSteps.initHtml();
    }

    public static HtmlSteps getInstance() {
        if (instance == null) {
            instance = new HtmlSteps();
        }
        return instance;
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
    public void userActionInBlockNoParams(String block, String action) throws NoSuchMethodException {
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action);
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
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, dataTable);
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
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, param);
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
        ((HtmlReflection) Environment.getReflection()).executeMethodByTitleInBlock(PageContext.getCurrentPage(), block, action, param1, param2);
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
        ((HtmlReflection) Environment.getReflection()).findElementInBlockByTitle(PageContext.getCurrentPage(), block, elementTitle, clazz);
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
        for (WebElement webElement : ((HtmlReflection) Environment.getReflection()).findListOfElements(PageContext.getCurrentPage(), listTitle)) {
            if (webElement.getText().equals(value)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AutotestError(String.format("Element with text '%s' is absent in list '%s'", value, listTitle));
        }
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps openCopyPage() {
        return (HtmlSteps) super.openCopyPage();
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps switchesToNextTab() {
        return (HtmlSteps) super.switchesToNextTab();
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps urlMatches(String url) {
        return (HtmlSteps) super.urlMatches(url);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps closingCurrentWin(String title) {
        return (HtmlSteps) super.closingCurrentWin(title);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps backPage() {
        return (HtmlSteps) super.backPage();
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps goToUrl(String url) {
        return (HtmlSteps) super.goToUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps goToPageByUrl(String url) throws PageInitializationException {
        return (HtmlSteps) super.goToPageByUrl(url);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps reInitPage() {
        return (HtmlSteps) super.reInitPage();
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps acceptAlert(String text) throws WaitException {
        return (HtmlSteps) super.acceptAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps dismissAlert(String text) throws WaitException {
        return (HtmlSteps) super.dismissAlert(text);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps checkTextAppears(String text) throws WaitException {
        return (HtmlSteps) super.checkTextAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps checkTextIsNotPresent(String text) {
        return (HtmlSteps) super.checkTextIsNotPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps checkModalWindowAppears(String text) throws WaitException {
        return (HtmlSteps) super.checkModalWindowAppears(text);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlSteps checkElementWithTextIsPresent(String text) {
        return (HtmlSteps) super.checkElementWithTextIsPresent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps openPage(String title) throws PageInitializationException {
        return (HtmlSteps) super.openPage(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps action(String action) throws NoSuchMethodException {
        return (HtmlSteps) super.action(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps action(String action, String param) throws NoSuchMethodException {
        return (HtmlSteps) super.action(action, param);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps action(String action, String param1, String param2) throws NoSuchMethodException {
        return (HtmlSteps) super.action(action, param1, param2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps action(String action, String param1, String param2, String param3) throws NoSuchMethodException {
        return (HtmlSteps) super.action(action, param1, param2, param3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps action(String action, DataTable dataTable) throws NoSuchMethodException {
        return (HtmlSteps) super.action(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps action(String action, String param, DataTable dataTable) throws NoSuchMethodException {
        return (HtmlSteps) super.action(action, param, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps action(String action, List<String> list) throws NoSuchMethodException {
        return (HtmlSteps) super.action(action, list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps fill(String elementTitle, String text) throws PageException {
        return (HtmlSteps) super.fill(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps click(String elementTitle) throws PageException {
        return (HtmlSteps) super.click(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps pressKey(String keyName) {
        return (HtmlSteps) super.pressKey(keyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps pressKey(String keyName, String elementTitle) throws PageException {
        return (HtmlSteps) super.pressKey(keyName, elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps select(String elementTitle, String option) throws PageException {
        return (HtmlSteps) super.select(elementTitle, option);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps setCheckBox(String elementTitle) throws PageException {
        return (HtmlSteps) super.setCheckBox(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps checkValueIsEqual(String elementTitle, String text) throws PageException {
        return (HtmlSteps) super.checkValueIsEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps checkValueIsNotEqual(String elementTitle, String text) throws PageException {
        return (HtmlSteps) super.checkValueIsNotEqual(elementTitle, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps checkNotEmpty(String elementTitle) throws PageException {
        return (HtmlSteps) super.checkNotEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps checkEmpty(String elementTitle) throws PageException {
        return (HtmlSteps) super.checkEmpty(elementTitle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps isElementFocused(String element) {
        return (HtmlSteps) super.isElementFocused(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSteps userInsertsFragment(String fragmentName) throws FragmentException {
        return (HtmlSteps) super.userInsertsFragment(fragmentName);
    }
}
