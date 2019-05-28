package ru.sbtqa.tag.pagefactory.elements.select;

import org.openqa.selenium.WebElement;

/**
 * Interface for elements with a choice of values
 */
public interface SelectValue extends WebElement {

    /**
     * Searches for a radio button for a given text and selects it.
     * If the button is locked, an error will be thrown
     *
     * @param value text of the button to be selected
     */
    void selectByValue(String value);
}
