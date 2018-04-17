package ru.sbtqa.tag.pagefactory;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.actions.WebPageActions;
import ru.sbtqa.tag.pagefactory.web.checks.WebPageChecks;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class SetupStepDefs {

    @Before
    public void setUp() {
        Environment.setDriverService(new WebDriverService());
        Environment.setPageActions(new WebPageActions());
        Environment.setPageChecks(new WebPageChecks());
    }
}
