package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.environment.Environment;

/**
 * Contains basic ru.sbtqa.tag.pagefactory.mobile.actions in particular with web elements
 * If we want to extend this functional - inherit from this class
 */
public abstract class WebPage implements Page {

    public WebPage() {
        PageFactory.initElements((WebDriver) Environment.getDriverService().getDriver(), this);
    }

    public WebPage(FieldDecorator decorator) {
        PageFactory.initElements(decorator, this);
    }
}