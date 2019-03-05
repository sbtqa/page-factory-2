package ru.sbtqa.tag.pagefactory.data;

import cucumber.runner.PickleTestStep;
import cucumber.runtime.Argument;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleString;
import gherkin.pickles.PickleTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import static java.lang.String.format;

public class StashParser {

    private final static String PATH_PARSE_REGEX = "(?:\\#\\{([^\\}]+)\\})";
    private final static String STASH_KEY_FULL_PATH = "#{%s}";

    public static void replaceStep(PickleTestStep testStep) throws IllegalAccessException {
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
        StashParser.replaceStepArguments(testStep);
        StashParser.replacePickleArguments(step);
        StashParser.replaceStepText(step);
    }

    private static void replaceStepArguments(PickleTestStep testStep) throws IllegalAccessException {
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
        String stepText = step.getText();
        List<Argument> replacedArguments = new ArrayList<>();
        int offset = 0;

        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);
        StringBuilder replacedValue = new StringBuilder(stepText);

        for (Argument argument : testStep.getDefinitionArgument()) {
            String argVal = argument.getVal();
            int argOffset = argument.getOffset();

            if (argVal != null) {
                if (stepDataPattern.matcher(argVal).find() && stepDataMatcher.find()) {
                    String stashKey = stepDataMatcher.group(1);
                    String stashValue = StashParser.getStashValue(step, stashKey);
                    String stashKeyFullPath = format(STASH_KEY_FULL_PATH, stashKey);

                    if (stashValue == null) {
                        break;
                    }

                    if (stashKeyFullPath.equals(argument.getVal())) {
                        argVal = stashValue;
                        argOffset = stepDataMatcher.start();
                        offset = stashValue.length() + stashKeyFullPath.length();
                    }

                    replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
                    stepDataMatcher = stepDataPattern.matcher(replacedValue);
                } else {
                    argOffset = argument.getOffset() - offset;
                }
            }
            replacedArguments.add(new Argument(argOffset, argVal));
        }
        FieldUtils.writeField(FieldUtils.readField(testStep, "definitionMatch", true),
                "arguments", replacedArguments, true);
    }

    private static void replaceStepText(PickleStepCustom currentStep) throws IllegalAccessException {
        currentStep.setText(StashParser.replaceDataPlaceholders(currentStep, currentStep.getText()));
    }

    private static void replacePickleArguments(PickleStepCustom currentStep) throws IllegalAccessException {
        for (gherkin.pickles.Argument argument : currentStep.getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                replaceDataTable(currentStep, (PickleTable) argument);
            } else if (argument.getClass().equals(PickleString.class)) {
                String content = replaceDataPlaceholders(currentStep, ((PickleString) argument).getContent());
                FieldUtils.writeField(argument, "content", content, true);
            }
        }
    }

    private static void replaceDataTable(PickleStepCustom currentStep, PickleTable argument) throws IllegalAccessException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceDataPlaceholders(currentStep, pickleCell.getValue()), true);
            }
        }
    }

    private static String replaceDataPlaceholders(PickleStepCustom currentStep, String replaceableValue) {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(replaceableValue);
        StringBuilder replacedValue = new StringBuilder(replaceableValue);

        while (stepDataMatcher.find()) {
            String stashKey = stepDataMatcher.group(1);
            String stashValue = getStashValue(currentStep, stashKey);
            if (stashValue == null) {
                break;
            }
            replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
            stepDataMatcher = stepDataPattern.matcher(replacedValue);
        }
        return replacedValue.toString();
    }

    private static String getStashValue(PickleStepCustom currentStep, String stashKey) {
        Object stashValue = Stash.getValue(stashKey);
        String result = Optional.ofNullable((String) stashValue).orElse(null);
        String messageTemplate = result == null ? "Stash value not found by key: "
                : "The value received by the key must be a string. Key: ";
        if (stashValue == null) {
            saveMessage(currentStep, messageTemplate + stashKey);
        }
        return result;
    }

    private static void saveMessage(PickleStepCustom currentStep, String message) {
        if (message != null) {
            if (currentStep.isSkipped()) {
                currentStep.setLog(message);
            } else {
                currentStep.setError(new AutotestError(message));
            }
        }
    }
}
