package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains basic ru.sbtqa.tag.pagefactory.mobile.actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class MobilePage extends DefaultPage {

    private static final Logger LOG = LoggerFactory.getLogger(MobilePage.class);

    public MobilePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public MobilePage(WebDriver driver, FieldDecorator decorator) {
        super(driver);
        PageFactory.initElements(decorator, this);
    }

//    /**
//     * Find element with specified title annotation, and fill it with given text
//     * Add elementTitle-&gt;text as parameter-&gt;value to corresponding step in
//     * allure report
//     *
//     * @param elementTitle element to fill
//     * @param text text to enter
//     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if page was not
//     * initialized, or required element couldn't be found
//     */
//    @ActionTitle("ru.sbtqa.tag.pagefactory.fill.field")
//    public void fillField(String elementTitle, String text) throws PageException {
//        WebElement element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
//        element.click();
//
//
//        if (TagMobileDriver.getAppiumClickAdb()) {
//            // set ADBKeyBoard as default
//            AdbConsole.execute("ime set com.android.adbkeyboard/.AdbIME");
//            // send broadcast intent via adb
//            AdbConsole.execute(String.format("am broadcast -a ADB_INPUT_TEXT --es msg '%s'", text));
//        } else {
//            element.sendKeys(text);
//        }
//    }
}