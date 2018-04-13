package ru.sbtqa.tag.pagefactory;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class SetupStepDefs {

    @Before
    public void setUp() {
        Environment.setDriverService(new WebDriverService());
    }
}
