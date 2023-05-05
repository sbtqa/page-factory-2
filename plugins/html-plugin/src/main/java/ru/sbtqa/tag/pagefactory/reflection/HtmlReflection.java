package ru.sbtqa.tag.pagefactory.reflection;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exception.ElementSearchError;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.html.loader.decorators.CustomHtmlElementDecorator;
import ru.sbtqa.tag.pagefactory.html.properties.HtmlConfiguration;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.pagefactory.CustomElementLocatorFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;

public class HtmlReflection extends DefaultReflection {

    private static final HtmlConfiguration PROPERTIES = HtmlConfiguration.create();

    /**
     * Execute method with one or more parameters inside of the given block
     * element
     *
     * @param blockPath block title, or a block chain string separated with
     * {@code ->} symbols
     * @param actionTitle title of the action to execute
     * @param parameters parameters that will be passed to method
     */
    public void executeMethodByTitleInBlock(String blockPath, String actionTitle, Object... parameters) {
        try {
            HtmlElement block = ((HtmlFindUtils) Environment.getFindUtils()).find(blockPath, HtmlElement.class);
            executeMethodByTitle(block, actionTitle, parameters);
        } catch (ElementSearchError ex) {
            throw new ElementSearchError(format("Block not found by path '%s'", blockPath), ex);
        }
    }

    /**
     * Get custom decorator instance
     *
     * @param factory custom element locator factory
     */
    public static <T extends CustomHtmlElementDecorator> T getDecorator(CustomElementLocatorFactory factory) {
        String fullyQualifiedClassName = PROPERTIES.getDecoratorFullyQualifiedClassName();
        try {
            Class<?> clazz  = Class.forName(fullyQualifiedClassName);
            Constructor<?> constructor = clazz.getConstructor(CustomElementLocatorFactory.class);
            T instance = (T) constructor.newInstance(factory);
            LOG.debug("Loaded decorator: {}", fullyQualifiedClassName);
            return instance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new ElementSearchError(format("Decorator not found by path '%s'", fullyQualifiedClassName), e);
        }
    }
}
