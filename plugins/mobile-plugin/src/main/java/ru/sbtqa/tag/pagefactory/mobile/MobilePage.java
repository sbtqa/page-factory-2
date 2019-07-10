package ru.sbtqa.tag.pagefactory.mobile;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.find.FindUtils;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;
import ru.sbtqa.tag.pagefactory.mobile.checks.MobilePageChecks;
import ru.sbtqa.tag.pagefactory.mobile.junit.MobileSetupSteps;

/**
 * Inherit your mobile page objects from this class
 */
public abstract class MobilePage implements Page {

    public MobilePage() {
        MobileSetupSteps.initMobile();

        MobileDriver driver =  Environment.getDriverService().getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);

        Environment.setPageActions(new MobilePageActions());
        Environment.setPageChecks(new MobilePageChecks());
        Environment.setFindUtils(new FindUtils());
    }
}