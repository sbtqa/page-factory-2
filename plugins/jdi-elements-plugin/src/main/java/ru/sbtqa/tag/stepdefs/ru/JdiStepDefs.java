package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import ru.sbtqa.tag.stepdefs.JdiSetupSteps;

public class JdiStepDefs extends JdiSetupSteps{

    @Before
    public void initJDI() {
        super.initJDI();
    }
}
