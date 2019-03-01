package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Result;
import cucumber.api.TestStep;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestStepFinished;
import cucumber.runtime.Match;
import cucumber.runtime.StepDefinitionMatch;
import gherkin.pickles.PickleStep;
import io.qameta.allure.model.Status;
import java.util.Arrays;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.allure.CategoriesInjector;
import ru.sbtqa.tag.pagefactory.allure.Category;
import ru.sbtqa.tag.pagefactory.allure.ErrorHandler;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import ru.sbtqa.tag.qautils.errors.AutotestError;

@Aspect
public class CriticalStepCheckAspect {
    private static final String STEP_FIELD_NAME = "step";
    private static final String NON_CRITICAL_CATEGORY_NAME = "Non-critical failures";
    private static final String NON_CRITICAL_CATEGORY_MESSAGE = "Some non-critical steps are failed";

    private static final Logger LOG = LoggerFactory.getLogger(CriticalStepCheckAspect.class);

    private final Category nonCriticalCategory = new Category(NON_CRITICAL_CATEGORY_NAME,
            NON_CRITICAL_CATEGORY_MESSAGE, null,
            Arrays.asList(Status.PASSED.value()));

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.runStep(..))")
    public void runStep() {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(TestStepFinished event) {
        return !event.testStep.isHook();
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendCaseFinished(TestCaseFinished event) {
        return event.result.isOk(true);
    }

    @Around("runStep()")
    public void runStep(ProceedingJoinPoint joinPoint) throws Throwable {
        Match match = (Match) joinPoint.getThis();

        PickleStep step = (PickleStep) FieldUtils.readField(match, STEP_FIELD_NAME, true);

        if (!isCritical(step)) {
            PickleStepCustom pickleStep = (PickleStepCustom) step;
            try {
                joinPoint.proceed();
            } catch (Throwable e) {
                pickleStep.setError(e);
                FieldUtils.writeField(match, STEP_FIELD_NAME, pickleStep, true);
                ErrorHandler.attachError(e);
                ErrorHandler.attachScreenshot();
                CategoriesInjector.inject(nonCriticalCategory);
            }
        } else {
            joinPoint.proceed();
        }
    }

    private boolean isCritical(PickleStep step) {
        return !(step instanceof PickleStepCustom)
                || ((PickleStepCustom) step).isCritical();
    }

    @Around("sendCaseFinished(event)")
    public void sendCaseFinished(ProceedingJoinPoint joinPoint, TestCaseFinished event) throws Throwable {
        boolean hasFailedNonCriticalStep = event.testCase.getTestSteps().stream().filter(testStep -> !testStep.isHook())
                .map(TestStep::getPickleStep)
                .filter(pickleStep -> pickleStep instanceof PickleStepCustom)
                .anyMatch(pickleStep -> ((PickleStepCustom) pickleStep).hasError());

        if (hasFailedNonCriticalStep) {
            final Result result = new Result(Result.Type.PASSED, event.result.getDuration(),
                    new AutotestError(NON_CRITICAL_CATEGORY_MESSAGE));

            event = new TestCaseFinished(event.getTimeStamp(),
                    event.testCase, result);

            joinPoint.proceed(new Object[]{event});
        } else {
            joinPoint.proceed();
        }
    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, TestStepFinished event) throws Throwable {
        StepDefinitionMatch definitionMatch = (StepDefinitionMatch) FieldUtils.readField(event.testStep, "definitionMatch", true);
        PickleStep step = (PickleStep) FieldUtils.readField(definitionMatch, "step", true);

        if (step instanceof PickleStepCustom) {
            PickleStepCustom pickleStep = (PickleStepCustom) step;
            if (pickleStep.hasLog()) {
                LOG.warn(pickleStep.getLog());
            }
            if (pickleStep.hasError()) {
                ((PickleStepCustom) event.testStep.getPickleStep()).setError(((PickleStepCustom) step).getError());
                final Result result = new Result(Result.Type.AMBIGUOUS,
                        event.result.getDuration(),
                        pickleStep.getError());

                event = new TestStepFinished(event.getTimeStamp(),
                        event.testStep, result);
                joinPoint.proceed(new Object[]{event});
            } else {
                joinPoint.proceed();
            }
        } else {
            joinPoint.proceed();
        }
    }
}
