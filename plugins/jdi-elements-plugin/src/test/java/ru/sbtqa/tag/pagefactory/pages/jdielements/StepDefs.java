package ru.sbtqa.tag.pagefactory.pages.jdielements;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.JDIUtils;
import ru.sbtqa.tag.pagefactory.TestEnvironment;

public class StepDefs {
    
    @Before
    public void initJDI() {
        JDIUtils.setJDIConfig(() -> TestEnvironment.getDriverService().getDriver());
    }
}
