package ru.sbtqa.tag.pagefactory.aspects.report;

import org.aeonbits.owner.ConfigFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.allurehelper.Type;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ScreenshotUtils;

@Aspect
public class AttachScreenshot {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    @Pointcut("execution(* ru.sbtqa.tag.stepdefs.CoreSetupSteps.tearDown()) && if()")
    public static boolean attachScreenshotOnTearDown() {
        return ScenarioContext.getScenario().isFailed();
    }

    @Before("attachScreenshotOnTearDown()")
    public void attach() {
        ScreenshotUtils screenshot = ScreenshotUtils.valueOf(PROPERTIES.getScreenshotStrategy().toUpperCase());
        ParamsHelper.addAttachmentToRender(screenshot.take(), "ScreenshotUtils", Type.PNG);
    }
}
