package ru.sbtqa.tag.pagefactory.elements.select;

import java.util.List;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.elements.BaseElement;

/**
 * Interface for the implementation of dropdown
 */
interface Select {

    <T extends WebElement> T getOpenButton();

    /**
     * Returns the xpath to the block with expanded values of the dropdown
     *
     * @return Returns the xpath to the block with expanded values of the dropdown
     */
    String getVisibleBlockXPath();

    /**
     * Selects from dropdown by value
     *
     * @param value option value
     */
    void selectByValue(String value);

    List<? extends BaseElement> getOptions();

    <T extends BaseElement> T getSelectedOption();

    <T extends WebElement> T getPlaceholder();
}
