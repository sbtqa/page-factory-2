package ru.sbtqa.tag.pagefactory.pages.jdielements;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.Environment;
import ru.sbtqa.tag.pagefactory.JDIUtils;

public class StepDefs {
    
    @Before
    public void initJDI() {
        JDIUtils.setJDIConfig(() -> Environment.getDriverService().getDriver());
    }
}
