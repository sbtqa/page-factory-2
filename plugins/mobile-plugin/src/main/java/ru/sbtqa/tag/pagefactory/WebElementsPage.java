package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.annotations.ActionTitle;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.utils.ReflectionUtils;

/**
 * Contains basic actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class MobilePage extends Page {

    private static final Logger LOG = LoggerFactory.getLogger(MobilePage.class);

    /**
     * Find element with specified title annotation, and fill it with given text
     * Add elementTitle-&gt;text as parameter-&gt;value to corresponding step in
     * allure report
     *
     * @param elementTitle element to fill
     * @param text text to enter
     * @throws ru.sbtqa.tag.pagefactory.exceptions.PageException if page was not
     * initialized, or required element couldn't be found
     */
    @ActionTitle("ru.sbtqa.tag.pagefactory.fill.field")
    public void fillField(String elementTitle, String text) throws PageException {
        WebElement element = ReflectionUtils.getElementByTitle(PageContext.getCurrentPage(), elementTitle);
        element.click();


        if (TagMobileDriver.getAppiumClickAdb()) {
            // set ADBKeyBoard as default
            AdbConsole.execute("ime set com.android.adbkeyboard/.AdbIME");
            // send broadcast intent via adb
            AdbConsole.execute(String.format("am broadcast -a ADB_INPUT_TEXT --es msg '%s'", text));
        } else {
            element.sendKeys(text);
        }
    }
}