package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.sbtqa.tag.pagefactory.TestEnvironment;
import ru.sbtqa.tag.pagefactory.web.properties.Configuration;

@Aspect
public class ClickViaSeleniumActions {

    private static final Configuration properties = ConfigFactory.create(Configuration.class);

    @Pointcut("call(* org.openqa.selenium.WebElement.click()) && if()")
    public static boolean isClickViaSeleniumActionsEnabled() {
        return properties.isClickViaSeleniumActionsEnabled();
    }

    @Around("isClickViaSeleniumActionsEnabled()")
    public void actions(ProceedingJoinPoint joinPoint) throws Throwable {
        WebElement element = (WebElement) joinPoint.getTarget();
        Actions actions = new Actions((WebDriver) TestEnvironment.getDriverService().getDriver());
        actions.moveToElement(element);
        actions.click();
        actions.build().perform();
    }

}
