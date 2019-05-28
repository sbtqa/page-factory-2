package ru.sbtqa.tag.pagefactory.elements.radiogroup;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.elements.select.SelectValue;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import static ru.sbtqa.tag.pagefactory.web.utils.ElementUtils.clickOnElemenByText;

public abstract class RadioGroupAbstract extends TypifiedElement implements SelectValue {

    public RadioGroupAbstract(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * Searches for a radio button for a given text and selects it.
     * If the button is locked, an error will be thrown
     *
     * @param value text of the button to be selected
     */
    @Override
    public void selectByValue(String value) {
        clickOnElemenByText(getButtons(), value);
    }

    /**
     * Selects button in the radio group by index
     *
     * @param index radio button index
     */
    public void selectByIndex(int index) {
        ElementUtils.getElementByIndex(getButtons(), index).click();
    }

    /**
     * Gets all radio buttons of group
     *
     * @return Returns radio buttons
     */
    public abstract List<? extends Button> getButtons();

    /**
     * Gets the text of the selected radio button
     *
     * @return Returns the text of the selected button. If none of the buttons is selected, it will return an empty string
     */
    public String getSelectedValue() {
        return getButtons().stream()
                .filter(Button::isSelected)
                .map(Button::getText)
                .findFirst()
                .orElse("");
    }

    /**
     * Checks if the radio group has the selected value
     *
     * @return Returns {@code true} if group has the selected value, else - {@code false}
     */
    public boolean hasSelectedValue() {
        return getButtons().stream()
                .anyMatch(Button::isSelected);
    }

    /**
     * Gets the text values ​​of the radio group
     *
     * @return Returns the text ​​of the radio group
     */
    public List<String> getValues() {
        return getButtons().stream().map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
