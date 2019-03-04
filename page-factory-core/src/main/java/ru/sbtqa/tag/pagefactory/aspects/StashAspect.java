package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Scenario;
import cucumber.api.TestStep;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.runner.EventBus;
import cucumber.runner.PickleTestStep;
import gherkin.pickles.PickleStep;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.data.StashParser;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;

@Aspect
public class StashAspect {

    private static final Logger LOG = LoggerFactory.getLogger(StashAspect.class);

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepStart(TestStepStarted event) {
        return !event.testStep.isHook();
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(TestStepFinished event) {
        return !event.testStep.isHook();
    }

    @Pointcut("execution(* cucumber.api.TestStep.run(..)) && args(bus,language,scenario,skipSteps,..)")
    public void run(EventBus bus, String language, Scenario scenario, boolean skipSteps) {
    }

    @Pointcut("execution(* cucumber.api.TestStep.executeStep(..)) && args(language,scenario,skipSteps,..)")
    public void executeStep(String language, Scenario scenario, boolean skipSteps) {
    }

    @Around("run(bus,language,scenario,skipSteps)")
    public Object run(ProceedingJoinPoint joinPoint, EventBus bus, String language, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (!testStep.isHook()) {
            PickleStepCustom step = new PickleStepCustom(testStep.getPickleStep(), skipSteps);
            FieldUtils.writeField(testStep, "step", step, true);
        }
        return joinPoint.proceed();
    }

    @Around("executeStep(language,scenario,skipSteps)")
    public Object executeStep(ProceedingJoinPoint joinPoint, String language, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (hasError(testStep)) {
            throw ((PickleStepCustom) testStep.getPickleStep()).getError();
        }
        return joinPoint.proceed();
    }

    private boolean hasError(TestStep testStep) {
        return !testStep.isHook()
                && testStep.getPickleStep() instanceof PickleStepCustom
                && ((PickleStepCustom) testStep.getPickleStep()).hasError();
    }

    @Around("sendStepStart(event)")
    public void sendStepStart(ProceedingJoinPoint joinPoint, TestStepStarted event) throws Throwable {
        PickleTestStep testStep = (PickleTestStep) event.testStep;
        StashParser.replaceStep(testStep);
        joinPoint.proceed(new Object[]{event});
    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, TestStepFinished event) throws Throwable {
        PickleStep step = event.testStep.getPickleStep();
        joinPoint.proceed();
        if (((PickleStepCustom) step).hasLog()) {
            LOG.warn(((PickleStepCustom) event.testStep.getPickleStep()).getLog());
        }
    }
}
