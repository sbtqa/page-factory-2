package ru.sbtqa.tag.pagefactory.utils;

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
        WebElement webElement;
        if (isTypifiedElement(element.getClass())) {
            webElement = ((TypifiedElement) element).getWrappedElement();
        } else {
            webElement = element;
        }
        return webElement;
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
}
