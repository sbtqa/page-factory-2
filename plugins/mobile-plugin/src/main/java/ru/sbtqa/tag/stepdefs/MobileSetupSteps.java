package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;
import ru.sbtqa.tag.pagefactory.mobile.checks.MobilePageChecks;
import ru.sbtqa.tag.pagefactory.mobile.drivers.MobileDriverService;

import static java.lang.ThreadLocal.withInitial;

public class MobileSetupSteps {

    static final ThreadLocal<MobileDriverService> storage = withInitial(MobileDriverService::new);

    private MobileSetupSteps() {}

    public static synchronized void initMobile() {
        PageManager.cachePages();

        if (Environment.isDriverEmpty()) {
            Environment.setDriverService(storage.get());
        }
    }
}