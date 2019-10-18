package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Scenario;
import cucumber.api.TestStep;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.runner.EventBus;
import cucumber.runner.PickleTestStep;
import cucumber.runtime.Match;
import cucumber.runtime.StepDefinitionMatch;
import gherkin.pickles.PickleStep;
import gherkin.pickles.PickleTag;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.data.DataReplacer;
import ru.sbtqa.tag.pagefactory.data.DataUtils;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;

@Aspect
public class DataAspect {

    private static final Logger LOG = LoggerFactory.getLogger(DataAspect.class);

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepStart(TestStepStarted event) {
        return !event.testStep.isHook();
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..)")
    public static void sendCaseStart(TestCaseStarted event) {
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

    @Around("sendCaseStart(event)")
    public Object run(ProceedingJoinPoint joinPoint, TestCaseStarted event) throws Throwable {
        List<PickleTag> tags = event.testCase.getTags().stream()
                .filter(pickleTag -> pickleTag.getName().startsWith(DataUtils.DATA_TAG))
                .collect(Collectors.toList());

        if (!tags.isEmpty()) {
            String dataTagName = tags.get(tags.size() - 1).getName();
            String data = DataUtils.getDataTagValue(dataTagName);

            for (TestStep step : event.testCase.getTestSteps()) {
                if (!step.isHook()) {
                    PickleStepCustom stepCustom = changePickleStep(step.getPickleStep());
                    stepCustom.setData(data);
                    writePickleStep(step, stepCustom);
                }
            }
        }
        return joinPoint.proceed();
    }

    private void writePickleStep(TestStep step, PickleStepCustom pickleStep) throws IllegalAccessException {
        FieldUtils.writeField(step, "step", pickleStep, true);
    }

    @Around("run(bus,language,scenario,skipSteps)")
    public Object run(ProceedingJoinPoint joinPoint, EventBus bus, String language, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (!testStep.isHook()) {
            PickleStepCustom stepCustom = changePickleStep(testStep.getPickleStep());
            stepCustom.setSkipped(skipSteps);
            writePickleStep(testStep, stepCustom);
        }
        return joinPoint.proceed();
    }

    private PickleStepCustom changePickleStep(PickleStep step) {
        return step instanceof PickleStepCustom ? (PickleStepCustom) step : new PickleStepCustom(step);
    }

    @Around("executeStep(language,scenario,skipSteps)")
    public Object executeStep(ProceedingJoinPoint joinPoint, String language, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (hasError(testStep)) {
            PickleStepCustom pickleStepCustom = (PickleStepCustom) testStep.getPickleStep();
            Match definitionMatch = (Match) FieldUtils.readField(testStep, "definitionMatch", true);
            if (definitionMatch.getClass().equals(StepDefinitionMatch.class)) {
                throw pickleStepCustom.getError();
            } else {
                pickleStepCustom.setLog(pickleStepCustom.getError().getMessage());
            }
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
        DataReplacer dataParser = new DataReplacer();
        dataParser.replace(testStep);
        joinPoint.proceed(new Object[]{event});
    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, TestStepFinished event) throws Throwable {
        PickleStep step = event.testStep.getPickleStep();

        try {
            joinPoint.proceed();
        } catch (Exception e) {
            LOG.warn("Failed to send finished step event", e);
        }

        if (((PickleStepCustom) step).hasLog()) {
            LOG.warn(((PickleStepCustom) event.testStep.getPickleStep()).getLog());
        }
    }
}
