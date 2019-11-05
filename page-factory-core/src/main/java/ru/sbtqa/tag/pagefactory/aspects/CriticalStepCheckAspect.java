package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.HookTestStep;
import cucumber.api.Result;
import cucumber.api.TestCase;
import cucumber.api.TestStep;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestStepFinished;
import cucumber.runtime.StepDefinitionMatch;
import gherkin.pickles.PickleStep;
import io.qameta.allure.Allure;
import io.qameta.allure.internal.AllureStorage;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.sbtqa.tag.pagefactory.allure.CategoriesInjector;
import ru.sbtqa.tag.pagefactory.allure.Category;
import ru.sbtqa.tag.pagefactory.allure.ErrorHandler;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.AllureNonCriticalError;
import ru.sbtqa.tag.pagefactory.exceptions.ReadFieldError;
import ru.sbtqa.tag.pagefactory.optional.PickleStepTag;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import static io.qameta.allure.util.ResultsUtils.md5;
import static org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField;

@Aspect
public class CriticalStepCheckAspect {
    private static final String STEP_FIELD_NAME = "step";
    private static final String DEFINITION_FIELD_NAME = "definitionMatch";

    private static final String NON_CRITICAL_CATEGORY_NAME = "Non-critical failures";
    private static final String NON_CRITICAL_CATEGORY_MESSAGE = "Some steps ended with non-critical errors";

    private final Category nonCriticalCategory = new Category(NON_CRITICAL_CATEGORY_NAME,
            NON_CRITICAL_CATEGORY_MESSAGE, null,
            Arrays.asList(Status.PASSED.value()));

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.runStep(..))")
    public void runStep() {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(TestStepFinished event) {
        return !(event.testStep instanceof HookTestStep);
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendCaseFinished(TestCaseFinished event) {
        return event.result.isOk(true);
    }

    @Around("runStep()")
    public void runStep(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            joinPoint.proceed();
        } catch (Throwable e) {
            StepDefinitionMatch match = (StepDefinitionMatch) joinPoint.getThis();
            if (FieldUtils.getField(match.getClass(), STEP_FIELD_NAME, true) != null) {
                PickleStep step = (PickleStep) FieldUtils.readField(match, STEP_FIELD_NAME, true);
                if (isNonCritical(step) || e instanceof AllureNonCriticalError) {
                    attachError(getPickleStepTag(step), e);
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    private PickleStepTag getPickleStepTag(PickleStep pickleStep) {
        return pickleStep instanceof PickleStepTag ? (PickleStepTag) pickleStep : new PickleStepTag(pickleStep);
    }

    private boolean isNonCritical(PickleStep step) {
        return step instanceof PickleStepTag && ((PickleStepTag) step).isNonCritical();
    }

    private void attachError(PickleStepTag step, Throwable e) {
        step.setError(e);
        ErrorHandler.attachError(e);
        if (!Environment.isDriverEmpty()) {
            ErrorHandler.attachScreenshot();
        }
        CategoriesInjector.inject(nonCriticalCategory);
    }

    @Around("sendCaseFinished(event)")
    public void sendCaseFinished(ProceedingJoinPoint joinPoint, TestCaseFinished event) throws Throwable {
        boolean hasFailedNonCriticalStep = hasFailedNonCriticalStep(event.testCase);

        if (hasFailedNonCriticalStep) {
            final Result result = new Result(Result.Type.PASSED, event.result.getDuration(),
                    new AutotestError(NON_CRITICAL_CATEGORY_MESSAGE));
            event = new TestCaseFinished(event.getTimeStamp(), event.testCase, result);

            Allure.getLifecycle().updateTestCase(getCurrentTestCaseUid(event.testCase),
                    testResult -> testResult.setStatus(Status.PASSED));

            joinPoint.proceed(new Object[]{event});
        } else {
            joinPoint.proceed();
        }
    }

    private boolean hasFailedNonCriticalStep(TestCase testCase) {
        return testCase.getTestSteps().stream()
                .filter(testStep -> !(testStep instanceof HookTestStep))
                .map(this::getDefinitionMatchStep)
                .anyMatch(this::hasError);
    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, TestStepFinished event) throws Throwable {
        PickleStep step = getDefinitionMatchStep(event.testStep);

        if (hasError(step)) {
            final Result result = new Result(Result.Type.AMBIGUOUS,
                    event.result.getDuration(), ((PickleStepTag) step).getError());
            event = new TestStepFinished(event.getTimeStamp(), event.getTimeStampMillis(), event.getTestCase(), event.testStep, result);
        }
        joinPoint.proceed(new Object[]{event});
    }

    private boolean hasError(PickleStep step) {
        return step instanceof PickleStepTag
                && ((PickleStepTag) step).hasError();
    }

    private PickleStep getDefinitionMatchStep(TestStep testStep) {
        try {
            StepDefinitionMatch definitionMatch = (StepDefinitionMatch) FieldUtils.readField(testStep, DEFINITION_FIELD_NAME, true);
            return (PickleStep) FieldUtils.readField(definitionMatch, STEP_FIELD_NAME, true);
        } catch (IllegalAccessException ex) {
            throw new ReadFieldError("Error reading the field");
        }
    }

    private String getCurrentTestCaseUid(TestCase testCase) throws IllegalAccessException {
        final String testCaseLocation = testCase.getUri() + ":" + testCase.getLine();
        String uid = md5(testCaseLocation);

        AllureStorage allureStorage = (AllureStorage) readDeclaredField(Allure.getLifecycle(), "storage", true);
        Map<String, Object> storage = (Map<String, Object>) readDeclaredField(allureStorage, "storage", true);
        Collection<Object> testResults = storage.values();
        Optional<Object> testResultOptional = testResults.stream()
                .filter(o -> o instanceof TestResult && ((TestResult) o).getHistoryId().equals(uid)).findFirst();
        return testResultOptional.isPresent() ? ((TestResult) testResultOptional.get()).getUuid() : uid;
    }
}
