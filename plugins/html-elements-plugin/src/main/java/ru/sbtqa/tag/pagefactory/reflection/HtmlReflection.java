package ru.sbtqa.tag.pagefactory.reflection;

import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

public interface HtmlReflection extends Reflection {

    /**
     * Check whether given field is a child of specified class
     *
     * @param parent class that is supposed to be parent
     * @param field field to check
     * @return true|false
     */
    boolean isChildOf(Class<?> parent, Field field);

    /**
     * Find element of required type in the specified block, or block chain on
     * the page. If blockPath is separated by &gt; delimiters, it will be
     * treated as a block path, so element could be found only in the described
     * chain of blocks. Otherwise, given block will be searched recursively on
     * the page
     *
     * @param <T> supposed type of the field. if field cannot be cast into
     * this type, it will fail
     * @param page the page on which the method will be executed
     * @param blockPath block or block chain where element will be searched
     * @param title value of ElementTitle annotation of required element
     * @param type type of the searched element
     * @return web element of the required type
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException if
     * element was not found
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException if element was not found, but with the wrong type
     */
    <T extends WebElement> T findElementInBlockByTitle(Page page, String blockPath, String title, Class<T> type)
            throws PageException;

    /**
     * Acts exactly like
     * {@link HtmlReflection#findElementInBlockByTitle(Page, String, String, Class)}, but return a
     * WebElement instance. It still could be casted to HtmlElement and
     * TypifiedElement any class that extend them.
     *
     * @param page the page on which the method will be executed
     * @param blockPath block or block chain where element will be searched
     * @param title value of ElementTitle annotation of required element
     * @return WebElement
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException if
     * element was not found
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException if element was not found, but with the wrong type
     */
    WebElement findElementInBlockByTitle(Page page, String blockPath, String title) throws PageException;

    /**
     * Finds elements list in context of current page See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)}  for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param listTitle value of ElementTitle annotation of required element
     * @param type type of elements in list that is being searched for
     * @param <T> type of elements in returned list
     * @return list of elements of particular type
     * @throws PageException if nothing found or current page is not initialized
     */
    <T extends WebElement> List<T> findListOfElements(Page page, String listTitle, Class<T> type)
            throws PageException;

    /**
     * Find list of elements of the specified type with required title in
     * the given context. Context is either a page object itself, or a block
     * on the page. !BEWARE! field.get() will actually query browser to
     * evaluate the list, so this method might reduce performance!
     *
     * @param listTitle value of ElementTitle annotation of required element
     * @param type type of elements inside of the list
     * @param context object where search should be performed
     * @param <T> type of elements in returned list
     * @return list of WebElement's or its derivatives
     * @throws PageException if didn't find any list or current page wasn't
     * initialized
     */
    @SuppressWarnings("unchecked")
    <T extends WebElement> List<T> findListOfElements(String listTitle, Class<T> type, Object context)
            throws PageException;

    /**
     * Finds elements list in context of current page See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)} for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param listTitle value of ElementTitle annotation of required element
     * @return list of WebElement's
     * @throws PageException if nothing found or current page is not initialized
     */
    List<WebElement> findListOfElements(Page page, String listTitle) throws PageException;


    /**
     * Find elements list in context of required block See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)} for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @param listTitle value of ElementTitle annotation of required element
     * @param type type of elements in list that is being searched for
     * @param <T> type of elements in returned list
     * @return list of elements of particular type
     * @throws PageException if nothing found or current page is not initialized
     */
    <T extends WebElement> List<T> findListOfElementsInBlock(Page page, String blockPath, String listTitle, Class<T> type)
            throws PageException;

    /**
     * Finds elements list in context of required block See
     * ${@link HtmlReflection#findListOfElements(String, Class, Object)} for detailed
     * description
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @param listTitle value of ElementTitle annotation of required element
     * @return list of WebElement's
     * @throws PageException if nothing found or current page is not initialized
     */
    List<WebElement> findListOfElementsInBlock(Page page, String blockPath, String listTitle) throws PageException;

    /**
     * See {@link HtmlReflection#findBlocks(String, Object, boolean)} for description.
     * This wrapper finds only one block. Search is being performed on a current
     * page
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @return first found block object
     * @throws java.util.NoSuchElementException if couldn't find any block
     */
    HtmlElement findBlock(Page page, String blockPath) throws NoSuchElementException;

    /**
     * See {@link HtmlReflection#findBlocks(String, Object, boolean)} for description.
     * Search is being performed on a current page
     *
     * @param page the page on which the method will be executed
     * @param blockPath full path or just a name of the block to search
     * @return list of objects that were found by specified path
     */
    List<HtmlElement> findBlocks(Page page, String blockPath) throws NoSuchElementException;

    /**
     * Execute parameter-less method inside of the given block element.
     *
     * @param page the page on which the method will be executed
     * @param block block title, or a block chain string separated with '-&gt;'
     * symbols
     * @param actionTitle title of the action to execute
     * @throws NoSuchMethodException if required method couldn't be
     * found
     */
    void executeMethodByTitleInBlock(Page page, String block, String actionTitle) throws NoSuchMethodException;

    /**
     * Execute method with one or more parameters inside of the given block
     * element !BEWARE! If there are several elements found by specified block
     * path, a first one will be used!
     *
     * @param page the page on which the method will be executed
     * @param blockPath block title, or a block chain string separated with
     * '-&gt;' symbols
     * @param actionTitle title of the action to execute
     * @param parameters parameters that will be passed to method
     * @throws NoSuchMethodException if required method couldn't be
     * found
     */
    void executeMethodByTitleInBlock(Page page, String blockPath, String actionTitle, Object... parameters) throws NoSuchMethodException;

    /**
     * Find specified TypifiedElement by title annotation among current page
     * fields
     *
     * @param <T> supposed type of the field. if field cannot be cast into this type, it will fail
     * @param page the page on which the method will be executed
     * @param title a {@link String} object.
     * @return a {@link org.openqa.selenium.WebElement} object.
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if nothing found or current page is not initialized
     */
    @SuppressWarnings(value = "unchecked")
    <T extends TypifiedElement> T getTypifiedElementByTitle(Page page, String title) throws PageException;

    <T> T getElementByField(Object parentObject, Field field) throws ElementDescriptionException;
}
