package ru.sbtqa.tag.pagefactory.aspects.report;

import cucumber.api.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.allurehelper.Type;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ScreenshotUtils;

@Aspect
public class AttachScreenshot {

    private static final Configuration PROPERTIES = Configuration.create();
    private static final Logger LOG = LoggerFactory.getLogger(AttachScreenshot.class);

    private void attach() {
        try {
            ScreenshotUtils screenshot = ScreenshotUtils.valueOf(PROPERTIES.getScreenshotStrategy().toUpperCase());
            ParamsHelper.addAttachmentToRender(screenshot.take(), "Screenshot of failed step", Type.PNG);
        } catch (Exception e) {
            LOG.error("Can't attach screenshot to allure reports", e);
        }
    }

    @Around("call(* cucumber.runtime.junit.JUnitReporter.handleStepResult(..))")
    public Object errorAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object arg = (Result) joinPoint.getArgs()[1];
        if (((Result) arg).getStatus() == Result.Type.FAILED && !Environment.isDriverEmpty()) {
            attach();
        }
        return joinPoint.proceed();
    }
}
