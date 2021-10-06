package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.HintSteps;
import ru.sbtqa.tag.pagefactory.transformer.ContainCondition;

public class HintStepDefs {

    private final HintSteps hintSteps = HintSteps.getInstance();
    
    @Когда("^user opens a hint \"([^\"]*)\"$")
    public void open(String hintName) {
        hintSteps.open(hintName);
    }

    @Когда("^user closes a hint \"([^\"]*)\"$")
    public void close(String hintName) {
        hintSteps.close(hintName);
    }

    @И("^hint text \"([^\"]*)\" is strictly equal$")
    @Когда("^hint text \"([^\"]*)\" is strictly equal \"([^\"]*)\"$")
    public void hasText(String hintName, String text) {
        hintSteps.hasText(hintName, text);
    }

    @И("^hint text \"([^\"]*)\" contains a fragment$")
    @Когда("^hint text \"([^\"]*)\" contains a fragment \"([^\"]*)\"$")
    public void containsText(String hintName, String text) {
        hintSteps.containsText(hintName, text);
    }

    @Когда("^user checks that the hint \"([^\"]*)\" is (displayed|not displayed)$")
    public void isOpen(String hintName, ContainCondition condition) {
        hintSteps.isOpen(condition, hintName);
    }
}
