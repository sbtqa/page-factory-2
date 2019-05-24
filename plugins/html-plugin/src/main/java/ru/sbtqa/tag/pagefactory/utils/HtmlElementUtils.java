package ru.sbtqa.tag.pagefactory.utils;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.html.loader.CustomHtmlElementLoader;
import ru.sbtqa.tag.pagefactory.html.loader.decorators.CustomHtmlElementDecorator;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;
import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.isTypifiedElement;

public class HtmlElementUtils {

    private static final String TEXT_XPATH = ".//*[text()]";

    private HtmlElementUtils() {
    }

    /**
     * Получает {@code WebElement}. Если элемент уже имеет тип
     * {@code WebElement}, то вернет его. Если наследник
     * {@code TypifiedElement}, то {@code WrappedElement}
     *
     * @param <T> тип переданного элемента - любой наследниик {@code WebElement}
     * @param element элемент, по которому нужно получить элемент типа
     * {@code WebElement}
     * @return преобразованный к {@code WebElement} элемент
     */
    public static <T extends WebElement> WebElement getWebElement(T element) {
        if (isTypifiedElement(element.getClass())) {
            TypifiedElement typifiedElement = (TypifiedElement) element;
            return typifiedElement.getWrappedElement();
        } else {
            return element;
        }
    }

    /**
     * Creates an element with the specified custom type from {@code WebElement}
     * and initializes it on the current page
     *
     * @param <T> desired item type
     * @param clazz class to instantiate
     * @param element element from which we will create a custom one
     * @return Returns an element of the specified type
     */
    public static <T extends TypifiedElement> T createElementWithCustomType(Class<T> clazz, WebElement element) {
        T customElement = CustomHtmlElementLoader.createTypifiedElement(clazz, element, "Custom");
        CustomHtmlElementDecorator decorator = new CustomHtmlElementDecorator(new HtmlElementLocatorFactory(Environment.getDriverService().getDriver()));
        PageFactory.initElements(decorator, PageContext.getCurrentPage());
        return customElement;
    }

    /**
     * Gets a text value for elements that contain several text elements
     * <p>
     * If the element has no nested elements with text, its text value will be received.
     * If the element has no text, an empty string will be returned
     * <p>
     * If there are several elements with text inside, then their values ​​will be merged into a
     * string separated by a space
     *
     * @param <T> type of item received
     * @param element the element by which to get the text
     * @return Returns the text value of an element
     */
    public static <T extends WebElement> String getTextOfComplexElement(T element) {
        return getTextOfComplexElement(element, " ");
    }

    /**
     * Gets a text value for elements that contain several text elements
     * <p>
     * If the element has no nested elements with text, its text value will be received.
     * If the element has no text, an empty string will be returned
     * <p>
     * If there are several elements with text inside, then their values ​​will be merged into a
     * string through the specified separator
     *
     * @param <T> type of item received
     * @param element the element by which to get the text
     * @param separator separator
     * @return Returns the text value of an element
     */
    public static <T extends WebElement> String getTextOfComplexElement(T element, String separator) {
        WebElement webElement = getWebElement(element);
        List<WebElement> textElements = webElement.findElements(By.xpath(TEXT_XPATH));

        StringBuilder value = new StringBuilder();
        String elementText = webElement.getText();

        if (textElements.isEmpty()) {
            value.append(elementText);
        } else {
            for (WebElement textElement : textElements) {
                value.append(textElement.getText()).append(separator);
            }
        }

        // TODO A temporary solution for elements containing tagged overtones inside the text button.
        //  For them, the standard getText () does not work and the logic for obtaining a complex element does not work.
        if (elementText.length() - value.toString().length() >= 0) {
            value = new StringBuilder(elementText);
        }
        return value.toString().trim();
    }
}
