package ru.sbtqa.tag.pagefactory.elements.hint;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.utils.HtmlElementUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

public abstract class HintAbstract extends TypifiedElement implements Hint {

    private static final Logger LOG = LoggerFactory.getLogger(HintAbstract.class);

    protected HintAbstract(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void open() {
        if (!isOpen()) {
            getOpenButton().click();
        } else {
            LOG.info("The hint has already been opened");
        }
    }

    @Override
    public void close() {
        if (isOpen()) {
            getOpenButton().click();
        } else {
            LOG.info("The hint has already been closed");
        }
    }

    @Override
    public String getText() {
        if (isOpen()) {
            return HtmlElementUtils.getTextOfComplexElement(getContent());
        } else {
            throw new AutotestError("Text is not available when the hint is closed");
        }
    }
}
