package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.WebElement;
import ru.sbtqa.tag.pagefactory.WebExtension;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

@Aspect
public class Highlight {

    private static final WebConfiguration properties = ConfigFactory.create(WebConfiguration.class);

    @Pointcut("call(* org.openqa.selenium.WebElement.click()) && if()")
    public static boolean isHighlighEnabled() {
        return properties.isHighlightEnabled();
    }

    @Around("isHighlighEnabled()")
    public void highlight(ProceedingJoinPoint joinPoint) throws Throwable {
        WebElement webElement = (WebElement) joinPoint.getTarget();
        String originalStyle = WebExtension.getElementBorderStyle(webElement);

        WebExtension.highlightElementOn(webElement);

        joinPoint.proceed();

        WebExtension.highlightElementOff(webElement, originalStyle);
    }

}
