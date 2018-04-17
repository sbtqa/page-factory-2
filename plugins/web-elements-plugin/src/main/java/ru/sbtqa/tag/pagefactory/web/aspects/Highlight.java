package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;
import ru.sbtqa.tag.pagefactory.web.utils.WebExpectedConditionsUtils;

@Aspect
public class Highlight {

    private static final WebConfiguration PROPERTIES = ConfigFactory.create(WebConfiguration.class);
    private static final Logger LOG = LoggerFactory.getLogger(WebExpectedConditionsUtils.class);

    @Pointcut("call(* org.openqa.selenium.WebElement.click()) && if()")
    public static boolean isHighlighEnabled() {
        return PROPERTIES.isHighlightEnabled();
    }

    @Around("isHighlighEnabled()")
    public void highlight(ProceedingJoinPoint joinPoint) throws Throwable {
        WebElement webElement = (WebElement) joinPoint.getTarget();
        String originalStyle = getElementBorderStyle(webElement);

        highlightElementOn(webElement);

        joinPoint.proceed();

        highlightElementOff(webElement, originalStyle);
    }

    private String getElementBorderStyle(WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) PageContext.getCurrentPage().getDriver();
        return (String) js.executeScript("return arguments[0].style.border", webElement);
    }

    private void highlightElementOn(WebElement webElement) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) PageContext.getCurrentPage().getDriver();
            js.executeScript("arguments[0].style.border='3px solid red'", webElement);
        } catch (Exception e) {
            LOG.warn("Something went wrong with element highlight", e);
        }
    }

    private void highlightElementOff(WebElement webElement, String originalStyle) {
        originalStyle = (originalStyle == null) ? "" : originalStyle;
        try {
            JavascriptExecutor js = (JavascriptExecutor) PageContext.getCurrentPage().getDriver();
            js.executeScript("arguments[0].style.border='" + originalStyle + "'", webElement);
        } catch (Exception e) {
            LOG.debug("Something went wrong with element highlight", e);
        }
    }
}
