package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.SelectSteps;

public class SelectStepDefs {

    private final SelectSteps selectSteps = SelectSteps.getInstance();

    @Когда("^(?:пользователь |он )?выбирает в выпадающем списке \"([^\"]*)\" \"([^\"]*)\"$")
    public void select(String selectName, String selectValue) {
        selectSteps.select(selectName, selectValue);
    }

    @Когда("^(?:пользователь |он )?выбирает опцию в выпадающем списке \"([^\"]*)\" по частичному совпадению с \"([^\"]*)\"$")
    public void selectByTextFragment(String selectName, String textFragment) {
        selectSteps.selectByTextFragment(selectName, textFragment);
    }

    @Когда("^(?:пользователь |он )?выбирает в выпадающем списке \"([^\"]*)\" по значению \"([^\"]*)\" в элементе \"([^\"]*)\"$")
    public void selectByTextOfElement(String selectName, String selectValue, String selectField) {
        selectSteps.selectByTextOfElement(selectName, selectValue, selectField);
    }

    @Когда("^в выпадающем списке \"([^\"]*)\" выбрана опция со значением \"([^\"]*)\" в элементе \"([^\"]*)\"$")
    public void checkSelectedOptionByElement(String selectName, String selectValue, String selectField) {
        selectSteps.checkSelectedOptionByElement(selectName, selectValue, selectField);
    }

    @Когда("^в выпадающем списке \"([^\"]*)\" выбрана опция \"([^\"]*)\"$")
    public void checkSelectedOption(String selectName, String selectValue) {
        selectSteps.checkSelectedOption(selectName, selectValue);
    }

    @Когда("^(?:пользователь |он )?открывает список \"([^\"]*)\"$")
    public void open(String selectName) {
        selectSteps.open(selectName);
    }

    @Когда("^(?:пользователь |он )?закрывает список \"([^\"]*)\"$")
    public void close(String selectName) {
        selectSteps.close(selectName);
    }
}
