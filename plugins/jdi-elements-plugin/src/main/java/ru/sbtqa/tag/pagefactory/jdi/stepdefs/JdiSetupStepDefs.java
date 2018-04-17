package ru.sbtqa.tag.pagefactory.jdi.stepdefs;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.JDIUtils;
import ru.sbtqa.tag.pagefactory.jdi.actions.JdiPageActions;

public class JdiSetupStepDefs {
    
    @Before
    public void initJDI() {
        JDIUtils.setJDIConfig(() -> Environment.getDriverService().getDriver());
        Environment.setPageActions(new JdiPageActions());
    }
}
