package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.ru.И;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.html.junit.PanelSteps;

public class PanelStepDefs {

    private final PanelSteps panelSteps = PanelSteps.getInstance();

    @И("^header text of the panel \"([^\"]*)\" is strictly equal$")
    @Когда("^header text of the panel \"([^\"]*)\" is strictly equal \"([^\"]*)\"$")
    public void hasHeader(String panelName, String headerText) {
        panelSteps.hasHeader(panelName, headerText);
    }

    @И("^body text of the panel \"([^\"]*)\" is strictly equal$")
    @Когда("^body text of the panel \"([^\"]*)\" is strictly equal \"([^\"]*)\"$")
    public void hasBody(String panelName, String bodyText) {
        panelSteps.hasBody(panelName, bodyText);
    }

    @И("^header text of the panel \"([^\"]*)\" contains a fragment$")
    @Когда("^header text of the panel \"([^\"]*)\" contains a fragment \"([^\"]*)\"$")
    public void containsHeaderText(String panelName, String textFragment) {
        panelSteps.containsHeaderText(panelName, textFragment);
    }

    @И("^body text of the panel \"([^\"]*)\" contains a fragment$")
    @Когда("^body text of the panel \"([^\"]*)\" contains a fragment \"([^\"]*)\"$")
    public void containsBodyText(String panelName, String textFragment) {
        panelSteps.containsBodyText(panelName, textFragment);
    }
}
