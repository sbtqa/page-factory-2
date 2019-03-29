package ru.sbtqa.tag.pagefactory.data;

import cucumber.runner.PickleTestStep;
import cucumber.runtime.Argument;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleString;
import gherkin.pickles.PickleTable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.datajack.exceptions.StashKeyNotFoundException;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;
import static ru.sbtqa.tag.datajack.providers.AbstractDataProvider.PATH_PARSE_REGEX;

public class DataReplacer {

    private static final String COLLECTION_SIGNATURE = "$";
    private final static String STASH_PARSE_REGEX = "(?:\\#\\{([^\\}]+)\\})";

    public String replaceDataPlaceholders(String raw, String currentScenarioData) throws DataException {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedStep = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String collection = stepDataMatcher.group(1);
            String value = stepDataMatcher.group(2);

            if (collection == null) {
                DataUtils.parseDataTagValue(currentScenarioData);
            }

            String builtPath = COLLECTION_SIGNATURE + (collection == null ? "" : collection) + value;
            String parsedValue = DataFactory.getDataProvider().getByPath(builtPath).getValue();
            replacedStep.replace(stepDataMatcher.start(), stepDataMatcher.end(), parsedValue);
            stepDataMatcher = stepDataPattern.matcher(replacedStep);
        }
        return replacedStep.toString();
    }

    private String replaceStashPlaceholders(String replaceableValue) {
        Pattern stepDataPattern = Pattern.compile(STASH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(replaceableValue);
        StringBuilder replacedValue = new StringBuilder(replaceableValue);

        while (stepDataMatcher.find()) {
            String stashValue = Stash.getValue(stepDataMatcher.group(1));
            replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
            stepDataMatcher = stepDataPattern.matcher(replacedValue);
        }
        return replacedValue.toString();
    }

    private void replaceStepText(PickleStepCustom currentStep, boolean isStash) {
        currentStep.setText(replaceData(currentStep, currentStep.getText(), isStash));
    }

    private String replaceData(PickleStepCustom currentStep, String row, boolean isStash) {
        String replacedText = row;
        if (isStash) {
            try {
                replacedText = replaceStashPlaceholders(row);
            } catch (StashKeyNotFoundException ex) {
                saveMessage(currentStep, ex);
            }
        } else {
            try {
                replacedText = replaceDataPlaceholders(row, currentStep.getData());
            } catch (DataException ex) {
                saveMessage(currentStep, ex);
            }
        }
        return replacedText;
    }

    private void replacePickleArguments(PickleStepCustom currentStep, boolean isStash) throws IllegalAccessException {
        for (gherkin.pickles.Argument argument : currentStep.getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                replaceDataTable(currentStep, (PickleTable) argument, isStash);
            } else if (argument.getClass().equals(PickleString.class)) {
                String content = replaceData(currentStep, ((PickleString) argument).getContent(), isStash);
                FieldUtils.writeField(argument, "content", content, true);
            }
        }
    }

    private void replaceDataTable(PickleStepCustom currentStep, PickleTable argument, boolean isStash) throws IllegalAccessException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceData(currentStep, pickleCell.getValue(), isStash), true);
            }
        }
    }

    private void replace(PickleTestStep testStep, boolean isStash) throws IllegalAccessException {
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
        replaceStepArguments(testStep, isStash);
        replacePickleArguments(step, isStash);
        replaceStepText(step, isStash);
    }


    public void replace(PickleTestStep testStep) throws IllegalAccessException, DataException {
        if (DataFactory.getDataProvider() != null) {
            replace(testStep, false);
        }
        replace(testStep, true);
    }

    private void replaceStepArguments(PickleTestStep testStep, boolean isStash) throws IllegalAccessException {
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
        String stepText = step.getText();
        List<Argument> replacedArguments = new ArrayList<>();
        int offset = 0;

        String pattern = isStash ? STASH_PARSE_REGEX : PATH_PARSE_REGEX;

        Pattern stepDataPattern = Pattern.compile(pattern);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);
        StringBuilder replacedValue = new StringBuilder(stepText);

        for (Argument argument : testStep.getDefinitionArgument()) {
            String argVal = argument.getVal();

            if (argVal != null) {
                int argOffset;
                if (stepDataPattern.matcher(argVal).find() && stepDataMatcher.find()) {
                    String data = replaceData(step, argVal, isStash);

                    offset = argVal.length() - data.length();
                    argOffset = stepDataMatcher.start();
                    argVal = data;

                    replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), data);
                    stepDataMatcher = stepDataPattern.matcher(replacedValue);
                } else {
                    argOffset = argument.getOffset() - offset;
                }
                replacedArguments.add(new Argument(argOffset, argVal));
            } else {
                replacedArguments.add(argument);
            }
        }
        FieldUtils.writeField(FieldUtils.readField(testStep, "definitionMatch", true),
                "arguments", replacedArguments, true);
    }

    private void saveMessage(PickleStepCustom currentStep, Throwable message) {
        if (message != null) {
            if (currentStep.isSkipped()) {
                currentStep.setLog(message.getMessage());
            } else {
                currentStep.setError(message);
            }
        }
    }

}