package ru.sbtqa.tag.pagefactory.html.loader;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import ru.yandex.qatools.htmlelements.exceptions.HtmlElementsException;
import ru.yandex.qatools.htmlelements.loader.HtmlElementLoader;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;
import ru.yandex.qatools.htmlelements.pagefactory.CustomElementLocatorFactory;

import java.lang.reflect.InvocationTargetException;

import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.newInstance;

public class CustomHtmlElementLoader extends HtmlElementLoader {

    public static <T extends HtmlElement> T createHtmlElement(Class<T> elementClass, WebElement elementToWrap, String name) {
        try {
            T instance = newInstance(elementClass);
            instance.setWrappedElement(elementToWrap);
            instance.setName(name);
            populatePageObject(instance, elementToWrap);
            return instance;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new HtmlElementsException(e);
        }
    }

    public static <T extends TypifiedElement> T createTypifiedElement(Class<T> elementClass, WebElement elementToWrap, String name) {
        try {
            T instance = newInstance(elementClass, elementToWrap);
            instance.setName(name);
            // this is the patch to Yandex typified elements. With this string it walks via nested elements and initializes them too
            populatePageObject(instance, elementToWrap);
            return instance;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new HtmlElementsException(e);
        }
    }

    public static void populatePageObject(Object page, SearchContext searchContext) {
        populatePageObject(page, new HtmlElementLocatorFactory(searchContext));
    }
    

    public static void populatePageObject(Object page, CustomElementLocatorFactory locatorFactory) {
        PageFactory.initElements((FieldDecorator) HtmlReflection.getDecorator(locatorFactory), page);
    }

}
