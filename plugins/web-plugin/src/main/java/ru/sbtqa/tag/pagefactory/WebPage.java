package ru.sbtqa.tag.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.sbtqa.tag.pagefactory.web.checks.WebPageChecks;

/**
 * Inherit your web page objects from this class
 */
public abstract class WebPage implements Page {

    private static PageActions pageActions = new WebPageActions();
    private static PageChecks pageChecks = new WebPageChecks();

    public WebPage() {
        PageFactory.initElements((WebDriver) Environment.getDriverService().getDriver(), this);
        Environment.setPageActions(pageActions);
        Environment.setPageChecks(pageChecks);
    }

    public WebPage(FieldDecorator decorator) {
        PageFactory.initElements(decorator, this);
    }
}