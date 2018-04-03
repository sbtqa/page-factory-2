package ru.sbtqa.tag.pagefactory.pages.jdielements;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.JDIUtils;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;

public class StepDefs {
    
    @Before
    public void initJDI() {
        JDIUtils.setJDIConfig(() -> TagWebDriver.getDriver());
    }
}
