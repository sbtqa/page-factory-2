package ru.sbtqa.tag.pagefactory.elements;

import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;
import ru.sbtqa.tag.pagefactory.utils.HtmlElementUtils;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

public class BaseElement extends TypifiedElement {

    public BaseElement(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getText() {
        return HtmlElementUtils.getTextOfComplexElement(this);
    }

    /**
     * Ищет элемент вунтри контента
     *
     * @param <T> тип элемента
     * @param elementName имя элемента внутри {@code ElementTitle}. Может быть
     * задано в виде пути до элемента, относительно контента
     *
     * @return Возвращает найденный элемент
     */
    public <T extends TypifiedElement> T getElement(String elementName) {
        return (T) ((HtmlFindUtils) Environment.getFindUtils()).find(this, elementName, true);
    }
}
