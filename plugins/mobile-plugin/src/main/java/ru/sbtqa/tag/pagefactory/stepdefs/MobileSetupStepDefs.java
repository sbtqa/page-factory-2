package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.mobile.drivers.MobileDriverService;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.actions.MobilePageActions;
import ru.sbtqa.tag.pagefactory.mobile.checks.MobilePageChecks;

public class MobileSetupStepDefs {

    @Before(order = 1)
    public void setUp() {
        Environment.setDriverService(new MobileDriverService());
        Environment.setPageActions(new MobilePageActions());
        Environment.setPageChecks(new MobilePageChecks());
    }
}
