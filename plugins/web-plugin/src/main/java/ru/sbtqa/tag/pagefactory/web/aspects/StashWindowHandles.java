package ru.sbtqa.tag.pagefactory.web.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.UnsupportedCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

@Aspect
public class StashWindowHandles {

    private static final Logger LOG = LoggerFactory.getLogger(StashWindowHandles.class);

    @Pointcut("call(* org.openqa.selenium.WebElement.click())")
    public void click() {
    }

    @Before("click()")
    public void stash() {
        try {
            if (Environment.getDriverService() instanceof WebDriverService) {
                Stash.put("beforeClickHandles", Environment.getDriverService().getDriver().getWindowHandles());
            }
        } catch (UnsupportedCommandException e) {
            LOG.debug("Failed to stash window handles", e);
        }
    }

}
