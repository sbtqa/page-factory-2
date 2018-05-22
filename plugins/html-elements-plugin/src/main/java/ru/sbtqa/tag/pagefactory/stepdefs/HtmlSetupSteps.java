package ru.sbtqa.tag.pagefactory.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.html.actions.HtmlPageActions;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class HtmlSetupSteps {

    private static final ThreadLocal<Boolean> isHTMLInited = ThreadLocal.withInitial(() -> false);

    public synchronized void initHTML() {
        if (isHTMLInited.get()) {
            return;
        } else {
            isHTMLInited.set(true);
        }
        Environment.setDriverService(new WebDriverService());
        Environment.setPageActions(new HtmlPageActions());
    }
}
