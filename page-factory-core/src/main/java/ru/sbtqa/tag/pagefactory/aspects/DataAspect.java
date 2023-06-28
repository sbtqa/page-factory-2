package ru.sbtqa.tag.pagefactory.aspects;

import com.rits.cloning.Cloner;
import cucumber.api.*;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.runner.EventBus;
import cucumber.runtime.StepDefinitionMatch;
import gherkin.events.PickleEvent;
import gherkin.pickles.PickleStep;
import gherkin.pickles.PickleTag;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.data.DataReplacer;
import ru.sbtqa.tag.pagefactory.data.DataUtils;
import ru.sbtqa.tag.pagefactory.optional.PickleStepTag;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
public class DataAspect {
    private static final Logger LOG = LoggerFactory.getLogger(DataAspect.class);
    DataReplacer dataParser = new DataReplacer();
    HashMap<Integer, Boolean> replaced = new HashMap<>();
    HashMap<Integer, PickleEvent> pickleEvent = new HashMap<>();
    HashMap<Integer, Object> stepDefinitionMatch = new HashMap<>();
    HashMap<Integer, Object> definitionMatch = new HashMap<>();

    HashMap<Integer, PickleEvent> testCasePickle = new HashMap<>();



    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepStart(TestStepStarted event) {
        return !(event.testStep instanceof HookTestStep);
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..)")
    public static void sendCaseStart(TestCaseStarted event) {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..)")
    public static void sendCaseFinish(TestCaseFinished event) {
    }

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepFinished(TestStepFinished event) {
        return !(event.testStep instanceof HookTestStep);
    }

    @Pointcut("execution(* cucumber.runner.TestCase.run(..)) && args(bus,..)")
    public void runTestCase(EventBus bus) {
    }

    @Pointcut("execution(* cucumber.runner.TestStep.run(..)) && args(testCase,bus,scenario,skipSteps,..)")
    public void run(TestCase testCase, EventBus bus, cucumber.api.Scenario scenario, boolean skipSteps) {
    }

    @Pointcut("execution(* cucumber.runner.TestStep.executeStep(..)) && args(scenario,skipSteps,..)")
    public void executeStep(Scenario scenario, boolean skipSteps) {
    }

//    @Before("runTestCase(bus)")
//    public void runBeforeTestCase(ProceedingJoinPoint joinPoint, EventBus bus) throws Throwable {
//        System.out.println(2);
//    }
//
//    @After("runTestCase(bus)")
//    public void runAfterTestCase(ProceedingJoinPoint joinPoint, EventBus bus) throws Throwable {
//        System.out.println(3);
//    }

    @Around("runTestCase(bus)")
    public void runBeforeTestCase(ProceedingJoinPoint joinPoint, EventBus bus) throws Throwable {
        System.out.println(1);
        joinPoint.proceed();
        System.out.println(2);
    }



    @Around("sendCaseStart(event)")
    public Object run(ProceedingJoinPoint joinPoint, TestCaseStarted event) throws Throwable {
        if (!pickleEvent.containsKey(event.testCase.hashCode())) {
            pickleEvent.put(event.testCase.hashCode(), (PickleEvent) new Cloner().deepClone(FieldUtils.readField(event.testCase, "pickleEvent", true)));
        }
        List<PickleTag> tags = event.testCase.getTags().stream()
                .filter(pickleTag -> pickleTag.getName().startsWith(DataUtils.DATA_TAG))
                .collect(Collectors.toList());

        if (!tags.isEmpty()) {
            String dataTagName = tags.get(tags.size() - 1).getName();
            String data = DataUtils.getDataTagValue(dataTagName);

            for (TestStep testStep : event.testCase.getTestSteps()) {
                if (!(testStep instanceof HookTestStep)) {
                    PickleStepTestStep pickleStepTestStep = (PickleStepTestStep) testStep;
                    PickleStepTag stepCustom = getPickleStepTag(pickleStepTestStep);

                    stepCustom.setDataTag(data);

                    replaceByPickleStepTag(pickleStepTestStep, stepCustom);
                }
            }
        }

        return joinPoint.proceed();
    }

    @Around("sendCaseFinish(event)")
    public Object run(ProceedingJoinPoint joinPoint, TestCaseFinished event) throws Throwable {
        FieldUtils.writeField(event.testCase, "pickleEvent", pickleEvent.get(event.testCase.hashCode()), true);
        return joinPoint.proceed();
    }

