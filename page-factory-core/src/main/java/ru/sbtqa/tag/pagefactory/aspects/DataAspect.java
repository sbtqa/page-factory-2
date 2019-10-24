package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.*;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.runner.EventBus;
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
        return !(event.testStep instanceof HookTestStep);
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..)")
    public static void sendCaseStart(TestCaseStarted event) {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(TestStepFinished event) {
        return !(event.testStep instanceof HookTestStep);
    }

    @Pointcut("execution(* cucumber.runner.*.run(..)) && args(testCase,bus,scenario,skipSteps,..)")
    public void run(TestCase testCase, EventBus bus, cucumber.api.Scenario scenario, boolean skipSteps) {
    }
//
//    @Pointcut("execution(* cucumber.runner.*.executeStep(..)) && args(scenario,skipSteps,..)")
//    public void executeStep(Scenario scenario, boolean skipSteps) {
//    }

    @Around("sendCaseStart(event)")
    public Object run(ProceedingJoinPoint joinPoint, TestCaseStarted event) throws Throwable {
        List<PickleTag> tags = event.testCase.getTags().stream()
                .filter(pickleTag -> pickleTag.getName().startsWith(DataUtils.DATA_TAG))
                .collect(Collectors.toList());

        if (!tags.isEmpty()) {
            String dataTagName = tags.get(tags.size() - 1).getName();
            String data = DataUtils.getDataTagValue(dataTagName);

            for (TestStep testStep : event.testCase.getTestSteps()) {
                if (!(testStep  instanceof HookTestStep)) {
                    PickleStepTestStep pickleStepTestStep = (PickleStepTestStep) testStep;
                    PickleStepCustom stepCustom = changePickleStep(pickleStepTestStep);
                    stepCustom.setData(data);
                    writePickleStep(pickleStepTestStep, stepCustom);
                }
            }
        }
        return joinPoint.proceed();
    }

    private void writePickleStep(TestStep step, PickleStepCustom pickleStep) throws IllegalAccessException {
        FieldUtils.writeField(step, "step", pickleStep, true);
    }

//    @Around("run(testCase,bus,scenario,skipSteps)")
//    public Object run(ProceedingJoinPoint joinPoint, TestCase testCase, EventBus bus, cucumber.api.Scenario scenario, boolean skipSteps) throws Throwable {
//        TestStep testStep = (TestStep) joinPoint.getThis();
//        if (!(testStep instanceof HookTestStep)) {
//            PickleStepCustom stepCustom = changePickleStep(((PickleStepTestStep) testStep).getPickleStep());
//            stepCustom.setSkipped(skipSteps);
//            writePickleStep(testStep, stepCustom);
//        }
//        return joinPoint.proceed();
//    }
//
    private PickleStepCustom changePickleStep(PickleStepTestStep step) {
        return step instanceof PickleStepCustom ? (PickleStepCustom) step : new PickleStepCustom(step.getPickleStep());
    }

//    @Around("executeStep(scenario,skipSteps)")
//    public Object executeStep(ProceedingJoinPoint joinPoint, Scenario scenario, boolean skipSteps) throws Throwable {
//        TestStep testStep = (TestStep) joinPoint.getThis();
//        if (!(testStep instanceof HookTestStep)
//                && ((PickleStepTestStep) testStep).getPickleStep() instanceof PickleStepCustom
//                && ((PickleStepCustom) ((PickleStepTestStep) testStep).getPickleStep()).hasError()) {
//        PickleStepCustom pickleStepCustom = (PickleStepCustom) ((PickleStepTestStep) testStep).getPickleStep();
////            StepDefinitionMatch definitionMatch = (StepDefinitionMatch) FieldUtils.readField(testStep, "definitionMatch", true);
//            if (FieldUtils.readField(testStep, "definitionMatch", true) instanceof StepDefinitionMatch) {
//                throw pickleStepCustom.getError();
//            } else {
//                pickleStepCustom.setLog(pickleStepCustom.getError().getMessage());
//            }
//        }
//        return joinPoint.proceed();
//    }

//    private boolean hasError(TestStep testStep) {
////        return !(testStep instanceof HookTestStep)
////                && ((PickleStepTestStep) testStep).getPickleStep() instanceof PickleStepCustom
////                && (((PickleStepTestStep) testStep).getPickleStep()). hasError();
//        return false;
//    }

    @Around("sendStepStart(event)")
    public void sendStepStart(ProceedingJoinPoint joinPoint, TestStepStarted event) throws Throwable {
        PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
        DataReplacer dataParser = new DataReplacer();
        dataParser.replace(testStep);
        joinPoint.proceed(new Object[]{event});
    }

//    @Around("sendStepFinished(event)")
//    public void sendStepFinished(ProceedingJoinPoint joinPoint, TestStepFinished event) throws Throwable {
//        PickleStep step = ((PickleStepTestStep) event.testStep).getPickleStep();
//
//        try {
//            joinPoint.proceed();
//        } catch (Exception e) {
//            LOG.warn("Failed to send finished step event", e);
//        }

//        if (((PickleStepCustom) step).hasLog()) {
////            LOG.warn(((PickleStepTestStep) event.testStep).getPickleStep()).getLog());
//            LOG.warn("error");
//        }
    }

