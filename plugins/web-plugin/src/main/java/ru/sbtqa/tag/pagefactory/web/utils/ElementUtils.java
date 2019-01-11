package ru.sbtqa.tag.pagefactory.web.utils;

import static java.lang.String.format;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.ElementDisabledError;
import ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException;

public class ElementUtils {

    private ElementUtils() {
    }

    /**
     * Double-clicks an item
     *
     * @param element element element clicked
     */
    public static void doubleClick(WebElement element) {
        new Actions((WebDriver) Environment.getDriverService().getDriver()).doubleClick(element).perform();
    }

    /**
     * Checks that the list of elements contains a text value that is strictly
     * equal to the one passed
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements list of any heirs {@code WebElement}
     * @param text text search text
     * @return Returns {@code true} if the text is found in the list of items
     * and {@code false} if not found
     */
    public static <T extends WebElement> boolean isListContains(List<T> elements, String text) {
        return elements.stream()
                .map(T::getText).anyMatch(text::equals);
    }

    /**
     * Checks that the element attribute contains the passed value
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param element element - any successor {@code WebElement}
     * @param attribute attribute name of the element in the DOM structure
     * @param partialAttributeValue part of the attribute value that is needed
     *      * check
     * @return Returns {@code true} if the value is found in the element
     * attribute and {@code false} if not found
     */
    public static <T extends WebElement> boolean isElementAttributeContains(T element, String attribute, String partialAttributeValue) {
        return element.getAttribute(attribute).contains(partialAttributeValue);
    }

    /**
     * Checks that the element attribute is equal to the passed value.
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param element element - any successor {@code WebElement}
     * @param attribute attribute name of the element in the DOM structure
     * @param attributeValue attribute value to check
     * @return Returns {@code true} if the value passed in is equal to the
     * attribute value of the element and {@code false} if not
     */
    public static <T extends WebElement> boolean isElementAttributeEquals(T element, String attribute, String attributeValue) {
        return element.getAttribute(attribute).equals(attributeValue);
    }

    /**
     * Returns an element whose attribute is equal to the passed value.
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param attribute attribute name of the element in the DOM structure
     * @param attributeValue attribute value
     * @return Returns the element whose attribute is equal to the passed value.
     * If such an item is not found in the list, it will return {@code null}
     */
    public static <T extends WebElement> T getElementWithAttributeWithEmptyResult(List<T> elements, String attribute, String attributeValue) {
        return getElementByPredicateWithEmptyResult(elements, element -> element.getAttribute(attribute).equals(attributeValue));
    }

    /**
     * Returns an element whose attribute is equal to the passed value
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param attribute attribute name of the element in the DOM structure
     * @param attributeValue attribute value
     * @return Returns the element whose attribute is equal to the passed value
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException If
     * no such element is found in the list
     */
    public static <T extends WebElement> T getElementWithAttribute(List<T> elements, String attribute, String attributeValue) throws ElementNotFoundException {
        T element = getElementWithAttributeWithEmptyResult(elements, attribute, attributeValue);
        if (element == null) {
            throw new ElementNotFoundException(format("An element with an '%s' "
                    + "attribute value of '%s' is not found in the list of elements", attribute, attributeValue));
        }
        return element;
    }

    /**
     * Returns the element whose attribute contains the passed value.
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param attribute attribute name of the element in the DOM structure
     * @param partialAttributeValue attribute value
     * @return Returns the element whose attribute contains the passed value. If
     * such an item is not found in the list, it will return {@code null}
     */
    public static <T extends WebElement> T getElementWithPartAttributeWithEmptyResult(List<T> elements, String attribute, String partialAttributeValue) {
        return getElementByPredicateWithEmptyResult(elements, element
                -> element.getAttribute(attribute).contains(partialAttributeValue));
    }

