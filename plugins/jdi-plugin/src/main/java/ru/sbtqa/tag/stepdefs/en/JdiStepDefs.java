package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.Before;
import ru.sbtqa.tag.stepdefs.JdiSetupSteps;

public class JdiStepDefs extends JdiSetupSteps{

    @Before
    public void initJDI() {
        super.initJDI();
    }
}
