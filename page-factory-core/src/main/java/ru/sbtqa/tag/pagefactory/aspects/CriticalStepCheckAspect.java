package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Result;
import cucumber.api.TestStep;
import cucumber.api.event.Event;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestStepFinished;
import cucumber.runner.EventBus;
import cucumber.runner.PickleTestStep;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.allure.Type;
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
    private ThreadLocal<Map<String, Throwable>> brokenTests = ThreadLocal.withInitial(HashMap::new);

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.runStep(..))")
    public void runStep() {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..)")
    public void send(Event event) {
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
                PickleStepCustom pickleStep = (PickleStepCustom) step;
                try {
                    joinPoint.proceed();
                } catch (Throwable e) {
                    AllureLifecycle lifecycle = Allure.getLifecycle();
                    Optional<String> currentTestCase = lifecycle.getCurrentTestCase();
                    if (!currentTestCase.isPresent()) {
                        throw e;
                    }
                    stepField.set(instance, pickleStep);
                    this.brokenCases.get().add(currentTestCase.get());
                    this.brokenTests.get().put(pickleStep.getText(), e);

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


    @Around("send(event)")
    public void send(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        Optional<String> currentTestCase = lifecycle.getCurrentTestCase();

        if (event instanceof TestStepFinished) {
            joinPoint.proceed();
            TestStep testStep = ((TestStepFinished) event).testStep;

            if (testStep.getClass().equals(PickleTestStep.class)) {
                String currentBrokenTest = this.brokenTests.get().keySet().stream()
                        .filter(brokenTest -> ("? " + brokenTest).equals(testStep.getPickleStep().getText()))
                        .findFirst().orElse("");

                if (!currentBrokenTest.isEmpty()) {
                    LOG.warn("Non critical step failed: " + currentBrokenTest, this.brokenTests.get().get(currentBrokenTest));
                }
            }
        } else {
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
    }

    @Attachment(value = "{name}", type = "text/html")
    private String textAttachment(String name, String throwable) {
        String errorHTML = "<div style='background-color: #ffc2c2; height: 100%'>" +
                "<pre style='color:#880b0b'>" + throwable + "</pre></div>";
        return errorHTML;
    }

}
