package ru.sbtqa.tag.pagefactory.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.PageContext;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.pagefactory.extensions.WebExtension;
import static ru.sbtqa.tag.pagefactory.support.BrowserType.CHROME;
import static ru.sbtqa.tag.pagefactory.support.BrowserType.IE;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;

@Aspect
public class ClickAspect {

    @Pointcut("call(* org.openqa.selenium.WebElement.click())")
    public void clickMethod() {
    }

    @Around("clickMethod()")
    public void doAroundClick(ProceedingJoinPoint joinPoint) throws Throwable {

        WebElement targetWebElement = (WebElement) joinPoint.getTarget();

        String elementHighlightStyle = null;
        boolean isVideoHighlightEnabled = Properties.getProperties().isVideoHighlightEnabled();
        if (isVideoHighlightEnabled) {
            elementHighlightStyle = WebExtension.highlightElementOn(targetWebElement);
        }

        if (PageFactory.getEnvironment() == Environment.WEB) {
            Stash.put("beforeClickHandles", PageContext.getCurrentPage().getDriver().getWindowHandles());
        }

        if (PageFactory.isAspectsEnabled()) {
            Actions actions = new Actions(PageContext.getCurrentPage().getDriver());
            if (IE.equals(TagWebDriver.getBrowserName())) {
                Dimension size = PageContext.getCurrentPage().getDriver().manage().window().getSize();
                Point elementLocation = (targetWebElement).getLocation();
                Dimension elementSize = (targetWebElement).getSize();
                //scroll to invisible element
                if (size.getHeight() < (elementLocation.getY() + elementSize.getHeight() + 200)) {
                    ((JavascriptExecutor) PageContext.getCurrentPage().getDriver()).
                          executeScript("window.scroll(" + elementLocation.getX() + ","
                                + (elementLocation.getY() - 200) + ");");
                }
            }

            switch (TagWebDriver.getBrowserName()) {
                case CHROME:
                case IE:
                    actions.moveToElement(targetWebElement);
                    actions.click();
                    actions.build().perform();
                    break;
                default:
                    joinPoint.proceed();
            }
        } else {
            joinPoint.proceed();
        }

        if (isVideoHighlightEnabled) {
            WebExtension.highlightElementOff(targetWebElement, elementHighlightStyle);
        }
    }

}
