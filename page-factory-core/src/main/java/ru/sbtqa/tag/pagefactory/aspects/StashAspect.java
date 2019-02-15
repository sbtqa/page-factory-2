package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.runtime.Argument;
import cucumber.runtime.formatter.Format;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleStep;
import gherkin.pickles.PickleTable;
import io.qameta.allure.Allure;
import io.qameta.allure.model.StepResult;
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

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.transformedArgs(..)) && args(step,xStream,..)")
    public void transformedArgs(PickleStep step, LocalizedXStreams.LocalizedXStream xStream) {
    }

    @Pointcut("execution(* cucumber.runtime.formatter.PrettyFormatter.formatStepText(..)) && args(keyword, stepText, textFormat, argFormat, arguments,..)")
    public void formatStepText(String keyword, String stepText, Format textFormat, Format argFormat, List<Argument> arguments) {
    }

    @Around("transformedArgs(step, xStream)")
    public Object transformedArgs(ProceedingJoinPoint joinPoint, PickleStep step, LocalizedXStreams.LocalizedXStream xStream) throws Throwable {
        List<gherkin.pickles.Argument> pickleArguments = new ArrayList<>();

        for (gherkin.pickles.Argument argument : step.getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                pickleArguments.add(replaceDataTable((PickleTable) argument));
            } else {
                pickleArguments.add(argument);
            }
        }
        FieldUtils.writeField(step, "arguments", pickleArguments, true);

        Object[] arguments = (Object[]) joinPoint.proceed(new Object[]{step, xStream});

        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            if (argument.getClass().equals(String.class)) {
                arguments[i] = replaceDataPlaceholders((String) argument);
            }
        }

        Allure.getLifecycle().updateStep((StepResult stepResult) ->
                stepResult.withName(replaceDataPlaceholders(step.getText())));

        return arguments;
    }

    @Around("formatStepText(keyword, stepText, textFormat, argFormat, arguments)")
    public String formatStepText(ProceedingJoinPoint joinPoint, String keyword, String stepText,
                                 Format textFormat, Format argFormat, List<Argument> arguments) throws Throwable {
        int offset = 0;
        List<Argument> replacedArguments = new ArrayList<>();

        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);
        StringBuilder replacedValue = new StringBuilder(stepText);

        for (Argument argument : arguments) {
            Argument arg = argument;
            if (stepDataPattern.matcher(argument.getVal()).find() && stepDataMatcher.find()) {

                String stashKey = stepDataMatcher.group(1);
                String stashValue = Optional.of((String) Stash.getValue(stashKey))
                        .orElseThrow(() -> new AutotestError("The value received by the key must be a string. Key: " + stashKey));

                if (format(STASH_KEY_FULL_PATH, stashKey).equals(argument.getVal())) {
                    arg = new Argument(stepDataMatcher.start(), stashValue);
                    offset = stashValue.length() - format(STASH_KEY_FULL_PATH, stashKey).length();
                }

                replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
                stepDataMatcher = stepDataPattern.matcher(replacedValue);
            } else {
                arg = new Argument(arg.getOffset() + offset, arg.getVal());
            }
            replacedArguments.add(arg);
        }
        return (String) joinPoint.proceed(new Object[]{keyword, replacedValue.toString(), textFormat, argFormat, replacedArguments});
    }

    private PickleTable replaceDataTable(PickleTable argument) throws IllegalAccessException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceDataPlaceholders(pickleCell.getValue()), true);
            }
        }
        return argument;
    }

    private String replaceDataPlaceholders(String raw) {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedValue = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String stashKey = stepDataMatcher.group(1);
            String stashValue = Optional.of((String) Stash.getValue(stashKey))
                    .orElseThrow(() -> new AutotestError("The value received by the key must be a string. Key: " + stashKey));

            replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), (String) stashValue);
            stepDataMatcher = stepDataPattern.matcher(replacedValue);
        }
        return replacedValue.toString();
    }
}
