package ru.sbtqa.tag.pagefactory.utils;

import java.util.ArrayList;
import java.util.List;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

public class Wait {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    /**
     * Wait until the element becomes visible
     *
     * @param xpath element xpath
     * @param message message in case the element did not appear after waiting
     */
    public static void visibility(String xpath, String message) {
        visibility(xpath, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until the element becomes visible
     *
     * @param xpath element xpath
     * @param message message in case the element did not appear after waiting
     */
    public static void visibility(String xpath, String message, int timeout) {
        wait(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)), message, timeout);
    }

    /**
     * Wait until the element becomes visible
     *
     * @param by locator to search for an item
     * @param message message in case the element did not appear after waiting
     */
    public static void visibility(By by, String message) {
        visibility(by, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until the element becomes visible
     *
     * @param by locator to search for an item
     * @param message message in case the element did not appear after waiting
     */
    public static void visibility(By by, String message, int timeout) {
        wait(ExpectedConditions.visibilityOfElementLocated(by), message, timeout);
    }

    /**
     * Wait until the element becomes visible
     *
     * @param element element
     * @param message message in case the element did not appear after waiting
     */
    public static void visibility(WebElement element, String message) {
        visibility(element, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until the element becomes visible
     *
     * @param element element
     * @param message message in case the element did not appear after waiting
     * @param timeout condition timeout in seconds
     */
    public static void visibility(WebElement element, String message, int timeout) {
        wait(ExpectedConditions.visibilityOf(element), message, timeout);
    }

    /**
     * Wait until the element becomes invisible
     *
     * @param xpath element xpath
     * @param message message in case the element did not disappear after waiting
     */
    public static void invisibility(String xpath, String message) {
        invisibility(xpath, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until the element becomes invisible
     *
     * @param xpath element xpath
     * @param message message in case the element did not disappear after waiting
     * @param timeout condition timeout in seconds
     */
    public static void invisibility(String xpath, String message, int timeout) {
        wait(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)), message, timeout);
    }

    /**
     * Wait until the element becomes invisible
     *
     * @param by locator to search for an item
     * @param message message in case the element did not disappear after waiting
     */
    public static void invisibility(By by, String message) {
        invisibility(by, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until the element becomes invisible
     *
     * @param by locator to search for an item
     * @param message message in case the element did not disappear after waiting
     * @param timeout condition timeout in seconds
     */
    public static void invisibility(By by, String message, int timeout) {
        wait(ExpectedConditions.invisibilityOfElementLocated(by), message, timeout);
    }

    /**
     * Wait until the element becomes invisible
     *
     * @param element element
     * @param message message in case the element did not disappear after waiting
     */
    public static void invisibility(WebElement element, String message) {
        invisibility(element, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until the element becomes invisible
     *
     * @param element element
     * @param message message in case the element did not disappear after waiting
     * @param timeout condition timeout in seconds
     */
    public static void invisibility(WebElement element, String message, int timeout) {
        List<WebElement> elements = new ArrayList<>();
        elements.add(element);
        wait(ExpectedConditions.invisibilityOfAllElements(elements), message, timeout);
    }

    /**
     * Wait until element value change
     *
     * @param element element
     * @param attributeName attribute name, for example "class"
     * @param attributeValue expected attribute value
     * @param message message in case the attribute value has not changed
     */
    public static void changeAttribute(WebElement element, String attributeName, String attributeValue, String message) {
        changeAttribute(element, attributeName, attributeValue, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until element value change
     *
     * @param element element
     * @param attributeName attribute name, for example "class"
     * @param attributeValue expected attribute value
     * @param message message in case the attribute value has not changed
     * @param timeout condition timeout in seconds
     */
    public static void changeAttribute(WebElement element, String attributeName, String attributeValue, String message, int timeout) {
        wait(ExpectedConditions.attributeToBe(element, attributeName, attributeValue), message, timeout);
    }

    /**
     * Wait until element value change
     *
     * @param by locator to search for an item
     * @param attributeName attribute name, for example "class"
     * @param attributeValue expected attribute value
     * @param message message in case the attribute value has not change
     */
    public static void changeAttribute(By by, String attributeName, String attributeValue, String message) {
        changeAttribute(by, attributeName, attributeValue, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until element value change
     *
     * @param by locator to search for an item
     * @param attributeName attribute name, for example "class"
     * @param attributeValue expected attribute value
     * @param message message in case the attribute value has not change
     * @param timeout condition timeout in seconds
     */
    public static void changeAttribute(By by, String attributeName, String attributeValue, String message, int timeout) {
        wait(ExpectedConditions.attributeToBe(by, attributeName, attributeValue), message, timeout);
    }

    /**
     * Waiting for a certain text fragment to appear in the attribute
     *
     * @param element element
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should contains
     * @param message message in case the attribute value does not contain the expected value
     */
    public static void attributeContains(WebElement element, String attributeName, String partAttributeValue, String message) {
        attributeContains(element, attributeName, partAttributeValue, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for a certain text fragment to appear in the attribute
     *
     * @param element element
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should contains
     * @param message message in case the attribute value does not contain the expected value
     * @param timeout condition timeout in seconds
     */
    public static void attributeContains(WebElement element, String attributeName, String partAttributeValue, String message, int timeout) {
        wait(ExpectedConditions.attributeContains(element, attributeName, partAttributeValue), message, timeout);
    }

    /**
     * Waiting for a certain text fragment to appear in the attribute
     *
     * @param by locator to search for an item
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should contains
     * @param message message in case the attribute value does not contain the expected value
     */
    public static void attributeContains(By by, String attributeName, String partAttributeValue, String message) {
        attributeContains(by, attributeName, partAttributeValue, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for a certain text fragment to appear in the attribute
     *
     * @param by locator to search for an item
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should contains
     * @param message message in case the attribute value does not contain the expected value
     * @param timeout condition timeout in seconds
     */
    public static void attributeContains(By by, String attributeName, String partAttributeValue, String message, int timeout) {
        wait(ExpectedConditions.attributeContains(by, attributeName, partAttributeValue), message, timeout);
    }

    /**
     * Awaiting disappearance in attribute of a certain text fragment
     *
     * @param element element
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should not contains
     * @param message message if the attribute value contains the expected value
     */
    public static void attributeNotContains(WebElement element, String attributeName, String partAttributeValue, String message) {
        attributeNotContains(element, attributeName, partAttributeValue, message, PROPERTIES.getTimeout());
    }

    /**
     * Awaiting disappearance in attribute of a certain text fragment
     *
     * @param element element
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should not contains
     * @param message message if the attribute value contains the expected value
     * @param timeout condition timeout in seconds
     */
    public static void attributeNotContains(WebElement element, String attributeName, String partAttributeValue, String message, int timeout) {
        wait(ExpectedConditions.not(ExpectedConditions.attributeContains(element, attributeName, partAttributeValue)), message, timeout);
    }

    /**
     * Awaiting disappearance in attribute of a certain text fragment
     *
     * @param by locator to search for an item
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should not contains
     * @param message message if the attribute value contains the expected value
     */
    public static void attributeNotContains(By by, String attributeName, String partAttributeValue, String message) {
        attributeNotContains(by, attributeName, partAttributeValue, message, PROPERTIES.getTimeout());
    }

    /**
     * Awaiting disappearance in attribute of a certain text fragment
     *
     * @param by locator to search for an item
     * @param attributeName attribute name, for example "class"
     * @param partAttributeValue the expected value that the attribute should not contains
     * @param message message if the attribute value contains the expected value
     * @param timeout condition timeout in seconds
     */
    public static void attributeNotContains(By by, String attributeName, String partAttributeValue, String message, int timeout) {
        wait(ExpectedConditions.not(ExpectedConditions.attributeContains(by, attributeName, partAttributeValue)), message, timeout);
    }

    /**
     * Waiting for text to change or appear in the item
     *
     * @param xpath element xpath
     * @param text text that must be in the element
     * @param message message in case there is no text in the element after waiting
     */
    public static void textContains(String xpath, String text, String message) {
        textContains(xpath, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for text to change or appear in the item
     *
     * @param xpath element xpath
     * @param text text that must be in the element
     * @param message message in case there is no text in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textContains(String xpath, String text, String message, int timeout) {
        wait(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text), message, timeout);
    }

    /**
     * Waiting for text to change or appear in the item
     *
     * @param by locator to search for an item
     * @param text text that must be in the element
     * @param message message in case there is no text in the element after waiting
     */
    public static void textContains(By by, String text, String message) {
        textContains(by, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for text to change or appear in the item
     *
     * @param by locator to search for an item
     * @param text text that must be in the element
     * @param message message in case there is no text in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textContains(By by, String text, String message, int timeout) {
        wait(ExpectedConditions.textToBePresentInElementLocated(by, text), message, timeout);
    }

    /**
     * Waiting for text to change or appear in the item
     *
     * @param element element
     * @param text text that must be in the element
     * @param message message in case there is no text in the element after waiting
     */
    public static void textContains(WebElement element, String text, String message) {
        textContains(element, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for text to change or appear in the item
     *
     * @param element element
     * @param text text that must be in the element
     * @param message message in case there is no text in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textContains(WebElement element, String text, String message, int timeout) {
        wait(ExpectedConditions.textToBePresentInElement(element, text), message, timeout);
    }

    /**
     * Waiting for missing expected text in the item
     *
     * @param xpath element xpath
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     */
    public static void textNotContains(String xpath, String text, String message) {
        textNotContains(xpath, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for missing expected text in the item
     *
     * @param xpath element xpath
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textNotContains(String xpath, String text, String message, int timeout) {
        wait(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.xpath(xpath), text)), message, timeout);
    }

    /**
     * Waiting for missing expected text in the item
     *
     * @param by locator to search for an item
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     */
    public static void textNotContains(By by, String text, String message) {
        textNotContains(by, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for missing expected text in the item
     *
     * @param by locator to search for an item
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textNotContains(By by, String text, String message, int timeout) {
        wait(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(by, text)), message, timeout);
    }

    /**
     * Waiting for missing expected text in the item
     *
     * @param element element
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     */
    public static void textNotContains(WebElement element, String text, String message) {
        textNotContains(element, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waiting for missing expected text in the item
     *
     * @param element element
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textNotContains(WebElement element, String text, String message, int timeout) {
        wait(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, text)), message, timeout);
    }

    /**
     * Waits for text inequality in the element
     *
     * @param by locator to search for an item
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     */
    public static void textNotEquals(By by, String text, String message) {
        textNotEquals(by, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waits for text inequality in the element
     *
     * @param by locator to search for an item
     * @param text text that should be missing from the element
     * @param message message in case text is present in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textNotEquals(By by, String text, String message, int timeout) {
        wait(ExpectedConditions.not(ExpectedConditions.textToBe(by, text)), message, timeout);
    }

    /**
     * Waits for text to be equal in the element
     *
     * @param by locator to search for an item
     * @param text текст, который должен присутствовать в элементе
     * @param message message in case there is no text in the element after waiting
     */
    public static void textEquals(By by, String text, String message) {
        textEquals(by, text, message, PROPERTIES.getTimeout());
    }

    /**
     * Waits for text to be equal in the element
     *
     * @param by locator to search for an item
     * @param text текст, который должен присутствовать в элементе
     * @param message message in case there is no text in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void textEquals(By by, String text, String message, int timeout) {
        wait(ExpectedConditions.textToBe(by, text), message, timeout);
    }

    /**
     * Wait until element will be present on the DOM of a page
     *
     * @param xpath element xpath
     * @param message message in case there is no text in the element after waiting
     */
    public static void presence(String xpath, String message) {
        presence(xpath, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until element will be present on the DOM of a page
     *
     * @param xpath element xpath
     * @param message message in case there is no text in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void presence(String xpath, String message, int timeout) {
        wait(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)), message, timeout);
    }

    /**
     * Wait until element will be present on the DOM of a page
     *
     * @param by locator to search for an item
     * @param message message in case there is no text in the element after waiting
     */
    public static void presence(By by, String message) {
        presence(by, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until element will be present on the DOM of a page
     *
     * @param by locator to search for an item
     * @param message message in case there is no text in the element after waiting
     * @param timeout condition timeout in seconds
     */
    public static void presence(By by, String message, int timeout) {
        wait(ExpectedConditions.presenceOfElementLocated(by), message, timeout);
    }

    /**
     * Wait until element present
     *
     * @param xpath element xpath
     * @param message message in case the element did not appear after waiting
     */
    public static void clickable(String xpath, String message) {
        clickable(xpath, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until element present
     *
     * @param xpath element xpath
     * @param message message in case the element did not appear after waiting
     * @param timeout condition timeout in seconds
     */
    public static void clickable(String xpath, String message, int timeout) {
        wait(ExpectedConditions.elementToBeClickable(By.xpath(xpath)), message, timeout);
    }

    /**
     * Wait until element present
     *
     * @param by locator to search for an item
     * @param message message in case the element did not appear after waiting
     */
    public static void clickable(By by, String message) {
        clickable(by, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until element present
     *
     * @param by locator to search for an item
     * @param message message in case the element did not appear after waiting
     * @param timeout condition timeout in seconds
     */
    public static void clickable(By by, String message, int timeout) {
        wait(ExpectedConditions.elementToBeClickable(by), message, timeout);
    }

    /**
     * Wait until element present
     *
     * @param element element
     * @param message message in case the element did not appear after waiting
     */
    public static void clickable(WebElement element, String message) {
        clickable(element, message, PROPERTIES.getTimeout());
    }

    /**
     * Wait until element present
     *
     * @param element element
     * @param message message in case the element did not appear after waiting
     * @param timeout condition timeout in seconds
     */
    public static void clickable(WebElement element, String message, int timeout) {
        wait(ExpectedConditions.elementToBeClickable(element), message, timeout);
    }

    /**
     * Waits during the standard timeout for the condition specified by the {@link ExpectedCondition} parameter.
     * For example, to wait for an element to invisible by its locator {@code ExpectedConditions.invisibilityOfElementLocated (by)}
     *
     * @param condition check condition
     * @param message message in case verification failed
     * @param timeout condition timeout in seconds
     */
    public static void wait(ExpectedCondition condition, String message, int timeout) {
        new WebDriverWait(Environment.getDriverService().getDriver(), timeout)
                .withMessage(message).until(condition);
    }
}