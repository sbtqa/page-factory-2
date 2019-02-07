package ru.sbtqa.tag.pagefactory.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.PageFactory;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.actions.PageActions;
import ru.sbtqa.tag.pagefactory.checks.PageChecks;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;
import ru.sbtqa.tag.pagefactory.mobile.checks.MobilePageChecks;

/**
 * Inherit your mobile page objects from this class
 */
public abstract class MobilePage implements Page {

    private static PageActions pageActions = new MobilePageActions();
    private static PageChecks pageChecks = new MobilePageChecks();

    public MobilePage() {
        PageFactory.initElements((AppiumDriver<AndroidElement>) Environment.getDriverService().getDriver(), this);
        Environment.setPageActions(pageActions);
        Environment.setPageChecks(pageChecks);
    }
}