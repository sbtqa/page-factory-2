package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;
import ru.sbtqa.tag.pagefactory.mobile.checks.MobilePageChecks;
import ru.sbtqa.tag.pagefactory.mobile.drivers.MobileDriverService;

public class MobileSetupSteps {

    private MobileSetupSteps() {}

    public static void initMobile() {
        PageManager.cachePages();
        Environment.setPageActions(new MobilePageActions());
        Environment.setPageChecks(new MobilePageChecks());
        Environment.setDriverService(new MobileDriverService());
    }
}