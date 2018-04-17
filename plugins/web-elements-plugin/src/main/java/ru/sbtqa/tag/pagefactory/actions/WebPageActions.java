package ru.sbtqa.tag.pagefactory.actions;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.utils.ExpectedConditionsUtils;

public class WebPageActions implements PageActions {

    private static final Logger LOG = LoggerFactory.getLogger(WebPageActions.class);

    @Override
    public void fill(WebElement webElement, String text) {
        click(webElement);
        if (null != text) {
            clear(webElement);
        }
        webElement.sendKeys(text);
    }

    private void clear(WebElement webElement) {
        try {
            webElement.clear();
        } catch (InvalidElementStateException | NullPointerException e) {
            LOG.debug("Failed to clear web element {}", webElement, e);
        }
    }

    @Override
    public void click(WebElement webElement) {
        ExpectedConditionsUtils.waitForElementGetEnabled(webElement);
        webElement.click();
    }

    @Override
    public void press(WebElement webElement, String keyName) {
        Actions actions = new Actions(PageContext.getCurrentPage().getDriver());

        if (null != webElement) {
            actions.moveToElement(webElement);
            actions.click();
        }

        Keys key = Keys.valueOf(keyName.toUpperCase());
        actions.sendKeys(key).build().perform();
    }

    @Override
    public void select(WebElement webElement, String option) {
        Select select = new Select(webElement);
        select.selectByValue(option);
    }

    @Override
    public void setCheckbox(WebElement webElement, boolean state) {
        if (webElement.isSelected() != state) {
            webElement.click();
        }
    }
}
