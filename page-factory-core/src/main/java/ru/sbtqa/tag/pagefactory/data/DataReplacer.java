package ru.sbtqa.tag.pagefactory.data;

import cucumber.api.Argument;
import cucumber.api.PickleStepTestStep;
import gherkin.pickles.PickleCell;
import gherkin.pickles.PickleRow;
import gherkin.pickles.PickleString;
import gherkin.pickles.PickleTable;
import io.cucumber.stepexpression.DataTableArgument;
import io.cucumber.stepexpression.DocStringArgument;
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

    /**
     * Replaces step data (from stash or data files)
     *
     * @param testStep step
     * @throws IllegalAccessException in case of a field write error
     * @throws DataException          in case of a data parse error
     */
    public void replace(PickleStepTestStep testStep) throws IllegalAccessException, DataException {
        if (DataFactory.getDataProvider() != null) {
            replace(testStep, false);
        }
        replace(testStep, true);
    }

    private void replace(PickleStepTestStep testStep, boolean isStash) throws IllegalAccessException {
        PickleStepCustom step = (PickleStepCustom) (testStep.getPickleStep());
        replacePickleArguments(testStep, isStash);
        replaceStepArguments(testStep, isStash);
        replaceStepText(step, isStash);
    }

    private void replaceStepText(PickleStepCustom currentStep, boolean isStash) {
        currentStep.setText(replaceData(currentStep, currentStep.getText(), isStash));
    }

    private void replacePickleArguments(PickleStepTestStep currentStep, boolean isStash) throws IllegalAccessException {
        PickleStepCustom pickleStepCustom = (PickleStepCustom) currentStep.getPickleStep();
        for (gherkin.pickles.Argument argument : currentStep.getPickleStep().getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                replacePickleTable(pickleStepCustom, (PickleTable) argument, isStash);
            } else if (argument.getClass().equals(PickleString.class)) {
                String content = replaceData(pickleStepCustom, ((PickleString) argument).getContent(), isStash);
                FieldUtils.writeField(argument, "content", content, true);
            }
        }

        Object definitionMatchArguments = FieldUtils.readField(FieldUtils.readField(currentStep, "definitionMatch", true), "arguments", true);
        if (definitionMatchArguments instanceof ArrayList) {
            for (Object definitionMatchArgument : (ArrayList) definitionMatchArguments) {
                if (definitionMatchArgument instanceof DataTableArgument) {
                    List<List<String>> newDefinitionMatchArgument = replaceDataTable(definitionMatchArgument, currentStep, isStash);
                    FieldUtils.writeField(definitionMatchArgument, "argument", newDefinitionMatchArgument, true);
                } else if (definitionMatchArgument instanceof DocStringArgument) {
                    String newDefinitionMatchArgument = replaceData((PickleStepCustom) currentStep.getPickleStep(), ((DocStringArgument) definitionMatchArgument).getValue().toString(), isStash);
                    FieldUtils.writeField(definitionMatchArgument, "argument", newDefinitionMatchArgument, true);
                }
            }
        }
    }

    private void replacePickleTable(PickleStepCustom currentStep, PickleTable argument, boolean isStash) throws IllegalAccessException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceData(currentStep, pickleCell.getValue(), isStash), true);
            }
        }
    }

    private List<List<String>> replaceDataTable(Object definitionMatchArgument, PickleStepTestStep currentStep, boolean isStash) throws IllegalAccessException {
        List<List<String>> arguments = (List<List<String>>) FieldUtils.readField(definitionMatchArgument, "argument", true);

        for (List<String> row : arguments) {
            for (String cell : row) {
                row.set(row.indexOf(cell), replaceData((PickleStepCustom) currentStep.getPickleStep(), cell, isStash));
            }
            arguments.set(arguments.indexOf(row), row);
        }

        return arguments;
    }

    private void replaceStepArguments(PickleStepTestStep testStep, boolean isStash) throws IllegalAccessException {
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
        String stepText = step.getText();

        String pattern = isStash ? STASH_PARSE_REGEX : PATH_PARSE_REGEX;
        Pattern stepDataPattern = Pattern.compile(pattern);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);

        for (Argument argument : testStep.getDefinitionArgument()) {
            String argVal = argument.getValue();

            if (argVal != null
                    && stepDataPattern.matcher(argVal).find()
                    && stepDataMatcher.find()) {
                String data = replaceData(step, argVal, isStash);
                FieldUtils.writeField(FieldUtils.readField(argument, "group", true),
                        "end", argument.getStart() + data.length(), true);
                FieldUtils.writeField(FieldUtils.readField(argument, "group", true),
                        "value", data, true);
            }
        }
    }

    private String replaceData(PickleStepCustom currentStep, String raw, boolean isStash) {
        String replacedText = raw;
        try {
            replacedText = isStash ? replaceStashPlaceholders(raw)
                    : replaceDataPlaceholders(raw, currentStep.getDataTag());
        } catch (StashKeyNotFoundException | DataException ex) {
            saveMessage(currentStep, ex);
        }
        return replacedText;
    }

    /**
     * Substitutes data from files into a string
     *
     * @param raw                 replaceable string
     * @param currentScenarioData scenario data path
     * @return replaced string
     * @throws DataException in case of a field write error
     */
    public String replaceDataPlaceholders(String raw, String currentScenarioData) throws DataException {
        Pattern stepDataPattern = Pattern.compile(PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedStep = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String collection = stepDataMatcher.group(1);
            String value = stepDataMatcher.group(2);

            if (collection == null && currentScenarioData != null) {
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

    private void saveMessage(PickleStepCustom currentStep, Throwable message) {
        if (currentStep.isSkipped()) {
            currentStep.setLog(message.getMessage());
        } else {
            currentStep.setError(message);
        }
    }
}
