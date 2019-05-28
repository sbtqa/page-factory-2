package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.Transform;
import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.HintSteps;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;
import ru.sbtqa.tag.pagefactory.transformer.enums.Condition;

public class HintStepDefs {

    private final HintSteps hintSteps = HintSteps.getInstance();

    @Когда("^(?:пользователь |он )?открывает подсказку \"([^\"]*)\"$")
    public void open(String hintName) {
        hintSteps.open(hintName);
    }

    @Когда("^(?:пользователь |он )?закрывает подсказку \"([^\"]*)\"$")
    public void close(String hintName) {
        hintSteps.close(hintName);
    }

    @И("^текст подсказки \"([^\"]*)\" равен$")
    @Когда("^текст подсказки \"([^\"]*)\" равен \"([^\"]*)\"$")
    public void hasText(String hintName, String text) {
        hintSteps.hasText(hintName, text);
    }

    @И("^текст подсказки \"([^\"]*)\" содержит фрагмент$")
    @Когда("^текст подсказки \"([^\"]*)\" содержит фрагмент \"([^\"]*)\"$")
    public void containsText(String hintName, String text) {
        hintSteps.containsText(hintName, text);
    }

    @Когда("^(?:пользователь |он )?проверяет что подсказка \"([^\"]*)\" (не )?отображается$")
    public void isOpen(String hintName, @Transform(ConditionTransformer.class) Condition negation) {
        hintSteps.isOpen(negation, hintName);
    }
}
