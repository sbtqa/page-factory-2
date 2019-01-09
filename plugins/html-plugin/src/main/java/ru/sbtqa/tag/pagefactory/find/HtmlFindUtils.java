package ru.sbtqa.tag.pagefactory.find;

import static java.lang.String.format;
import static ru.sbtqa.tag.pagefactory.find.ComplexElement.ELEMENT_SEPARATOR;
import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException;
import ru.sbtqa.tag.pagefactory.exception.ElementSearchError;
import ru.sbtqa.tag.pagefactory.exception.IncorrectElementTypeError;
import ru.sbtqa.tag.pagefactory.utils.HtmlElementUtils;
import static ru.sbtqa.tag.pagefactory.utils.HtmlElementUtils.createElementWithCustomType;
import ru.sbtqa.tag.pagefactory.utils.Wait;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;
import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.isHtmlElement;
import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.isTypifiedElement;

public class HtmlFindUtils extends FindUtils {

    @Override
    public <T> T getElementByTitle(Page page, String title) {
        return find(title);
    }
    
    protected boolean isNumeric(String text){
       return text.chars().allMatch(Character::isDigit);
    }

    /**
     * Gets an element by its name or path. The element is assumed to be on the
     * current page
     * <p>
     * As a path, an enumeration of elements in a nested structure can be used
     * through the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block{@code ->}Element
     * <li> element in the list with the sequence number 3: List{@code ->}3
     * </ul>
     * <p>
     * If an element is found, it will be expected to appear on the page
     *
     * @param <T> type of the returned element
     * @param name name of the element to be searched
     * @return Returns an element from a page by name or path
     */
    public <T extends WebElement> T find(String name) {
        return find(name, true);
    }

    /**
     * Gets an element by its name or path with the ability to specify whether
     * to expect this element to appear on the page or not. The element is
     * assumed to be on the current page
     * <p>
     * As a path, an enumeration of elements in a nested structure can be used
     * through the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block{@code ->}Element
     * <li> element in the list with the sequence number 3: List{@code ->}3
     * </ul>
     * <p>
     * @param <T> type of the returned element
     * @param name name of the element to be searched
     * @param wait to wait for the element to appear or not
     * @return Returns an element from a page by name or path
     */
    public <T extends WebElement> T find(String name, boolean wait) {
        return find(null, name, wait);
    }

    /**
     * Gets an element by its name or path in the specified context
     * <p>
     * As a path, an enumeration of elements in a nested structure can be used
     * through the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block {@code ->} Element
     * <li> element in the list with the sequence number 3: List {@code ->} 3
     * </ul>
     * <p>
     *
     * @param <T> type of the returned element
     * @param context current context - where to start searching for an element.
     * Specify {@code null} if you need to perform a search in the context of
     * the current page, or an element to search for
     * @param name name of the element to be searched
     * @param wait to wait for the element to appear or not
     * @return Returns an element from a page by name or path
     */
    public <T extends WebElement> T find(T context, String name, boolean wait) {
        ComplexElement element = findAndCheckComplexElement(context, name, wait);
        if (element.getElement() == null) {
           throw new ElementSearchError(format("Element not found '%s' on page", name));
        }
        return (T) element.getElement();
    }
    
    protected <T extends WebElement> ComplexElement findAndCheckComplexElement(T context, String name, boolean wait) {
        ComplexElement element = findComplexElement(context, name, wait);

        if (element.getElement() == null) {
            if (!(wait || element.isPresent())) {
                throw new IllegalStateException(format("Element not found '%s' on page", name));
            }
        }
        if (wait) {
            Wait.visibility(HtmlElementUtils.getWebElement(element.getElement()),
                    "The page does not display the element " + formErrorMessage(element));
        }
        return element;
    }

    /**
     * Gets a list of elements by name or path
     * <p>
     * As a path, an enumeration of elements in a nested structure can be used
     * through the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block{@code ->}Element
     * <li> element in the list with the sequence number 3: List{@code ->}3
     * </ul>
     * <p>
     *
     * @param <T> type of the returned element
     * @param name list name or path to it
     * @return Returns a list from a page by name or path
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException
     */
    public <T extends WebElement> List<T> findList(String name) throws ElementDescriptionException {
        return findList(null, name);
    }

