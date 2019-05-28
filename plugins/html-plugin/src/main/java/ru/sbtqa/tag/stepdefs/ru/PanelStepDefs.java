package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.PanelSteps;

public class PanelStepDefs {

    private final PanelSteps panelSteps = PanelSteps.getInstance();

    @И("^текст заголовка панели \"([^\"]*)\" соответствует$")
    @Когда("^текст заголовка панели \"([^\"]*)\" соответствует \"([^\"]*)\"$")
    public void hasHeader(String panelName, String headerText) {
        panelSteps.hasHeader(panelName, headerText);
    }

    @И("^текст панели \"([^\"]*)\" соответствует$")
    @Когда("^текст панели \"([^\"]*)\" соответствует \"([^\"]*)\"$")
    public void hasBody(String panelName, String bodyText) {
        panelSteps.hasBody(panelName, bodyText);
    }

    @И("^текст заголовка панели \"([^\"]*)\" содержит фрагмент$")
    @Когда("^текст заголовка панели \"([^\"]*)\" содержит фрагмент \"([^\"]*)\"$")
    public void containsHeaderText(String panelName, String textFragment) {
        panelSteps.containsBodyText(panelName, textFragment);
    }

    @И("^текст панели \"([^\"]*)\" содержит фрагмент$")
    @Когда("^текст панели \"([^\"]*)\" содержит фрагмент \"([^\"]*)\"$")
    public void containsBodyText(String panelName, String textFragment) {
        panelSteps.containsBodyText(panelName, textFragment);
    }
}
