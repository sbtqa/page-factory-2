package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.WebDriver;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.Environment;

@Aspect
public class StashWindowHandles {

    @Pointcut("call(* org.openqa.selenium.WebElement.click())")
    public void click() {
    }

    @Before("click()")
    public void stash() {
        WebDriver webDriver = Environment.getDriverService().getDriver();
        Stash.put("beforeClickHandles", webDriver.getWindowHandles());
    }

}
