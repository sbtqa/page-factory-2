package ru.sbtqa.tag.pagefactory.utils;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exception.ElementSearchError;
import ru.sbtqa.tag.pagefactory.transformer.TestIdType;
import static java.lang.String.format;

public class TestIdUtils {

    private static final String ERROR = "The element was not found on the page. " +
            "Also, a search was performed for data-test-id and text, which also did not produce results. " +
            "Search was performed by: %s";

    /**
     * Searches the data-test-id on a page or by text
     *
     * @param testId data-test-id or text
     * @return found element
     */
    public static WebElement find(String testId) {
        return findList(testId).get(0);
    }

    /**
     * Searches for a list of items by data-test-id on a page or by text
     *
     * @param testId data-test-id or the text by which we search for the list
     * @return found element
     */
    public static List<WebElement> findList(String testId) {
        List<WebElement> elements = findListWithEmptyResult(testId);
        if (elements.isEmpty()) {
            throw new ElementSearchError(format(ERROR, testId));
        }
        return elements;
    }

    /**
     * Searches the list of elements by data-test-id on the page or by the text that is inside the transferred element
     *
     * @param where element to search within
     * @param whatTestId data-test-id or the text by which we search for the list
     * @return found element
     */
    public static List<WebElement> findList(WebElement where, String whatTestId) {
        List<WebElement> elements = findListWithEmptyResult(where, whatTestId);
        if (elements.isEmpty()) {
            throw new ElementSearchError(format(ERROR, whatTestId));
        }
        return elements;
    }
    
    /**
     * Searches for a list of elements by data-test-id on a page or by text
     *
     * @param testId data-test-id or the text by which we search for the list
     * @return found element
     */
    public static List<WebElement> findListWithEmptyResult(String testId) {
        return Environment.getDriverService().getDriver().findElements(getFindBy(testId, TestIdType.QA));
    }

    /**
     * Searches the list of items by data-test-id on the page or by the text that is inside the transferred item
     *
     * @param where element to search within
     * @param whatTestId data-test-id or text
     * @return found element
     */
    public static List<WebElement> findListWithEmptyResult(WebElement where, String whatTestId) {
        return where.findElements(getFindBy(whatTestId, TestIdType.QA));
    }

    /**
     * Searches the data-test-id on the page or the text of the element that is inside the transmitted
     *
     * @param where element to search within
     * @param whatTestId data-test-id or text
     * @return found element
     */
    public static WebElement find(WebElement where, String whatTestId) {
        return findList(where, whatTestId).get(0);
    }

    /**
     * Searches the data-test-id on the page or on the text of the element that is inside another,
     * found in a similar way
     *
     * @param whereTestId data-test-id or the text of the element within which the search will be performed
     * @param whatTestId data-test-id or the text of the sought-for element
     * @return found element
     */
    public static WebElement find(String whereTestId, String whatTestId) {
        WebElement whereElement = find(whereTestId);
        return whereElement.findElements(getFindBy(whatTestId, TestIdType.QA)).get(0);
    }

    /**
     * Searches the data-test-id for the element that identifies the page
     *
     * @param testId page title
     * @return found element
     */
    public static List<WebElement> findElementIdentifyingPage(String testId) {
        return Environment.getDriverService().getDriver().findElements(getFindBy(testId, TestIdType.PAGE));
    }

    private static By getFindBy(String testId, TestIdType testIdType) {
        By by;
        by = By.xpath(format(".//*[%s or text()='%s']", parseDataTestId(testId, testIdType), testId));
        return by;
    }

    private static String parseDataTestId(String testId, TestIdType testIdType) {
        String dataTestId = MD5.hash(testId);
        return format("@data-test-id='%sId_%s'", testIdType.toString().toLowerCase(), dataTestId);
    }
}
