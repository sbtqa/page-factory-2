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
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.pagefactory.extensions.WebExtension;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.qautils.properties.Props;

@Aspect
public class ClickAspect {

//    TODO remove unnecessary symbols
    @Pointcut("call(* org.openqa.selenium.WebElement.click()) || call(* ru.yandex.qatools.htmlelements.element.*.click()) ")
    public void clickMethod() {
    }

    @Around("clickMethod()")
    public void doAroundClick(ProceedingJoinPoint joinPoint) throws Throwable {
        
        // Redirect
        WebElement targetWebElement;
        targetWebElement = (WebElement) joinPoint.getTarget();
    
        // TODO PL 5/30/17 REMOVE REDIRECT
//        Class<? extends Page> elementRedirect;
//        if (joinPoint.getTarget() instanceof TypifiedElement) {
//            targetWebElement = ((TypifiedElement) joinPoint.getTarget()).getWrappedElement();
//            TypifiedElement typifiedElement = (TypifiedElement) joinPoint.getTarget();
//            elementRedirect = getElementRedirect(typifiedElement);
//        } else if (joinPoint.getTarget() instanceof WebElement) {
//            targetWebElement = (WebElement) joinPoint.getTarget();
//            elementRedirect = getElementRedirect(targetWebElement);
//        } else {
//            return;
//        }

        String elementHighlightStyle = null;
        boolean isVideoHighlightEnabled = Boolean.valueOf(Props.get("video.highlight.enabled"));
        if (isVideoHighlightEnabled) {
            elementHighlightStyle = WebExtension.highlightElementOn(targetWebElement);
        }

        if (PageFactory.getEnvironment() == Environment.WEB) {
            Stash.put("beforeClickHandles", PageFactory.getWebDriver().getWindowHandles());
        }

        if (!PageFactory.isAspectsDisabled()) {
            Actions actions = new Actions(PageFactory.getWebDriver());
            if ("IE".equals(TagWebDriver.getBrowserName())) {
                Dimension size = PageFactory.getWebDriver().manage().window().getSize();
                Point elementLocation = (targetWebElement).getLocation();
                Dimension elementSize = (targetWebElement).getSize();
                //scroll to invisible element
                if (size.getHeight() < (elementLocation.getY() + elementSize.getHeight() + 200)) {
                    ((JavascriptExecutor) PageFactory.getWebDriver()).
                          executeScript("window.scroll(" + elementLocation.getX() + ","
                                + (elementLocation.getY() - 200) + ");");
                }
            }

            switch (TagWebDriver.getBrowserName()) {
                case "Chrome":
                case "IE":
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
    
        // TODO PL 6/6/17
//        if (null != elementRedirect) {
//            PageFactory.getInstance().getPage(elementRedirect);
//        }

        if (isVideoHighlightEnabled) {
            WebExtension.highlightElementOff(targetWebElement, elementHighlightStyle);
        }
    }

}
