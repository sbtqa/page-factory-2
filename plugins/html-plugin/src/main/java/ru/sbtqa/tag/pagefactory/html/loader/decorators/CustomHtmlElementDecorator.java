package ru.sbtqa.tag.pagefactory.html.loader.decorators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.proxyhandlers.TypifiedElementListNamedProxyHandler;
import ru.yandex.qatools.htmlelements.pagefactory.CustomElementLocatorFactory;
import ru.yandex.qatools.htmlelements.utils.HtmlElementUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static ru.sbtqa.tag.pagefactory.html.loader.CustomHtmlElementLoader.createHtmlElement;
import static ru.sbtqa.tag.pagefactory.html.loader.CustomHtmlElementLoader.createTypifiedElement;
import static ru.yandex.qatools.htmlelements.loader.decorator.ProxyFactory.createTypifiedElementListProxy;
import static ru.yandex.qatools.htmlelements.utils.HtmlElementUtils.getElementName;

/**
 * This decorator patches Yandex native TypifiedElement decorator and introduces
 * only one change: When typified element is being initialized on the page, page
 * factory would walk via element fields recursively and initialize all child
 * typified elements
 */
public class CustomHtmlElementDecorator extends HtmlElementDecorator {

    public CustomHtmlElementDecorator(CustomElementLocatorFactory factory) {
        super(factory);
    }

    @Override
    protected <T extends TypifiedElement> T decorateTypifiedElement(ClassLoader loader, Field field) {
        WebElement elementToWrap = decorateWebElement(loader, field);

        //noinspection unchecked
        return createTypifiedElement((Class<T>) field.getType(), elementToWrap, getElementName(field));
    }

    @Override
    protected <T extends TypifiedElement> List<T> decorateTypifiedElementList(ClassLoader loader, Field field) {
        final Class<T> elementClass = (Class<T>) HtmlElementUtils.getGenericParameterClass(field);
        final ElementLocator locator = factory.createLocator(field);
        final String name = getElementName(field);

        InvocationHandler handler = new TypifiedElementListNamedProxyHandler(elementClass, locator, name) {

            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if ("toString".equals(method.getName())) {
                    return name;
                }

                List<T> elements = new LinkedList<>();
                int elementNumber = 0;
                for (WebElement element : locator.findElements()) {
                    String newName = String.format("%s [%d]", name, elementNumber++);
                    elements.add(createTypifiedElement(elementClass, element, newName));
                }

                try {
                    return method.invoke(elements, objects);
                } catch (InvocationTargetException e) {
                    throw new AutotestError ("Error initializing elements.", e);
                }
            }
        };
        return createTypifiedElementListProxy(loader, handler);
    }

    @Override
    protected <T extends HtmlElement> T decorateHtmlElement(ClassLoader loader, Field field) {
        WebElement elementToWrap = decorateWebElement(loader, field);

        return createHtmlElement((Class<T>) field.getType(), elementToWrap, getElementName(field));
    }


}
