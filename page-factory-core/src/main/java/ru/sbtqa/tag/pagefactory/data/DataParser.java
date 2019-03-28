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
import ru.sbtqa.tag.datajack.exceptions.DataException;
import ru.sbtqa.tag.datajack.providers.AbstractDataProvider;
import ru.sbtqa.tag.pagefactory.optional.PickleStepCustom;

public class DataParser {

    private static final String COLLECTION_SIGNATURE = "$";

    public static String replaceDataPlaceholders(PickleStepCustom currentStep, String raw) throws DataException {
        Pattern stepDataPattern = Pattern.compile(AbstractDataProvider.PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(raw);
        StringBuilder replacedStep = new StringBuilder(raw);

        while (stepDataMatcher.find()) {
            String collection = stepDataMatcher.group(1);
            String value = stepDataMatcher.group(2);

            if (collection == null) {
                DataUtils.parseDataTagValue(currentStep.getData());
            }

            String builtPath = COLLECTION_SIGNATURE + (collection == null ? "" : collection) + value;
            String parsedValue = DataFactory.getDataProvider().getByPath(builtPath).getValue();
            replacedStep = replacedStep.replace(stepDataMatcher.start(), stepDataMatcher.end(), parsedValue);
            stepDataMatcher = stepDataPattern.matcher(replacedStep);
        }
        return replacedStep.toString();
    }


    public static void replaceStep(PickleTestStep testStep) throws IllegalAccessException, DataException {
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
//        try {
            replaceStepArguments(testStep);
            replacePickleArguments(step);
            replaceStepText(step);
//        } catch (DataException ex) {
//            saveMessage(step, "Error data parsing");
//        }
    }

    private static void replaceStepArguments(PickleTestStep testStep) throws IllegalAccessException, DataException {
        PickleStepCustom step = (PickleStepCustom) testStep.getPickleStep();
        String stepText = step.getText();
        List<Argument> replacedArguments = new ArrayList<>();
        int offset = 0;

        Pattern stepDataPattern = Pattern.compile(AbstractDataProvider.PATH_PARSE_REGEX);
        Matcher stepDataMatcher = stepDataPattern.matcher(stepText);
        StringBuilder replacedValue = new StringBuilder(stepText);

        for (Argument argument : testStep.getDefinitionArgument()) {
            String argVal = argument.getVal();

            if (argVal != null) {
                int argOffset = argument.getOffset();
                if (stepDataPattern.matcher(argVal).find() && stepDataMatcher.find()) {
                    String data = replaceDataPlaceholders(step, argVal);


                    offset = argVal.length() - data.length();
                    argOffset = stepDataMatcher.start();
                    argVal = data;

                    replacedValue = replacedValue.replace(stepDataMatcher.start(), stepDataMatcher.end(), data);
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

    private static void replaceStepText(PickleStepCustom currentStep) throws DataException {
        currentStep.setText(replaceDataPlaceholders(currentStep, currentStep.getText()));
    }

    private static void replacePickleArguments(PickleStepCustom currentStep) throws IllegalAccessException, DataException {
        for (gherkin.pickles.Argument argument : currentStep.getArgument()) {
            if (argument.getClass().equals(PickleTable.class)) {
                replaceDataTable(currentStep, (PickleTable) argument);
            } else if (argument.getClass().equals(PickleString.class)) {
                String content = replaceDataPlaceholders(currentStep, ((PickleString) argument).getContent());
                FieldUtils.writeField(argument, "content", content, true);
            }
        }
    }

    private static void replaceDataTable(PickleStepCustom currentStep, PickleTable argument) throws IllegalAccessException, DataException {
        for (PickleRow pickleRow : argument.getRows()) {
            for (PickleCell pickleCell : pickleRow.getCells()) {
                FieldUtils.writeField(pickleCell, "value", replaceDataPlaceholders(currentStep, pickleCell.getValue()), true);
            }
        }
    }

//    private static void saveMessage(PickleStepCustom currentStep, String message) {
//        if (message != null) {
//            if (currentStep.isSkipped()) {
//                currentStep.setLog(message);
//            } else {
//                currentStep.setError(new AutotestError(message));
//            }
//        }
//    }

}