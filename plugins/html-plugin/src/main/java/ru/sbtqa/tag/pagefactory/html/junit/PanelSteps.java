package ru.sbtqa.tag.pagefactory.html.junit;

import ru.sbtqa.tag.pagefactory.elements.panel.Panel;
import ru.sbtqa.tag.pagefactory.transformer.SearchStrategy;
import ru.sbtqa.tag.pagefactory.web.utils.ElementUtils;
import static java.lang.ThreadLocal.withInitial;

public class PanelSteps implements Steps {

    static final ThreadLocal<PanelSteps> storage = withInitial(PanelSteps::new);

    public static PanelSteps getInstance() {
        return storage.get();
    }

    public PanelSteps hasHeader(String panelName, String headerText) {
        Panel panel = getPanel(panelName);
        ElementUtils.checkText(panel.getHeaderText(), headerText, SearchStrategy.EQUALS);
        return this;
    }

    public PanelSteps hasBody(String panelName, String bodyText) {
        Panel panel = getPanel(panelName);
        ElementUtils.checkText(panel.getBodyText(), bodyText, SearchStrategy.EQUALS);
        return this;
    }

    public PanelSteps containsHeaderText(String panelName, String textFragment) {
        Panel panel = getPanel(panelName);
        ElementUtils.checkText(panel.getHeaderText(), textFragment, SearchStrategy.CONTAINS);
        return this;
    }

    public PanelSteps containsBodyText(String panelName, String textFragment) {
        Panel panel = getPanel(panelName);
        ElementUtils.checkText(panel.getBodyText(), textFragment, SearchStrategy.CONTAINS);
        return this;
    }

    private Panel getPanel(String panelName) {
        return getFindUtils().find(panelName, Panel.class);
    }
}
