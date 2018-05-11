package ru.sbtqa.tag.pagefactory.web.stepdefs;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class WebSetupStepDefs {

    @Before(order = 1)
    public void setUp() {
        Environment.setDriverService(new WebDriverService());
    }
}
