package ru.sbtqa.tag.pagefactory.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ClickAspect {

    @Pointcut("call(* org.openqa.selenium.WebElement.click())")
    public void clickMethod() {
    }

    @Around("clickMethod()")
    public void doAroundClick(ProceedingJoinPoint joinPoint) throws Throwable {

//        WebElement targetWebElement = (WebElement) joinPoint.getTarget();
//
//        String elementHighlightStyle = null;
//        boolean isVideoHighlightEnabled = Properties.getProperties().isVideoHighlightEnabled();
//        if (isVideoHighlightEnabled) {
//            elementHighlightStyle = WebExtension.highlightElementOn(targetWebElement);
//        }
//
//        if (PageFactory.getEnvironment() == Environment.WEB) {
//            Stash.put("beforeClickHandles", PageFactory.getWebDriver().getWindowHandles());
//        }
//
//        if (PageFactory.isAspectsEnabled()) {
//            Actions actions = new Actions(PageFactory.getWebDriver());
//            if (IE.equals(TestEnvironment.getDriverService().getDriver())) {
//                Dimension size = PageFactory.getWebDriver().manage().window().getSize();
//                Point elementLocation = (targetWebElement).getLocation();
//                Dimension elementSize = (targetWebElement).getSize();
//                //scroll to invisible element
//                if (size.getHeight() < (elementLocation.getY() + elementSize.getHeight() + 200)) {
//                    ((JavascriptExecutor) PageFactory.getWebDriver()).
//                          executeScript("window.scroll(" + elementLocation.getX() + ","
//                                + (elementLocation.getY() - 200) + ");");
//                }
//            }
//
//            switch (TestEnvironment.getDriverService().getDriver()) {
//                case CHROME:
//                case IE:
//                    actions.moveToElement(targetWebElement);
//                    actions.click();
//                    actions.build().perform();
//                    break;
//                default:
//                    joinPoint.proceed();
//            }
//        } else {
//            joinPoint.proceed();
//        }
//
//        if (isVideoHighlightEnabled) {
//            WebExtension.highlightElementOff(targetWebElement, elementHighlightStyle);
//        }
    }

}
