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
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
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
        Object instance = joinPoint.getThis();
        if (instance != null && FieldUtils.getDeclaredField(instance.getClass(), FIELD_NAME, true) != null) {
            Field stepField = FieldUtils.getDeclaredField(instance.getClass(), FIELD_NAME, true);
            stepField.setAccessible(true);
            Object step = stepField.get(instance);

            if (!(step instanceof PickleStepCustom)
                    || ((PickleStepCustom) step).isCritical()) {
                joinPoint.proceed();
            } else {
                PickleStepCustom pickle = (PickleStepCustom) step;
                try {
                    joinPoint.proceed();
                } catch (Throwable e) {
                    AllureLifecycle lifecycle = Allure.getLifecycle();
                    Optional<String> currentTestCase = lifecycle.getCurrentTestCase();
                    if (!currentTestCase.isPresent()) {
                        throw e;
                    }
                    stepField.set(instance, pickle);
                    this.brokenCases.get().add(currentTestCase.get());
                    LOG.warn("Non-critical step failed", e);

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
        AllureLifecycle lifecycle = Allure.getLifecycle();
        Optional<String> currentTestCase = lifecycle.getCurrentTestCase();

        if (event instanceof TestCaseFinished
                && ((TestCaseFinished) event).result.getStatus() == Result.Type.PASSED
                && currentTestCase.isPresent()
                && this.brokenCases.get().contains(currentTestCase.get())
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
        String errorHTML = "<div style='background-color: #ffc2c2; height: 100%'>" +
                "<pre style='color:#880b0b'>" + throwable + "</pre></div>";
        return errorHTML;
    }

}
