package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.SelectSteps;

public class SelectStepDefs {

    private final SelectSteps selectSteps = SelectSteps.getInstance();

    @Когда("^user selects option \"([^\"]*)\" from \"([^\"]*)\"$")
    public void select(String selectValue, String selectName) {
        selectSteps.select(selectName, selectValue);
    }

    @Когда("^user selects option from \"([^\"]*)\" overlap with \"([^\"]*)\"$")
    public void selectByTextFragment(String selectName, String textFragment) {
        selectSteps.selectByTextFragment(selectName, textFragment);
    }

    @Когда("^user selects option from \"([^\"]*)\" by value \"([^\"]*)\" of the element \"([^\"]*)\"$")
    public void selectByTextOfElement(String selectName, String selectValue, String selectField) {
        selectSteps.selectByTextOfElement(selectName, selectValue, selectField);
    }

    @Когда("^in the dropdown \"([^\"]*)\" the option is selected with the value \"([^\"]*)\" of the element \"([^\"]*)\"$")
    public void checkSelectedOptionByElement(String selectName, String selectValue, String selectField) {
        selectSteps.checkSelectedOptionByElement(selectName, selectValue, selectField);
    }

    @Когда("^in the dropdown \"([^\"]*)\" option \"([^\"]*)\" selected$")
    public void checkSelectedOption(String selectName, String selectValue) {
        selectSteps.checkSelectedOption(selectName, selectValue);
    }

    @Когда("^user opens dropdown \"([^\"]*)\"$")
    public void open(String selectName) {
        selectSteps.open(selectName);
    }

    @Когда("^user closes dropdown \"([^\"]*)\"$")
    public void close(String selectName) {
        selectSteps.close(selectName);
    }
}
