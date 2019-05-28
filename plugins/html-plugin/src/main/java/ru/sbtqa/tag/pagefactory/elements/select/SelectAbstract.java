package ru.sbtqa.tag.pagefactory.elements.select;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.elements.BaseElement;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.transformer.enums.SearchStrategy;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;
import static java.lang.String.format;

public abstract class SelectAbstract extends TypifiedElement implements Select {

    public static final Logger LOG = LoggerFactory.getLogger(SelectAbstract.class);

    private static final String ERROR_DISABLED_TEMPLATE = "The option with text \"%s\" is not editable";

    protected static final String NOT_FOUND_OPTION_ERROR = "Option not found";

    private static final String NOT_FOUND_OPTION_IN_ELEMENT_TEMPLATE = "Option with text \"%s\" not found in custom select in the element \"%s\"";

    public SelectAbstract(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * Opens dropdown
     */
    public void open() {
        List<WebElement> visibleBlock = getWrappedElement().findElements(By.xpath(getVisibleBlockXPath()));
        if (visibleBlock.isEmpty()) {
            getOpenButton().click();
        }
    }

    /**
     * Closes dropdown (press ESC)
     */
    public void close() {
        Actions actions = new Actions((WebDriver) Environment.getDriverService().getDriver());
        actions.sendKeys(Keys.ESCAPE).perform();
    }

    /**
     * Returns selected option value
     *
     * @return Returns selected option value
     */
    public String getSelectedOptionValue() {
        try {
            return getSelectedOption().getText();
        } catch (NoSuchElementException ex) {
            LOG.debug("No value selected. Will return the value of placeholder.", ex);
            return getPlaceholder().getText();
        }
    }

    /**
     * Returns option values
     *
     * @return Returns option values
     */
    public List<String> getOptionValues() {
        open();
        Assert.assertFalse(NOT_FOUND_OPTION_ERROR, getOptions().isEmpty());
        List<String> result = getOptions().stream()
                .map(BaseElement::getText)
                .collect(Collectors.toList());
        close();
        return result;
    }

    /**
     * Selects the option to strictly match the passed text value
     *
     * @param value the value to be selected
     */
    @Override
    public void selectByValue(String value) {
        selectByValue(value, SearchStrategy.EQUALS);
    }

    /**
     * Selects an option by text value depending on the search strategy
     *
     * @param value the value to be selected
     * @param strategy search strategy:
     * {@code SearchStrategy.EQUALS} - by exact match
     * {@code SearchStrategy.CONTAINS} - by partial match
     */
    public void selectByValue(String value, SearchStrategy strategy) {
        open();
        for (BaseElement option : getOptions()) {
            if (strategy.equals(SearchStrategy.EQUALS) ? value.equals(option.getText()) : option.getText().contains(value)) {
                if (!option.isEnabled()) {
                    throw new AutotestError(String.format(ERROR_DISABLED_TEMPLATE, option.getText()));
                }
                option.click();
                return;
            }
        }
        throw new AutotestError("Option '" + value + "' not found");
    }

    /**
     * Selects an option for the text value in the specified element
     *
     * @param value the value to be selected
     * @param element the element within which the search will occur
     */
    public void selectByValue(String value, String element) throws AutotestError {
        open();
        List<? extends BaseElement> listOp = getOptions();
        for (BaseElement op : listOp) {
            if (value.equals(op.getElement(element).getText())) {
                op.click();
                return;
            }
        }
        close();
        throw new AutotestError(format(NOT_FOUND_OPTION_IN_ELEMENT_TEMPLATE, value, element));
    }

    /**
     * Select an option by index
     *
     * @param i option index
     */
    public void selectByIndex(int i) {
        open();
        BaseElement option = ElementUtils.getElementByIndex(getOptions(), i);
        if (!option.isEnabled()) {
            throw new AutotestError(String.format(ERROR_DISABLED_TEMPLATE, option.getText()));
        }
        option.click();
    }

    /**
     * Looks for the option to strictly match the text and returns its index
     *
     * @param value option value
     * @return Returns option index
     */
    public int getIdByValue(String value) {
        return getIdByValue(value, SearchStrategy.EQUALS);
    }

    /**
     * Searches for an option by text depending on the search strategy and returns its index
     *
     * @param value option value
     * @param strategy search strategy:
     *      {@code SearchStrategy.EQUALS} - by exact match
     *      {@code SearchStrategy.CONTAINS} - by partial match
     * @return Returns option index
     */
    public int getIdByValue(String value, SearchStrategy strategy) {
        open();
        List<? extends BaseElement> listOp = getOptions();
        int listSize = listOp.size();
        for (int i = 0; i < listSize; i++) {
            String optionText = listOp.get(i).getText();
            if (strategy.equals(SearchStrategy.EQUALS) ? value.equals(optionText) : optionText.contains(value)) {
                close();
                return i;
            }
        }
        close();
        return -1;
    }

    /**
     * Searches for an option on the text in the element and returns its index
     *
     * @param value the element of an option value
     * @param element the element within which the search will occur
     * @return Returns option index
     */
    public int getIdByValue(String value, String element) {
        open();
        List<? extends BaseElement> listOp = getOptions();
        int listSize = listOp.size();
        for (int i = 0; i < listSize; i++) {
            if (value.equals(listOp.get(i).getElement(element).getText())) {
                close();
                return i;
            }
        }
        close();
        throw new AutotestError(format(NOT_FOUND_OPTION_IN_ELEMENT_TEMPLATE, value, element));
    }

}