    /**
     * Gets a list of elements by name or path
     * <p>
     * As a path, an enumeration of elements in a nested structure can be used
     * through the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block{@code ->}Element
     * <li> element in the list with the sequence number 3: List{@code ->}3
     * </ul>
     * <p>
     *
     * @param <T> type of the returned element
     * @param context current context - where to start searching for an element.
     * Specify {@code null} if you need to perform a search in the context of
     * the current page, or an element to search for
     * @param name list name or path to it
     * @return Returns a list from a page by name or path
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDescriptionException Error get element by field
     */
    public <T extends WebElement> List<T> findList(T context, String name) throws ElementDescriptionException {
        ComplexElement element = new ComplexElement(context, name, false);
        Field field;

        if (name.contains(ELEMENT_SEPARATOR)) {
            int lastSeparatorIndex = name.lastIndexOf(ELEMENT_SEPARATOR);

            element = findComplexElement(context, name.substring(0, lastSeparatorIndex), false);
            String listName = name.substring(lastSeparatorIndex + ELEMENT_SEPARATOR.length(), name.length());

            field = getField(element.getElement(), listName);
        } else {
            field = getField(context, name);
        }
        if (field == null) {
            throw new ElementSearchError("Element list not found");
        }

        if (!field.getType().isAssignableFrom(List.class)) {
            throw new IncorrectElementTypeError(format("The element was found, "
                    + "but it is not a list. The search was performed along the way: %s", name));
        }
        return (List<T>) getElementByField(element, field);
    }

    private Object getElementByField(ComplexElement element, Field field) throws ElementDescriptionException {
        WebElement currentElement = element.getElement();
        Object currentContext = currentElement == null ? PageContext.getCurrentPage() : currentElement;
        return Environment.getReflection().getElementByField(currentContext, field);
    }

    private <T extends WebElement> ComplexElement findComplexElement(T context, String name, boolean wait) {
        ComplexElement element = new ComplexElement(context, name, wait);
        try {
            for (; element.getCurrentPosition() < element.getElementPath().size();
                    element.setCurrentPosition(element.getCurrentPosition() + 1)) {
                Field field = getField(element.getElement(), element.getCurrentName());

                if (field == null) {
                    throw new ElementSearchError("No element declared on page " + formErrorMessage(element));
                }
                findElement(field, element);
            }
            if (element.getCurrentPosition() > 0) {
                element.setCurrentPosition(element.getCurrentPosition() - 1);
            }
        } catch (AutotestError | IllegalArgumentException | ElementDescriptionException ex) {
            throw new ElementSearchError("Element " + formErrorMessage(element), ex);
        }
        return element;
    }

    private String formErrorMessage(ComplexElement element) {
        StringBuilder errorMessage = new StringBuilder();
        String currenElementName = "\"" + element.getCurrentName() + "\"";

        if (element.getElementPath().size() > 1) {
            if (currenElementName.chars().allMatch(Character::isDigit)) {
                String prevElement = element.getElementPath().get(element.getCurrentPosition() - 1).toString();
                String elementOflistNotFound = format("with number '%s' not found in list: %s", currenElementName, prevElement);
                errorMessage.append(elementOflistNotFound);
            } else {
                errorMessage.append(currenElementName);
            }
            errorMessage.append(". The search was performed by path: ").append(element.getFullElementPath());
        } else {
            errorMessage.append(currenElementName);
        }
        return errorMessage.toString();
    }

    /**
     * Searches for an element of the specified type by the specified name or
     * path
     * <p>
     * As a path, an enumeration of elements in a nested structure can be used
     * through the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block{@code ->}Element
     * <li> element in the list with the sequence number 3: List{@code ->}3
     * </ul>
     * <p>
     * If an element on the page is declared as {@code WebElement}, then an
     * attempt will be made to convert it to the passed type if this is possible
     * <p>
     * By default, expects an element to appear on the page
     *
     * @param <T> expected element type
     * @param name element name or path
     * @param type element type
     * @return Returns an element from a page by name or path
     */
    public <T extends WebElement> T find(String name, Class<T> type) {
        return find(name, type, true);
    }

