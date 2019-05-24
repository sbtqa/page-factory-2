package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.Transform;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.ControlSteps;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;

public class ControlStepDefs {

    private final ControlSteps controlSteps = ControlSteps.getInstance();

    @Когда("^user marks a control \"([^\"]*)\"$")
    public void select(String flagName) {
        controlSteps.select(flagName);
    }

    @Когда("^control \"([^\"]*)\" is (not )?marked$")
    public void isSelected(String flagName, @Transform(ConditionTransformer.class) Condition negation) {
        controlSteps.isSelected(flagName, negation);
    }

    @Когда("^user marks a control \"([^\"]*)\" by value \"([^\"]*)\"$")
    public void selectByValue(String flagName, String value) {
        controlSteps.selectByValue(flagName, value);
    }

    @И("^in radio group \"([^\"]*)\" is (not )?marked value \"([^\"]*)\"$")
    public void isRadiobuttonSelected(String groupName, @Transform(ConditionTransformer.class) Condition negation, String value) {
        controlSteps.isRadiobuttonSelected(groupName, negation, value);
    }

    @И("^no value selected in radio group \"([^\"]*)\"$")
    public void checkRadioGroupValueNotSelected(String radioGroupName) {
        controlSteps.checkRadioGroupValueNotSelected(radioGroupName);
    }
}
