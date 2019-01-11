package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.allurehelper.Type;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ScreenshotUtils;

@Aspect
public class AttachScreenshot {

    private static final Configuration PROPERTIES = Configuration.create();
    private static final Logger LOG = LoggerFactory.getLogger(AttachScreenshot.class);

    @Pointcut("execution(* ru.sbtqa.tag.stepdefs.CoreSetupSteps.tearDown()) && if()")
    public static boolean attachScreenshotOnTearDown() {
        return ScenarioContext.getScenario() != null
                && ScenarioContext.getScenario().isFailed()
                && !Environment.isDriverEmpty();
    }

    @Before("attachScreenshotOnTearDown()")
    public void attach() {
        try {
            ScreenshotUtils screenshot = ScreenshotUtils.valueOf(PROPERTIES.getScreenshotStrategy().toUpperCase());
            ParamsHelper.addAttachmentToRender(screenshot.take(), "ScreenshotUtils", Type.PNG);
        } catch (Exception e) {
            LOG.error("Can't attach screenshot to allure reports", e);
        }
    }
}
