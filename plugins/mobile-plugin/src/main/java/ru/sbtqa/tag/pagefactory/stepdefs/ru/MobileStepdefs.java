package ru.sbtqa.tag.pagefactory.stepdefs.ru;

import cucumber.api.java.Before;
import ru.sbtqa.tag.pagefactory.stepdefs.MobileSetupSteps;

public class MobileStepdefs extends MobileSetupSteps{

    @Before(order = 1)
    public void initMobile() {
        super.initMobile();
    }
}
