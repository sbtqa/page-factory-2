package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.ControlSteps;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;

public class ControlStepDefs {

    private final ControlSteps controlSteps = ControlSteps.getInstance();

    @Когда("^(?:пользователь |он )?отмечает признак \"([^\"]*)\"$")
    public void select(String flagName) {
        controlSteps.select(flagName);
    }

    @Когда("^признак \"([^\"]*)\" (отмечен|не отмечен)$")
    public void isSelected(String flagName, ContainCondition condition) {
        controlSteps.isSelected(flagName, condition);
    }

    @Когда("^(?:пользователь |он )?отмечает признак \"([^\"]*)\" по значению \"([^\"]*)\"$")
    public void selectByValue(String flagName, String value) {
        controlSteps.selectByValue(flagName, value);
    }

    @И("^в радио группе \"([^\"]*)\" (отмечено|не отмечено) значение \"([^\"]*)\"$")
    public void isRadiobuttonSelected(String groupName, ContainCondition condition, String value) {
        controlSteps.isRadiobuttonSelected(groupName, condition, value);
    }

    @И("^в радио группе \"([^\"]*)\" не выбрано ни одно значение$")
    public void checkRadioGroupValueNotSelected(String radioGroupName) {
        controlSteps.checkRadioGroupValueNotSelected(radioGroupName);
    }
}
