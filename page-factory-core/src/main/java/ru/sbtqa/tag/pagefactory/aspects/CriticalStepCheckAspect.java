package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Result;
import cucumber.api.TestCase;
import cucumber.api.event.Event;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestStepFinished;
import gherkin.pickles.PickleStep;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.internal.AllureStorage;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.allure.CategoriesInjector;
import ru.sbtqa.tag.pagefactory.allure.Category;
import ru.sbtqa.tag.pagefactory.allure.ErrorHandler;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static io.qameta.allure.util.ResultsUtils.md5;

@Aspect
public class CriticalStepCheckAspect {
    private static final String STEP_FIELD_NAME = "step";
    private static final String NON_CRITICAL_CATEGORY_NAME = "Non-critical failures";
    private static final String NON_CRITICAL_CATEGORY_MESSAGE = "Some non-critical steps are failed";
    private final Category nonCriticalCategory = new Category(NON_CRITICAL_CATEGORY_NAME,
            NON_CRITICAL_CATEGORY_MESSAGE, null,
            Arrays.asList(Status.PASSED.value()));

    private ThreadLocal<Pair<PickleStep, Throwable>> currentFailedNonCritical = new ThreadLocal<>();

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.runStep(..))")
    public void runStep() {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(Event event) {
        return event instanceof TestStepFinished;
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendCaseFinished(Event event) {
        return event instanceof TestCaseFinished;
    }

    @Around("runStep()")
    public void runStep(ProceedingJoinPoint joinPoint) throws Throwable {
        Object instance = joinPoint.getThis();
        if (instance != null && FieldUtils.getDeclaredField(instance.getClass(), STEP_FIELD_NAME, true) != null) {
            Field stepField = FieldUtils.getDeclaredField(instance.getClass(), STEP_FIELD_NAME, true);
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
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    String message = cause.getMessage() == null || cause.getMessage().isEmpty()
                            ? e.getMessage() : cause.getMessage();

                    stepField.set(instance, pickleStep);
                    this.currentFailedNonCritical.set(Pair.of(pickleStep.step, cause));

                    ErrorHandler.attachError(message, cause);
                    ErrorHandler.attachScreenshot();

                    CategoriesInjector.inject(nonCriticalCategory);
                }
            }
        } else {
            joinPoint.proceed();
        }
    }

    @Around("sendCaseFinished(event)")
    public void sendCaseFinished(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        TestCaseFinished testCaseFinished = (TestCaseFinished) event;

        if (currentFailedNonCritical.get() != null && testCaseFinished.result.isOk(true)) {
            currentFailedNonCritical.set(null);
            final Result result = new Result(Result.Type.PASSED, testCaseFinished.result.getDuration(),
                    new AutotestError(NON_CRITICAL_CATEGORY_MESSAGE));

            event = new TestCaseFinished(event.getTimeStamp(),
                    testCaseFinished.testCase, result);

            Allure.getLifecycle().updateTestCase(getCurrentTestCaseUid(testCaseFinished.testCase),
                    testResult -> testResult.setStatus(Status.PASSED));
            joinPoint.proceed(new Object[]{event});
        } else {
            joinPoint.proceed();
        }
    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        TestStepFinished testStepFinished = (TestStepFinished) event;
        if (testStepFinished.testStep.isHook() || currentFailedNonCritical.get() == null
                || !testStepFinished.testStep.getPickleStep().equals(currentFailedNonCritical.get().getLeft())) {
            joinPoint.proceed();
        } else {
            final Result result = new Result(Result.Type.AMBIGUOUS,
                    testStepFinished.result.getDuration(),
                    currentFailedNonCritical.get().getRight());

            event = new TestStepFinished(event.getTimeStamp(),
                    ((TestStepFinished) event).testStep, result);

            joinPoint.proceed(new Object[]{event});
        }
    }

    private String getCurrentTestCaseUid(TestCase testCase) throws IllegalAccessException {
        final String testCaseLocation = testCase.getUri() + ":" + testCase.getLine();
        String uid = md5(testCaseLocation);

        AllureStorage allureStorage = (AllureStorage) FieldUtils
                .readDeclaredField(Allure.getLifecycle(), "storage", true);
        Map<String, Object> storage = (Map<String, Object>) FieldUtils
                .readDeclaredField(allureStorage, "storage", true);
        Collection<Object> testResults = storage.values();
        Optional<Object> testResultOptional = testResults.stream()
                .filter(o -> o instanceof TestResult && ((TestResult) o).getHistoryId().equals(uid)).findFirst();
        return testResultOptional.isPresent() ? ((TestResult) testResultOptional.get()).getUuid() : uid;
    }
}
