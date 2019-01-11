package ru.sbtqa.tag.pagefactory.fragments;

import gherkin.ast.DataTable;
import gherkin.ast.Location;
import gherkin.ast.Node;
import gherkin.ast.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class StepReplacer {

    private Step step;
    private List<Step> replacedSteps = new ArrayList<>();

    StepReplacer(Step step) {
        this.step = step;
    }

    List<Step> replaceWith(List<Step> replacementSteps) {

        List<Map<String, String>> dataTable = FragmentDataTableUtils.getDataTable(step);

        if (dataTable.isEmpty()) {
            addSteps(replacementSteps);
            return replacedSteps;
        }

        for (Map<String, String> dataTableRow : dataTable) {
            for (Step replacementStep : replacementSteps) {

                String text = FragmentDataTableUtils.applyToText(dataTableRow, replacementStep.getText());

                DataTable argument = null;
                if (replacementStep.getArgument() != null) {
                    argument = FragmentDataTableUtils.applyToArgument(dataTableRow, replacementStep);
                }

                addStep(step.getLocation(), replacementStep.getKeyword(), text, argument);
            }
        }

        return replacedSteps;
    }

    private void addSteps(List<Step> replacementSteps) {
        for (Step replacementStep : replacementSteps) {
            addStep(step.getLocation(), replacementStep.getKeyword(), replacementStep.getText(), replacementStep.getArgument());
        }
    }

    private void addStep(Location location, String keyword, String text, Node argument) {
        Step replacedStep = new Step(location, keyword, text, argument);
        replacedSteps.add(replacedStep);
    }
}
