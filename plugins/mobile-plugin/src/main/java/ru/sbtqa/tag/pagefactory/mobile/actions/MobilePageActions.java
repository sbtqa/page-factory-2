package ru.sbtqa.tag.pagefactory.mobile.actions;

import io.appium.java_client.AppiumDriver;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.AdbConsole;
import ru.sbtqa.tag.pagefactory.MobileConfiguration;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class MobilePageActions implements PageActions {

    private static final MobileConfiguration PROPERTIES = ConfigFactory.create(MobileConfiguration.class);

    @Override
    public void fill(Object element, String text) {
        WebElement webElement = (WebElement) element;
        AppiumDriver driver = Environment.getDriverService().getDriver();
        webElement.click();
        driver.getKeyboard().sendKeys(text);


        if (PROPERTIES.isAppiumClickAdb()) {
            // set ADBKeyBoard as default
            AdbConsole.execute("ime set com.android.adbkeyboard/.AdbIME");
            // send broadcast intent via adb
            AdbConsole.execute(String.format("am broadcast -a ADB_INPUT_TEXT --es msg '%s'", text));
        } else {
            element.sendKeys(text);
        }
    }

    @Override
    public void click(Object element) {
        WebElement webElement = (WebElement) element;
        if (PROPERTIES.isAppiumClickAdb()) {
            // get center point of element to tap on it
            int x = webElement.getLocation().getX() + webElement.getSize().getWidth() / 2;
            int y = webElement.getLocation().getY() + webElement.getSize().getHeight() / 2;
            AdbConsole.execute(String.format("input tap %s %s", x, y));
        } else {
            webElement.click();
        }
    }

    @Override
    public void press(Object element, String keyName) {
        throw new UnsupportedOperationException("Not supported yet.");
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
