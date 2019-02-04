package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Result;
import cucumber.api.event.Event;
import cucumber.api.event.TestCaseFinished;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.allurehelper.Type;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.utils.ScreenshotUtils;
import ru.sbtqa.tag.qautils.errors.AutotestError;

@Aspect
public class CriticalStepCheckAspect {

    private static final Configuration PROPERTIES = Configuration.create();
    private static final String FIELD_NAME = "step";
    private static final Logger LOG = LoggerFactory.getLogger(CriticalStepCheckAspect.class);

    private ThreadLocal<List<String>> brokenCases = ThreadLocal.withInitial(ArrayList::new);

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.runStep(..))")
    public void runStep() {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..))")
    public void send() {
    }

    @Around("runStep()")
    public void runStep(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.getThis() != null) {
            Object instance = joinPoint.getThis();
            Field step = instance.getClass().getDeclaredField(FIELD_NAME);
            step.setAccessible(true);
            PickleStepCustom pickle = (PickleStepCustom) step.get(instance);

            if (pickle.isCritical()) {
                joinPoint.proceed();
            } else {
                try {
                    joinPoint.proceed();
                } catch (AssertionError e) {
                    pickle.setIsFailed(true);
                    step.set(instance, pickle);
                    LOG.warn("Uncritical step failed", e);
                    this.brokenCases.get().add(Allure.getLifecycle().getCurrentTestCase().get());
                    AllureLifecycle lifecycle = Allure.getLifecycle();
                    lifecycle.updateStep(stepResult ->
                            stepResult.withStatus(Status.BROKEN));
                    this.textAttachment(e.getMessage(), ExceptionUtils.getStackTrace(e));
                    if (!Environment.isDriverEmpty()) {
                        ScreenshotUtils screenshot = ScreenshotUtils.valueOf(PROPERTIES.getScreenshotStrategy().toUpperCase());
                        ParamsHelper.addAttachmentToRender(screenshot.take(), "Screenshot", Type.PNG);
                    }

                    lifecycle.stopStep();
                }
            }
        } else {
            joinPoint.proceed();
        }
    }


    @Around("send()")
    public void send(ProceedingJoinPoint joinPoint) throws Throwable {
        Event event = (Event) joinPoint.getArgs()[0];
        if (event instanceof TestCaseFinished
                && ((TestCaseFinished) event).result.getStatus() == Result.Type.PASSED
                && this.brokenCases.get().contains(Allure.getLifecycle().getCurrentTestCase().get())
        ) {
            final Result result = new Result(Result.Type.AMBIGUOUS, ((TestCaseFinished) event).result.getDuration(),
                    new AutotestError("Some non-critical steps are failed"));

            event = new TestCaseFinished(event.getTimeStamp(),
                    ((TestCaseFinished) event).testCase, result);

            joinPoint.proceed(new Object[]{event});
        } else {
            joinPoint.proceed();
        }

    }

    @Attachment(value = "{name}", type = "text/html")
    private String textAttachment(String name, String throwable) {
        String errorHTML = "<div style='background-color: #ffc2c2'>" +
                "<pre style='color:#880b0b'>" + throwable + "</pre></div>";
        return errorHTML;
    }

}
