package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.event.Event;
import cucumber.api.event.TestStepStarted;
import cucumber.runner.PickleTestStep;
import cucumber.runtime.Argument;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleStep;
import gherkin.pickles.PickleString;
import gherkin.pickles.PickleTable;
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
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import static java.lang.String.format;

@Aspect
public class StashAspect {

    private final static String PATH_PARSE_REGEX = "(?:\\#\\{([^\\}]+)\\})";
    private final static String STASH_KEY_FULL_PATH = "#{%s}";

    @Pointcut("execution(* cucumber.runner.EventBus.send(..)) && args(event,..) && if()")
    public static boolean sendStepStart(Event event) {
        return event instanceof TestStepStarted;
    }

    @Around("sendStepStart(event)")
    public void sendStepStart(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        TestStepStarted stepStarted = (TestStepStarted) event;
        if (stepStarted.testStep instanceof PickleTestStep) {
            PickleTestStep step = (PickleTestStep) stepStarted.testStep;
            List<Argument> replacedArguments = new ArrayList<>();
            String stepText = step.getStepText();
            int offset = 0;

            Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
            Matcher stepDataMatcher = stepDataPattern.matcher(stepText);
            StringBuilder replacedValue = new StringBuilder(stepText);

            for (Argument argument : step.getDefinitionArgument()) {
                Argument arg = argument;
                if (stepDataPattern.matcher(argument.getVal()).find() && stepDataMatcher.find()) {
                    String stashValue = getStashValue(stepDataMatcher.group(1));
                    String stashKeyFullPath = format(STASH_KEY_FULL_PATH, stepDataMatcher.group(1));

                    if (stashKeyFullPath.equals(argument.getVal())) {
                        arg = new Argument(stepDataMatcher.start(), stashValue);
                        offset = stashValue.length() - stashKeyFullPath.length();
                    }
                    replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
                    stepDataMatcher = stepDataPattern.matcher(replacedValue);
                } else {
                    arg = new Argument(arg.getOffset() + offset, arg.getVal());
                }
                replacedArguments.add(arg);
            }
            replacePickleArguments(((TestStepStarted) event).testStep.getPickleStep());

            FieldUtils.writeField(FieldUtils.readField(step, "definitionMatch", true),
                    "arguments", replacedArguments, true);
            FieldUtils.writeField(step.getPickleStep(), "text", replaceDataPlaceholders(step.getStepText()), true);
        }
        joinPoint.proceed(new Object[]{event});
    }

    private void replacePickleArguments(PickleStep step) throws IllegalAccessException {
        for (gherkin.pickles.Argument argument : step.getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                replaceDataTable((PickleTable) argument);
            } else {
                if (argument.getClass().equals(PickleString.class)) {
                    FieldUtils.writeField(argument, "content", replaceDataPlaceholders(((PickleString) argument).getContent()), true);
                }
            }
        }
    }

    private void replaceDataTable(PickleTable argument) throws IllegalAccessException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceDataPlaceholders(pickleCell.getValue()), true);
            }
        }
    }

    private String replaceDataPlaceholders(String raw) {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedValue = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), getStashValue(stepDataMatcher.group(1)));
            stepDataMatcher = stepDataPattern.matcher(replacedValue);
        }
        return replacedValue.toString();
    }

    private String getStashValue(String stashKey){
        return Optional.of((String) Stash.getValue(stashKey))
                .orElseThrow(() -> new AutotestError("The value received by the key must be a string. Key: " + stashKey));
    }
}
