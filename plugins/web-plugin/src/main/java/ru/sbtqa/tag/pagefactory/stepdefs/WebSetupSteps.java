package ru.sbtqa.tag.pagefactory.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.sbtqa.tag.pagefactory.web.checks.WebPageChecks;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class WebSetupSteps {

    private static final ThreadLocal<Boolean> isWebInited = ThreadLocal.withInitial(() -> false);

    public synchronized void initWeb() {
        if (isWebInited.get()) {
            return;
        } else {
            isWebInited.set(true);
        }
        Environment.setDriverService(new WebDriverService());
        Environment.setPageActions(new WebPageActions());
        Environment.setPageChecks(new WebPageChecks());
    }
}
