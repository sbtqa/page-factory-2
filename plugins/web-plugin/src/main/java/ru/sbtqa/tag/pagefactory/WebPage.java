package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.environment.Environment;

/**
 * Inherit your web page objects from this class
 */
public interface WebPage extends Page {

    public WebPage() {
        PageFactory.initElements((WebDriver) Environment.getDriverService().getDriver(), this);
    }

    public WebPage(FieldDecorator decorator) {
        PageFactory.initElements(decorator, this);
    }
}