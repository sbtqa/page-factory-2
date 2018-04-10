package ru.sbtqa.tag.pagefactory;

import cucumber.api.java.Before;

public class SetupStepDefs {

    @Before
    public void setUp() {
        TestEnvironment.setDriverService(new WebDriverService());
    }
}
