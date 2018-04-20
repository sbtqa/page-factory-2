package ru.sbtqa.tag.pagefactory;


import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.pagefactory.jdi.utils.JDIUtils;

/**
 * Contains basic ru.sbtqa.tag.pagefactory.mobile.actions in particular with jdi elements
 * If we want to extend this functional - inherit from this class
 */
public class JDIPage extends WebPage {

    public JDIPage(WebDriver driver) {
        super(driver);
        JDIUtils.initElementsOnPage(this);
    }
}
