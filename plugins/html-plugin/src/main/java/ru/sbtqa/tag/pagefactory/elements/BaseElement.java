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
     * Looks for element from content
     *
     * @param <T> element type
     * @param elementName element name inside {@code ElementTitle}.
     * May be specified as the path to the item, relative to the content
     *
     * @return Returns the found element
     */
    public <T extends TypifiedElement> T getElement(String elementName) {
        return ((HtmlFindUtils) Environment.getFindUtils()).find(this, elementName, true);
    }
}
