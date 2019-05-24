package ru.sbtqa.tag.pagefactory.elements.accordion;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

public abstract class AccordionAbstract extends TypifiedElement implements Accordion {

    private static final Logger LOG = LoggerFactory.getLogger(AccordionAbstract.class);

    public AccordionAbstract(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void open() {
        if (!isOpen()) {
            getExpandButton().click();
        } else {
            LOG.info("The accordion has already been opened");
        }
    }

    @Override
    public void close() {
        if (isOpen()) {
            getExpandButton().click();
        } else {
            LOG.info("The accordion has already been closed");
        }
    }
}
