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
import ru.sbtqa.tag.datajack.exceptions.DataException;
//import ru.sbtqa.tag.pagefactory.data.DataFactory;
//import ru.sbtqa.tag.pagefactory.data.DataParser;
import ru.sbtqa.tag.pagefactory.data.DataFactory;
import ru.sbtqa.tag.pagefactory.data.DataParser;
import ru.sbtqa.tag.pagefactory.data.StashParser;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import ru.sbtqa.tag.qautils.errors.AutotestError;

@Aspect
public class StashAspect {

    private static final Logger LOG = LoggerFactory.getLogger(StashAspect.class);

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
                .filter(pickleTag -> pickleTag.getName().startsWith("@data="))
                .collect(Collectors.toList());

        if (!tags.isEmpty()) {
            String dataTagName = tags.get(tags.size() - 1).getName();
            String data = dataTagName.substring(dataTagName.lastIndexOf("=") + 1);

            event.testCase.getTestSteps().stream()
                    .filter(testStep -> !testStep.isHook())
                    .forEach(testStep -> {
                        try {
                            PickleStep step = testStep.getPickleStep();
                            PickleStepCustom stepCustom;
                            if (step instanceof PickleStepCustom) {
                                stepCustom = (PickleStepCustom) step;
                                stepCustom.setData(data);
                            } else {
                                stepCustom = new PickleStepCustom(testStep.getPickleStep(), data);
                            }
                            FieldUtils.writeField(testStep, "step", stepCustom, true);
                        } catch (IllegalAccessException e) {
                            throw new AutotestError("Don't write new step");
                        }
                    });
        }
        return joinPoint.proceed();
    }

    @Around("run(bus,language,scenario,skipSteps)")
    public Object run(ProceedingJoinPoint joinPoint, EventBus bus, String language, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (!testStep.isHook()) {
            PickleStep step = testStep.getPickleStep();
            PickleStepCustom stepCustom;
            if (step instanceof PickleStepCustom) {
                stepCustom = (PickleStepCustom) step;
                stepCustom.setSkipped(skipSteps);
            } else {
                stepCustom = new PickleStepCustom(testStep.getPickleStep(), skipSteps);
            }
            FieldUtils.writeField(testStep, "step", stepCustom, true);
        }
        return joinPoint.proceed();
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
        StashParser.replaceStep(testStep);
        if (DataFactory.getDataProvider() != null) {
//            DataParser dataParser = new DataParser();
            DataParser.replaceStep(testStep);
        }
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
