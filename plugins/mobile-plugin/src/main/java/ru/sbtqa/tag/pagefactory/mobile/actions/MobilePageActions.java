package ru.sbtqa.tag.pagefactory.mobile.actions;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.properties.MobileConfiguration;
import ru.sbtqa.tag.pagefactory.mobile.support.AdbConsole;

public class MobilePageActions implements PageActions {

    private static final MobileConfiguration PROPERTIES = MobileConfiguration.create();

    @Override
    public void fill(Object element, String text) {
        WebElement webElement = (WebElement) element;
        webElement.click();

        if (PROPERTIES.isAppiumFillAdb()) {
            fillViaAdb(text);
        } else {
            AppiumDriver driver = Environment.getDriverService().getDriver();
            driver.getKeyboard().sendKeys(text);
        }
    }

    private void fillViaAdb(String text) {
        // set ADBKeyBoard as default
        AdbConsole.execute("ime set com.android.adbkeyboard/.AdbIME");
        // send broadcast intent via adb
        AdbConsole.execute(String.format("am broadcast -a ADB_INPUT_TEXT --es msg '%s'", text));
    }

    @Override
    public void click(Object element) {
        WebElement webElement = (WebElement) element;
        if (PROPERTIES.isAppiumClickAdb()) {
            clickViaAdb(webElement);
        } else {
            webElement.click();
        }
    }

    private void clickViaAdb(WebElement webElement) {
        // get center point of element to tap on it
        int x = webElement.getLocation().getX() + webElement.getSize().getWidth() / 2;
        int y = webElement.getLocation().getY() + webElement.getSize().getHeight() / 2;
        AdbConsole.execute("ime set com.android.adbkeyboard/.AdbIME");
        AdbConsole.execute(String.format("input tap %s %s", x, y));
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
