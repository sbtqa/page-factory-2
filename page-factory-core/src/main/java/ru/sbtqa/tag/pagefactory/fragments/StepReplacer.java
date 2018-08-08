package ru.sbtqa.tag.pagefactory.fragments;

import gherkin.ast.DataTable;
import gherkin.ast.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StepReplacer {

    private Step step;

    public StepReplacer(Step step) {
        this.step = step;
    }

    public List<Step> replaceWith(List<Step> replacementSteps) {
        List<Step> replacedSteps = new ArrayList<>();

        for (Step replacementStep : replacementSteps) {
            Map<String, String> dataTableAsMap = FragmentDataTableUtils.getDataTableAsMap(step);

            String text = FragmentDataTableUtils.applyToText(dataTableAsMap, replacementStep.getText());

            DataTable argument = null;
            if (replacementStep.getArgument() != null) {
                argument = FragmentDataTableUtils.applyToArgument(dataTableAsMap, replacementStep);
            }

            Step replacedStep = new Step(step.getLocation(), replacementStep.getKeyword(), text, argument);

            replacedSteps.add(replacedStep);
        }

        return replacedSteps;
    }
}
