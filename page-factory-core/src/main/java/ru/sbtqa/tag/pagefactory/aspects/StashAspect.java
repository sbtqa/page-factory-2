package ru.sbtqa.tag.pagefactory.aspects;

import cucumber.api.Result;
import cucumber.api.TestStep;
import cucumber.runtime.Argument;
import cucumber.runtime.formatter.Format;
import cucumber.runtime.xstream.LocalizedXStreams;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleStep;
import gherkin.pickles.PickleTable;
import java.util.ArrayList;
import java.util.List;
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
import ru.sbtqa.tag.qautils.errors.AutotestError;

@Aspect
public class StashAspect {

    private final static String PATH_PARSE_REGEX = "(?:\\#\\{([^\\}]+)\\})";
    private static final Logger LOG = LoggerFactory.getLogger(StashAspect.class);

    @Pointcut("execution(* cucumber.runtime.StepDefinitionMatch.transformedArgs(..)) && args(step,xStream,..)")
    public void transformedArgs(PickleStep step, LocalizedXStreams.LocalizedXStream xStream) {
    }

    @Around("transformedArgs(step, xStream)")
    public Object transformedArgs(ProceedingJoinPoint joinPoint, PickleStep step, LocalizedXStreams.LocalizedXStream xStream) throws Throwable {
        List<gherkin.pickles.Argument> arguments = new ArrayList<>();

        for (gherkin.pickles.Argument argument : step.getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                arguments.add(replaceDataTable((PickleTable) argument));
            } else {
                arguments.add(argument);
            }
        }
        FieldUtils.writeField(step, "arguments", arguments, true);

        Object[] result = (Object[]) joinPoint.proceed(new Object[]{step, xStream});
        for (int i = 0; i < result.length; i++) {
            Object argument = result[i];

            if (argument.getClass().equals(String.class) && notReplace((String) argument)) {
                result[i] = replaceDataPlaceholders((String) argument);
            }
        }
        return result;
    }

    @Pointcut("call(cucumber.api.event.TestStepFinished.new(..)) && args(timeStamp, testStep, result,..)")
    public void testFinished(Long timeStamp, TestStep testStep, Result result) {
    }

    @Pointcut("execution(* cucumber.runtime.formatter.PrettyFormatter.formatStepText(..)) && args(keyword, stepText, textFormat, argFormat, arguments,..)")
    public void formatStepText(String keyword, String stepText, Format textFormat, Format argFormat, List<Argument> arguments) {
    }


    @Around("formatStepText(keyword, stepText, textFormat, argFormat, arguments)")
    public String formatStepText(ProceedingJoinPoint joinPoint, String keyword, String stepText,
                                 Format textFormat, Format argFormat, List<Argument> arguments) throws Throwable {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);
        StringBuilder replacedValue = new StringBuilder(stepText);
        int offset = 0;
        List<Argument> replacedArguments = new ArrayList<>();

        for (int i = 0; i < arguments.size(); i++) {
            Argument argument = arguments.get(i);
            
            Argument arg = argument;
            if (stepDataPattern.matcher(argument.getVal()).find() && stepDataMatcher.find()) {
                String stashKey = stepDataMatcher.group(1);
                Object stashValue = Stash.getValue(stashKey);

                if (!stashValue.getClass().equals(String.class)) {
                    throw new AutotestError("The value received by the key must be a string. Key: " + stashKey);
                }

                if (("#{" + stashKey + "}").equals(argument.getVal())) {
                    arg = new Argument(stepDataMatcher.start(), (String) stashValue);
                }

                offset = ((String) stashValue).length() - ("#{" + stashKey + "}").length();
                replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), (String) stashValue);
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
            Object stashValue = Stash.getValue(stashKey);
            if (!stashValue.getClass().equals(String.class)) {
                throw new AutotestError("The value received by the key must be a string. Key: " + stashKey);
            }

            replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), (String) stashValue);
            stepDataMatcher = stepDataPattern.matcher(replacedValue);
        }
        return replacedValue.toString();
    }

    private boolean notReplace(String raw) {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        return stepDataMatcher.find();
    }
}
