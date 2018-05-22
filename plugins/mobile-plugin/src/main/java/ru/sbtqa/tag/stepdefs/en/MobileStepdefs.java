package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.Before;
import ru.sbtqa.tag.stepdefs.MobileSetupSteps;

public class MobileStepdefs extends MobileSetupSteps {

    @Before(order = 1)
    public void initMobile() {
        super.initMobile();
    }
}
