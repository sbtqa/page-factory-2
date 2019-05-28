package ru.sbtqa.tag.pagefactory.elements.panel;

import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.utils.HtmlElementUtils;

public interface Panel extends WebElement {

    <T extends WebElement> T getBody();

    default <T extends WebElement> T getHeader() {
        throw new UnsupportedOperationException("The panel does not contain a header");
    }

    /**
     * Returns the body text of the panel. If the body has complex content,
     * then it receives a concatenation of all text values separated by spaces
     *
     * @return Returns the body text of the panel
     */
    default String getBodyText() {
        return HtmlElementUtils.getTextOfComplexElement(getBody());
    }

    /**
     * Returns the header text of the panel. If the header has complex content,
     * then it receives a concatenation of all text values separated by spaces
     *
     * @return Returns the header text of the panel
     */
    default String getHeaderText() {
        return HtmlElementUtils.getTextOfComplexElement(getHeader());
    }
}
