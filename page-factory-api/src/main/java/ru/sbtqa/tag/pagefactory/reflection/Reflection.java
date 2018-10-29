package ru.sbtqa.tag.pagefactory.reflection;

import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Tag;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitles;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.annotations.ValidationRule;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;

public interface Reflection {

    /**
     * Search for the given Object in page repository storage, that is being
     * generated during preconditions to all features. If element is found, return
     * its title annotation. If nothing found, log debug message and return
     * toString() of corresponding element
     *
     * @param element Object to search
     * @param page page for searching
     * @return title of the given element
     */
    String getElementTitle(Page page, Object element);

    /**
     * Find method with corresponding title on current page, and execute it
     *
     * @param page the page on which the method is executing
     * @param title title of the method to call
     * @param param parameters that will be passed to method
     * @throws java.lang.NoSuchMethodException if required method couldn't be
     * found
     */
    void executeMethodByTitle(Page page, String title, Object... param) throws NoSuchMethodException;

    /**
     * Return a list of methods declared tin the given class and its super
     * classes
     *
     * @param clazz class to check
     * @return list of methods. could be empty list
     */
    List<Method> getDeclaredMethods(Class clazz);

    /**
     * Check whether given method has {@link ActionTitle} or
     * {@link ActionTitles} annotation with required title
     *
     * @param method method to check
     * @param title required title
     * @return true|false
     */
    Boolean isRequiredAction(Method method, final String title);

    /**
     * Find specified Object by title annotation among current page fields
     *
     * @param page the page on which the method is executing
     * @param title title of the element to search
     * @param <T> supposed type of the field. if field cannot be cast into this type, it will fail
     * @return Object found by corresponding title
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if failed to
     * find corresponding element or element type is set incorrectly
     */
    <T extends Object> T getElementByTitle(Page page, String title) throws PageException;


    /**
     * Check whether {@link ElementTitle} annotation of the field has a
     * required value
     *
     * @param field field to check
     * @param title value of ElementTitle annotation of required element
     * @return true|false
     */
    boolean isRequiredElement(Field field, String title);

    /**
     * Return value of {@link ElementTitle} annotation for the field. If
     * none present, return empty string
     *
     * @param field field to check
     * @return either an element title, or an empty string
     */
    String getFieldTitle(Field field);

    /**
     * Get object from a field of specified parent
     *
     * @param parentObject object that contains(must contain) given field
     * @param field field to get
     * @param <T> supposed type of the field. if field cannot be cast into
     * this type, it will fail
     * @return element of requested type
     * @throws ElementDescriptionException in case if field does not belong
     * to the object, or element could not be cast to specified type
     */
    @SuppressWarnings("unchecked")
    <T> T getElementByField(Object parentObject, Field field) throws ElementDescriptionException;

    /**
     * Find a method with {@link ValidationRule} annotation on current page, and
     * call it
     *
     * @param page the page on which the method is executing
     * @param title title of the validation rule
     * @param params parameters passed to called method
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if couldn't
     * find corresponding validation rule
     */
    void fireValidationRule(Page page, String title, Object... params) throws PageException;

    /**
     * TODO
     * @param scenarioDefinition
     * @return
     */
    List<Tag> getScenarioTags(ScenarioDefinition scenarioDefinition);
}