    @Before("run(testCase,bus,scenario,skipSteps)")
    public void runBefore(TestCase testCase, EventBus bus, cucumber.api.Scenario scenario, boolean skipSteps) throws Throwable {
        if (!testCasePickle.containsKey(testCase.hashCode())) {
            testCasePickle.put(testCase.hashCode(), (PickleEvent) new Cloner().deepClone(FieldUtils.readField(testCase, "pickleEvent", true)));
        }
    }

    @Around("run(testCase,bus,scenario,skipSteps)")
    public Object run(ProceedingJoinPoint joinPoint, TestCase testCase, EventBus bus, cucumber.api.Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (!(testStep instanceof HookTestStep)) {
            PickleStepTestStep pickleStepTestStep = (PickleStepTestStep) testStep;
            PickleStepTag stepCustom = getPickleStepTag(pickleStepTestStep);

            stepCustom.setSkipped(skipSteps);

            replaceByPickleStepTag(pickleStepTestStep, stepCustom);
        }

        return joinPoint.proceed();
    }

    @After("run(testCase,bus,scenario,skipSteps)")
    public void runAfter(TestCase testCase, EventBus bus, cucumber.api.Scenario scenario, boolean skipSteps) throws Throwable {
        FieldUtils.writeField(testCase, "pickleEvent", testCasePickle.get(testCase.hashCode()), true);
        System.out.println(1);
    }

    private PickleStepTag getPickleStepTag(PickleStepTestStep pickleStepTestStep) {
        PickleStep pickleStep = pickleStepTestStep.getPickleStep();
        return pickleStep instanceof PickleStepTag ? (PickleStepTag) pickleStep : new PickleStepTag(pickleStep);
    }

    private void replaceByPickleStepTag(PickleStepTestStep pickleStepTestStep, PickleStepTag stepCustom) throws IllegalAccessException {
        FieldUtils.writeField(pickleStepTestStep, "step", stepCustom, true);
    }

    @Around("executeStep(scenario,skipSteps)")
    public Object executeStep(ProceedingJoinPoint joinPoint, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (!(testStep instanceof HookTestStep)
                && testStep instanceof PickleStepTestStep
                && ((PickleStepTag) ((PickleStepTestStep) testStep).getPickleStep()).hasError()) {
            PickleStepTag pickleStepTag = (PickleStepTag) ((PickleStepTestStep) testStep).getPickleStep();
            if (FieldUtils.readField(testStep, "definitionMatch", true) instanceof StepDefinitionMatch) {
                throw pickleStepTag.getError();
            } else {
                pickleStepTag.setLog(pickleStepTag.getError().getMessage());
            }
        }
        return joinPoint.proceed();
    }

    private boolean hasError(TestStep testStep) {
        return !(testStep instanceof HookTestStep)
                && ((PickleStepTestStep) testStep).getPickleStep() instanceof PickleStepTag
                && ((PickleStepTag) testStep).hasError();
    }

    @Around("sendStepStart(event)")
    public void sendStepStart(ProceedingJoinPoint joinPoint, TestStepStarted event) throws Throwable {
        PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
        if (!replaced.containsKey(testStep.hashCode())) {
            definitionMatch.put(testStep.hashCode(), new Cloner().deepClone(FieldUtils.readField(testStep, "definitionMatch", true)));
            stepDefinitionMatch.put(testStep.hashCode(), new Cloner().deepClone(FieldUtils.readField(testStep, "stepDefinitionMatch", true)));
            replaced.put(testStep.hashCode(), dataParser.replace(testStep));
        }
        joinPoint.proceed(new Object[]{event});

    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, TestStepFinished event) throws Throwable {
        PickleStep step = ((PickleStepTestStep) event.testStep).getPickleStep();


        try {
            joinPoint.proceed();
        } catch (Exception e) {
            LOG.warn("Failed to send finished step event", e);
        }

        PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
        dataParser.replaceRevert(testStep);
        FieldUtils.writeField(testStep, "definitionMatch", definitionMatch.get(testStep.hashCode()), true);
        FieldUtils.writeField(testStep, "stepDefinitionMatch", stepDefinitionMatch.get(testStep.hashCode()), true);

        if (((PickleStepTag) step).hasLog()) {
            LOG.debug(((PickleStepTag) (((PickleStepTestStep) event.testStep).getPickleStep())).getLog());
        }
    }
}

