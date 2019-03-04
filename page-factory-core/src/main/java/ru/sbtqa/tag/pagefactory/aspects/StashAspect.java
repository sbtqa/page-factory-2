package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Result;
import cucumber.api.Scenario;
import cucumber.api.TestStep;
import cucumber.api.event.Event;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.runner.EventBus;
import cucumber.runner.PickleTestStep;
import cucumber.runtime.Argument;
import cucumber.runtime.StepDefinitionMatch;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleStep;
import gherkin.pickles.PickleString;
import gherkin.pickles.PickleTable;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.data.DataFactory;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import static java.lang.String.format;

@Aspect
public class StashAspect {

    private final static String PATH_PARSE_REGEX = "(?:\\#\\{([^\\}]+)\\})";
    private final static String STASH_KEY_FULL_PATH = "#{%s}";
    private String message = null;

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

    @Around("run(bus,language,scenario,skipSteps)")
    public Object run(ProceedingJoinPoint joinPoint, EventBus bus, String language, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (!testStep.isHook()) {
            PickleStepCustom step = new PickleStepCustom(testStep.getPickleStep(), skipSteps);
            FieldUtils.writeField(testStep, "step", step, true);
        }
        return joinPoint.proceed();
    }

    @Pointcut("execution(* cucumber.api.TestStep.executeStep(..)) && args(language,scenario,skipSteps,..)")
    public void executeStep(String language, Scenario scenario, boolean skipSteps) {
    }

    @Around("executeStep(language,scenario,skipSteps)")
    public Object executeStep(ProceedingJoinPoint joinPoint, String language, Scenario scenario, boolean skipSteps) throws Throwable {
        TestStep testStep = (TestStep) joinPoint.getThis();
        if (!testStep.isHook() && testStep.getPickleStep() instanceof PickleStepCustom) {
            PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
            if (step.hasError()) {
                throw (step.getError());
            }
        }
        return joinPoint.proceed();
    }

    @Around("sendStepStart(event)")
    public void sendStepStart(ProceedingJoinPoint joinPoint, TestStepStarted event) throws Throwable {
        PickleTestStep testStep = (PickleTestStep) event.testStep;
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();

        List<Argument> replacedArguments = new ArrayList<>();
        String stepText = testStep.getStepText();
        int offset = 0;

        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);
        StringBuilder replacedValue = new StringBuilder(stepText);


//        testStep.getDefinitionArgument().stream()
//                .filter(argument -> argument.getVal() != null)

        for (Argument argument : testStep.getDefinitionArgument()) {
            Argument arg = argument;
            if ((arg.getVal() != null)) {
                if (stepDataPattern.matcher(argument.getVal()).find() && stepDataMatcher.find()) {

                    String stashKey = stepDataMatcher.group(1);
                    String stashValue = getStashValue(step, stashKey);

                    if (!stashValue.isEmpty()) {

                        String stashKeyFullPath = format(STASH_KEY_FULL_PATH, stashKey);

                        if (stashKeyFullPath.equals(argument.getVal())) {
                            arg = new Argument(stepDataMatcher.start(), stashValue);
                            offset = stashValue.length() - stashKeyFullPath.length();
                        }

                        replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
                    } else {
                        break;
                    }
                    stepDataMatcher = stepDataPattern.matcher(replacedValue);
                } else {
                    arg = new Argument(arg.getOffset() + offset, arg.getVal());
                }
            }
            replacedArguments.add(arg);
        }
        replacePickleArguments(step);

        FieldUtils.writeField(FieldUtils.readField(testStep, "definitionMatch", true),
                "arguments", replacedArguments, true);
        FieldUtils.writeField(step, "text", replaceDataPlaceholders(step, stepText), true);

        if (message != null) {
            saveMessage(step, message);
            message = null;
        }
        joinPoint.proceed(new Object[]{event});
    }

    private void replacePickleArguments(PickleStepCustom step) throws IllegalAccessException {
        for (gherkin.pickles.Argument argument : step.getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                replaceDataTable(step, (PickleTable) argument);
            } else {
                if (argument.getClass().equals(PickleString.class)) {
                    String content = replaceDataPlaceholders(step, ((PickleString) argument).getContent());
                    FieldUtils.writeField(argument, "content", content, true);
                }
            }
        }
    }

    private void replaceDataTable(PickleStepCustom step, PickleTable argument) throws IllegalAccessException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceDataPlaceholders(step, pickleCell.getValue()), true);
            }
        }
    }

    private String replaceDataPlaceholders(PickleStepCustom step, String raw) {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedValue = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String stashKey = stepDataMatcher.group(1);
            String stashValue = getStashValue(step, stashKey);
            if (stashValue == null) {
                break;
            }
            replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
            stepDataMatcher = stepDataPattern.matcher(replacedValue);
        }
        return replacedValue.toString();
    }


    private String getStashValue(PickleStepCustom step, String stashKey) {
        Object stashValue = Stash.getValue(stashKey);
        String result = null;

        if (stashValue == null) {
            message = "Stash value not found by key: " + stashKey;
        } else {
            if (stashValue instanceof String) {
                result = (String) stashValue;
            } else {
                message = "The value received by the key must be a string. Key: " + stashKey;
            }
        }
        return result;
    }

    private void saveMessage(PickleStepCustom step, String message){
        if (step.isSkipped()) {
            step.setLog(message);
        } else {
            step.setError(new AutotestError(message));
        }
    }

    @Around("sendStepFinished(event)")
    public void sendStepFinished(ProceedingJoinPoint joinPoint, TestStepFinished event) throws Throwable {
        PickleStep step = event.testStep.getPickleStep();

        joinPoint.proceed();

        if (step instanceof PickleStepCustom && ((PickleStepCustom) step).hasLog()) {
            LOG.warn(((PickleStepCustom) event.testStep.getPickleStep()).getLog());
        }
    }
}