    /**
     * Searches for an element of the specified type by the specified name or
     * path 
     * <p>
     * As a path, an enumeration of elements in a nested structure can be used
     * through the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block {@code ->} Element
     * <li> element in the list with the sequence number 3: List {@code ->} 3
     * </ul>
     * <p>
     * If an element on the page is declared as {@code WebElement}, then an
     * attempt will be made to convert it to the passed type if this is possible
     *
     * @param <T> expected element type
     * @param name element name or path
     * @param type element type
     * @param wait to wait for the element to appear or not
     * @return Returns an element from a page by name or path
     */
    public <T extends WebElement> T find(String name, Class<T> type, boolean wait) {
        Class instanceType = type;
        T element = find(name, wait);
        Class elementType = element.getClass();

        if (isTypifiedElement(elementType) || isHtmlElement(elementType)) {
            if (!type.isAssignableFrom(elementType)) {
                throw new IncorrectElementTypeError(format("Found component named '%s', "
                        + "but his type does not meet the required. "
                        + "Expected: %s. Found: %s", name, type.getName(), elementType.getName()));
            }
        } else if (!(type.equals(WebElement.class) || isHtmlElement(elementType))) {
            return (T) createElementWithCustomType(instanceType, element);
        }
        return element;
    }

    /**
     * Searches for the name of the element or the path in the specified context
     * <p>
     * The path can be an enumeration of elements in a nested structure through
     * the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block{@code ->}Element
     * <li> element in the list with the sequence number 3: List{@code ->}3
     * </ul>
     * <p>
     * By default, expects an element to appear on the page
     *
     * @param <T> expected type of the returned element
     * @param name element name or path to it
     * @param clazz list of valid types. If the element found does not match the
     * valid type, an exception will be thrown
     * @return Returns an element from a page by name or path
     */
    public <T extends WebElement> T find(String name, List<Class> clazz) {
        return find(name, clazz, true);
    }

    /**
     * Searches for the name of the element or the path in the specified context
     * <p>
     * The path can be an enumeration of elements in a nested structure through
     * the separator "{@code ->}", for example:
     * <ul>
     * <li> element in the block: Block{@code ->}Element
     * <li> element in the list with the sequence number 3: List{@code ->}3
     * </ul>
     *
     * @param <T> expected type of the returned element
     * @param name element name or path to it
     * @param clazz list of valid types. If the element found does not match the
     * valid type, an exception will be thrown
     * @param wait to wait for the element to appear or not
     * @return Returns an element from a page by name or path
     */
    public <T extends WebElement> T find(String name, List<Class> clazz, boolean wait) {
        T element = find(name, wait);
        if (!clazz.contains(element.getClass())) {
            throw new AutotestError(format("Uncorrected element type. Element: %s. Valid types: %s", name, clazz.toString()));
        }
        return element;
    }

    private void findElement(Field field, ComplexElement element) throws ElementDescriptionException {
        Object fieldObject = getElementByField(element, field);

        if (field.getType().isAssignableFrom(List.class)) {
            findElementOfList((List<WebElement>) fieldObject, element);
        } else {
            element.setElement((WebElement) fieldObject);
        }
    }

    private Field getField(Object element, String elementName) {
        Page page = PageContext.getCurrentPage();
        Class clazz = element == null ? page.getClass() : element.getClass();

        for (Field field : FieldUtilsExt.getFieldsListWithAnnotation(clazz, ElementTitle.class)) {
            field.setAccessible(true);
            if (field.getAnnotation(ElementTitle.class).value().equals(elementName)) {
                return field;
            }
        }
        return null;
    }

    protected static void findElementOfList(List<WebElement> list, ComplexElement currentElement) {
        int position = currentElement.getCurrentPosition();

        int index = 0;
        List<String> elementPath = currentElement.getElementPath();

        if (position + 1 < elementPath.size()) {
            String nextParam = elementPath.get(position + 1);
            boolean isNumeric = nextParam.chars().allMatch(Character::isDigit);
            if (isNumeric) {
                index = Integer.parseInt(elementPath.get(position + 1)) - 1;
                currentElement.setCurrentPosition(position + 1);
            } else {
                LOG.info("The parameter following the list is not an index. Index list element: 0.");
            }
        }
        if (index >= list.size()) {
            boolean isLastElement = currentElement.getCurrentPosition() + 1 != currentElement.getElementPath().size();
            if (!isLastElement && currentElement.isWaitAppear()) {
                throw new AutotestError(format("Index element in list "
                        + "exceeds list size. Index: %s. Size: %s", index, list.size()));
            } else {
                currentElement.setElement(null);
            }
        } else {
            LOG.debug("Element with index {} was received.", index);
            currentElement.setElement(list.get(index));
        }
    }
}
