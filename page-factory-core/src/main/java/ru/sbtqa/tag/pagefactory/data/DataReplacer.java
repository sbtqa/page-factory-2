package ru.sbtqa.tag.pagefactory.data;

import com.rits.cloning.Cloner;
import cucumber.api.Argument;
import cucumber.api.PickleStepTestStep;
import gherkin.pickles.*;
import io.cucumber.stepexpression.DataTableArgument;
import io.cucumber.stepexpression.DocStringArgument;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.datajack.exceptions.StashKeyNotFoundException;
import ru.sbtqa.tag.pagefactory.optional.PickleStepTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.sbtqa.tag.datajack.providers.AbstractDataProvider.PATH_PARSE_REGEX;

public class DataReplacer {

    private static final String COLLECTION_SIGNATURE = "$";
    private final static String STASH_PARSE_REGEX = "(?:#\\{([^}]+)})";

    private Map<String, String> replacesDone = new HashMap<>();

    HashMap<Integer, PickleStep> iPickleStep = new HashMap<>();
    HashMap<Integer, ArrayList> iPickleStepDef = new HashMap<>();

    /**
     * Replaces step data (from stash or data files)
     *
     * @param testStep step
     * @throws IllegalAccessException in case of a field write error
     * @throws DataException          in case of a data parse error
     */
    public boolean replace(PickleStepTestStep testStep) throws IllegalAccessException, DataException {
        if (DataFactory.getDataProvider() != null) {
            replace(testStep, false);
        }
        replace(testStep, true);

        return true;
    }

    /**
     * Replaces step data (from stash or data files)
     *
     * @param testStep step
     * @throws IllegalAccessException in case of a field write error
     * @throws DataException          in case of a data parse error
     */
    public void replaceRevert(PickleStepTestStep testStep) throws IllegalAccessException, DataException {
//        if (DataFactory.getDataProvider() != null) {
//            replace(testStep, false);
//        }
        replaceRevert(testStep, true);
    }

    private void replace(PickleStepTestStep testStep, boolean isStash) throws IllegalAccessException {
        PickleStepTag step = (PickleStepTag) (testStep.getPickleStep());
        replacePickleArguments(testStep, isStash);
        replaceStepArguments(testStep, isStash);
        replaceStepText(step, isStash);
    }

    private void replaceRevert(PickleStepTestStep testStep, boolean isStash) throws IllegalAccessException {
        PickleStepTag step = (PickleStepTag) (testStep.getPickleStep());
        replacePickleArgumentsRevert(testStep, isStash);
//        replaceStepArguments(testStep, isStash);
//        replaceStepText(step, isStash);
    }

    private void replaceStepText(PickleStepTag currentStep, boolean isStash) {
        currentStep.setText(replaceData(currentStep, currentStep.getText(), isStash));
    }

    private void replacePickleArguments(PickleStepTestStep currentStep, boolean isStash) throws IllegalAccessException {
        Cloner cloner = new Cloner();

        if (!iPickleStep.containsKey(currentStep.hashCode())) {
            PickleStep oldPickleStep = cloner.deepClone(currentStep.getPickleStep());
            iPickleStep.put(currentStep.hashCode(), oldPickleStep);
        }
        PickleStepTag pickleStepTag = (PickleStepTag) currentStep.getPickleStep();
        for (gherkin.pickles.Argument argument : currentStep.getPickleStep().getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                replacePickleTable(pickleStepTag, (PickleTable) argument, isStash);
            } else if (argument.getClass().equals(PickleString.class)) {
                String content = replaceData(pickleStepTag, ((PickleString) argument).getContent(), isStash);
                FieldUtils.writeField(argument, "content", content, true);
            }
        }