    /**
     * Returns the element whose attribute contains the passed value.
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param attribute attribute name of the element in the DOM structure
     * @param partialAttributeValue attribute value
     * @return Returns the element whose attribute contains the passed value
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException If
     * no such element is found in the list
     */
    public static <T extends WebElement> T getElementWithPartAttribute(List<T> elements, String attribute, String partialAttributeValue) throws ElementNotFoundException {
        T element = getElementWithPartAttributeWithEmptyResult(elements, attribute, partialAttributeValue);
        if (element == null) {
            throw new ElementNotFoundException(format("Element with an attribute '%s' "
                    + "that contains the text '%s' isn't found in the list of elements", attribute, partialAttributeValue));
        }
        return element;
    }

    /**
     * Returns text values of list elements
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @return Text values of list elements
     */
    public static <T extends WebElement> List<String> getElementsText(List<T> elements) {
        return elements.stream()
                .map(T::getText)
                .collect(Collectors.toList());
    }

    /**
     * Finds an element in the list of elements with the specified text and
     * clicks on it. Checks if an element is editable. If a not editable, throws
     * an exception
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param text the text value of the element to click on
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException If
     * no such element is found in the list
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementDisabledError If
     * element is disabled
     */
    public static <T extends WebElement> void clickOnElemenByText(List<T> elements, String text) throws ElementNotFoundException {
        T element = getElementByText(elements, text);
        if (!element.isEnabled()) {
            throw new ElementDisabledError(format("Element with text '%s' is disabled.", text));
        }
        element.click();
    }

    /**
     * Finds an element by its text value
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param text the text value of the element
     * @return Returns an element of the specified type and with the specified
     * text value. If no such item is found, it will return {@code null}
     */
    public static <T extends WebElement> T getElementByTextWithEmptyResult(List<T> elements, String text) {
        return getElementByPredicateWithEmptyResult(elements, element -> text.equals(element.getText()));
    }

    /**
     * Finds an element by its text value
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param text the text value of the element
     * @return Returns the element of the specified type and with the given text
     * value
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException If
     * no such element is found in the list
     */
    public static <T extends WebElement> T getElementByText(List<T> elements, String text) throws ElementNotFoundException {
        T element = getElementByTextWithEmptyResult(elements, text);
        if (element == null) {
            throw new ElementNotFoundException(format("Element with text \"%s\" not found.", text));
        }
        return element;
    }

    /**
     * Returns a list element by index
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param index element index
     * @return Returns the element of the specified type and with the specified
     * index
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException If
     * no such element is found in the list
     */
    public static <T extends WebElement> T getElementByIndex(List<T> elements, int index) throws ElementNotFoundException {
        if (elements.isEmpty()) {
            throw new ElementNotFoundException("Element list is empty.");
        }
        if (index < elements.size() && index > -1) {
            return elements.get(index);
        } else {
            throw new ElementNotFoundException(format("Item with index '%s' not found.", index));
        }
    }

    /**
     * Returns the list item that matches the predicate
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param predicate predicate to which the required one should correspond
     * element
     * @return Returns a list item matching the predicate. If so item not found
     * in list, returns {@code null}
     */
    public static <T extends WebElement> T getElementByPredicateWithEmptyResult(List<T> elements, Predicate<T> predicate) {
        return elements.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the list element that matches the predicate
     *
     * @param <T> type of element passed - any successor {@code WebElement}
     * @param elements elements list
     * @param predicate predicate to which the required one should correspond
     * element
     * @return Returns a list element matching the predicate
     * @throws ru.sbtqa.tag.pagefactory.exceptions.ElementNotFoundException If
     * no such element is found in the list
     */
    public static <T extends WebElement> T getElementByPredicate(List<T> elements, Predicate<T> predicate) throws ElementNotFoundException {
        return elements.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("No element in the list of elements was found that matches the predicate."));
    }

    /**
     * Gets the text value of an element by its xpath expression
     *
     * @param <T> type of element relative to which we will perform the search
     * @param currentContext element for which we will execute search
     * @param xpath path to the element
     * @return Returns the text of the element. If the element was not found by
     * the specified path, then returns an empty string
     */
    public static <T extends WebElement> String getTextByElementXpath(T currentContext, String xpath) {
        List<WebElement> elements = currentContext.findElements(By.xpath(xpath));
        return elements.isEmpty() ? "" : elements.get(0).getText();
    }
}
