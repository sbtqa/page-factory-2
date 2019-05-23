package ru.sbtqa.tag.pagefactory.elements.accordion;

import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.elements.BaseElement;
import ru.yandex.qatools.htmlelements.element.Button;

public interface Accordion extends WebElement {

    <T extends BaseElement> T getHeader();

    <T extends BaseElement> T getContent();

    Button getExpandButton();

    void open();

    void close();

    boolean isOpen();
}