        Object definitionMatchArguments = FieldUtils.readField(FieldUtils.readField(currentStep, "definitionMatch", true), "arguments", true);
        if (definitionMatchArguments instanceof ArrayList) {

            ArrayList revertList = new ArrayList();
            for (Object definitionMatchArgument : (ArrayList) definitionMatchArguments) {
                if (definitionMatchArgument instanceof DataTableArgument) {
                    revertList.add(cloner.deepClone((List<List<String>>) FieldUtils.readField(definitionMatchArgument, "argument", true)));
                    List<List<String>> newDefinitionMatchArgument = replaceDataTable(definitionMatchArgument, currentStep, isStash);
                    FieldUtils.writeField(definitionMatchArgument, "argument", newDefinitionMatchArgument, true);
                } else if (definitionMatchArgument instanceof DocStringArgument) {
                    revertList.add(cloner.deepClone((List<List<String>>) FieldUtils.readField(definitionMatchArgument, "argument", true)));
                    String newDefinitionMatchArgument = replaceData((PickleStepTag) currentStep.getPickleStep(), ((DocStringArgument) definitionMatchArgument).getValue().toString(), isStash);
                    FieldUtils.writeField(definitionMatchArgument, "argument", newDefinitionMatchArgument, true);
                }
            }

            if (!iPickleStepDef.containsKey(currentStep.hashCode())) {
                iPickleStepDef.put(currentStep.hashCode(), revertList);
            }
        }
    }

    private void replacePickleArgumentsRevert(PickleStepTestStep currentStep, boolean isStash) throws IllegalAccessException {
        if (iPickleStep.containsKey(currentStep.hashCode())) {
            FieldUtils.writeField(currentStep, "step", iPickleStep.get(currentStep.hashCode()), true);
        }

        Object definitionMatchArguments = FieldUtils.readField(FieldUtils.readField(currentStep, "definitionMatch", true), "arguments", true);
        if (definitionMatchArguments instanceof ArrayList) {
            if (iPickleStepDef.containsKey(currentStep.hashCode())) {
                int count = 0;
                for (Object definitionMatchArgument : (ArrayList) definitionMatchArguments) {
                    if (definitionMatchArgument instanceof DataTableArgument) {
                        FieldUtils.writeField(definitionMatchArgument, "argument", iPickleStepDef.get(currentStep.hashCode()).get(count), true);
                        count++;
//                    } else if (definitionMatchArgument instanceof DocStringArgument) {
//                        FieldUtils.writeField(definitionMatchArgument, "argument", iPickleStepDef.get(currentStep.hashCode()).get(count), true);
//                        count++;
//                    }
                    }
                }
            }
        }
    }

    private void replacePickleTable(PickleStepTag currentStep, PickleTable argument, boolean isStash) throws IllegalAccessException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceData(currentStep, pickleCell.getValue(), isStash), true);
            }
        }
    }

    private List<List<String>> replaceDataTable(Object definitionMatchArgument, PickleStepTestStep currentStep,
                                                boolean isStash) throws IllegalAccessException {
        List<List<String>> arguments = (List<List<String>>) FieldUtils.readField(definitionMatchArgument, "argument", true);

        for (List<String> row : arguments) {
            for (String cell : row) {
                row.set(row.indexOf(cell), replaceData((PickleStepTag) currentStep.getPickleStep(), cell, isStash));
            }
            arguments.set(arguments.indexOf(row), row);
        }

        return arguments;
    }

    private void replaceStepArguments(PickleStepTestStep testStep, boolean isStash) throws IllegalAccessException {
        PickleStepTag step = (PickleStepTag) testStep.getPickleStep();
        String stepText = step.getText();
        String pattern = isStash ? STASH_PARSE_REGEX : PATH_PARSE_REGEX;
        Pattern stepDataPattern = Pattern.compile(pattern);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);

        int offset = 0;
        for (Argument argument : testStep.getDefinitionArgument()) {
            String argVal = argument.getValue();

            if (argVal != null) {
                String data = replaceData(step, argVal, isStash);
                boolean isReplaceNeededParameter = stepDataPattern.matcher(argVal).find() && stepDataMatcher.find();
                Object group = FieldUtils.readField(argument, "group", true);
                if (isReplaceNeededParameter && offset == 0) {
                    // this is first replace-needed parameter
                    offset = data.length() - argVal.length();
                    FieldUtils.writeField(group, "value", data, true);
                    FieldUtils.writeField(group, "end", argument.getEnd() + offset, true);
                } else if (isReplaceNeededParameter) {
                    // this is not first replace-needed parameter
                    FieldUtils.writeField(group, "value", data, true);
                    FieldUtils.writeField(group, "start", argument.getStart() + offset, true);
                    int thisOffset = data.length() - argVal.length();
                    FieldUtils.writeField(group, "end", argument.getEnd() + offset + thisOffset, true);
                    offset += thisOffset;
                } else if (offset != 0) {
                    // this is ordinary parameter
                    FieldUtils.writeField(group, "start", argument.getStart() + offset, true);
                    FieldUtils.writeField(group, "end", argument.getEnd() + offset, true);
                }
            }
        }
    }

    private String replaceData(PickleStepTag currentStep, String raw, boolean isStash) {
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
            String key = stepDataMatcher.group(1);
            if (key != null && Stash.getValue(key) instanceof String) {
                String stashValue = Stash.getValue(key);
                replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), stashValue);
                stepDataMatcher = stepDataPattern.matcher(replacedValue);
            }
        }
        return replacedValue.toString();
    }

    private void saveMessage(PickleStepTag currentStep, Throwable message) {
        if (currentStep.isSkipped()) {
            currentStep.setLog(message.getMessage());
        } else {
            currentStep.setError(message);
        }
    }
}
