package ru.sbtqa.tag.pagefactory.mobile.actions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.properties.MobileConfiguration;
import ru.sbtqa.tag.pagefactory.utils.Wait;

import static ru.sbtqa.tag.pagefactory.mobile.utils.PlatformName.IOS;

public class MobilePageActions implements PageActions {

    private static final MobileConfiguration PROPERTIES = MobileConfiguration.create();

    @Override
    public void fill(Object element, String text) {
        WebElement webElement = (WebElement) element;
        webElement.click();

//        if (PROPERTIES.getAppiumPlatformName() == IOS) {
            webElement.sendKeys(text);
//        } else {
//            AppiumDriver driver = Environment.getDriverService().getDriver();
//            ((AndroidDriver) driver).key getKeyboard().sendKeys(text);
//        }
    }

    @Override
    public void click(Object element) {
        WebElement webElement = (WebElement) element;
        Wait.visibility(webElement, "Element is not clickable");
        webElement.click();
    }

    @Override
    public void press(Object element, String keyName) {
        Keys key = Keys.valueOf(keyName.toUpperCase());
        Actions actions = new Actions((WebDriver) Environment.getDriverService().getDriver());
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
