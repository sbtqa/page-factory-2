package ru.sbtqa.tag.pagefactory.web.actions;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.utils.WebWait;

public class WebPageActions implements PageActions {

    private static final Logger LOG = LoggerFactory.getLogger(WebPageActions.class);

    @Override
    public void fill(Object element, String text) {
        WebElement webElement = (WebElement) element;
        click(webElement);
        if (null != text) {
            clear(webElement);
        }
        webElement.sendKeys(text);
    }

    public void clear(Object element) {
        WebElement webElement = (WebElement) element;
        try {
            webElement.clear();
        } catch (InvalidElementStateException | NullPointerException e) {
            LOG.debug("Failed to clear web element {}", webElement, e);
        }
    }

    @Override
    public void click(Object element) {
        WebElement webElement = (WebElement) element;
        WebWait.visibility(webElement);
        webElement.click();
    }

    @Override
    public void press(Object element, String keyName) {
        WebElement webElement = (WebElement) element;
        Actions actions = new Actions((WebDriver) Environment.getDriverService().getDriver());

        if (null != webElement) {
            actions.moveToElement(webElement);
            actions.click();
        }

        Keys key = Keys.valueOf(keyName.toUpperCase());
        actions.sendKeys(key).build().perform();
    }

    @Override
    public void select(Object element, String option) {
        WebElement webElement = (WebElement) element;
        Select select = new Select(webElement);
        select.selectByValue(option);
    }

    @Override
    public void setCheckbox(Object element, boolean state) {
        WebElement webElement = (WebElement) element;
        if (webElement.isSelected() != state) {
            webElement.click();
        }
    }
}
