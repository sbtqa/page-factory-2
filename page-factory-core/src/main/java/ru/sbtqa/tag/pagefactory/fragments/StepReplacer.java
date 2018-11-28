package ru.sbtqa.tag.pagefactory.fragments;

import gherkin.ast.DataTable;
import gherkin.ast.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class StepReplacer {

    private Step step;

    StepReplacer(Step step) {
        this.step = step;
    }

    List<Step> replaceWith(List<Step> replacementSteps) {
        List<Step> replacedSteps = new ArrayList<>();

        List<Map<String, String>> dataTable = FragmentDataTableUtils.getDataTable(step);
        for (Map<String, String> dataTableRow : dataTable) {
            for (Step replacementStep : replacementSteps) {

                String text = FragmentDataTableUtils.applyToText(dataTableRow, replacementStep.getText());

                DataTable argument = null;
                if (replacementStep.getArgument() != null) {
                    argument = FragmentDataTableUtils.applyToArgument(dataTableRow, replacementStep);
                }

                Step replacedStep = new Step(step.getLocation(), replacementStep.getKeyword(), text, argument);

                replacedSteps.add(replacedStep);
            }
        }

        return replacedSteps;
    }
}
