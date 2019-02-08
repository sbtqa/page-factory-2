package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.stepdefs.MobileGenericSteps;
import ru.sbtqa.tag.stepdefs.MobileSetupSteps;

public class MobileStepDefs extends MobileGenericSteps<MobileStepDefs> {

    @Before(order = 1)
    public void initMobile() {
        MobileSetupSteps.initMobile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user swipes in direction \"(.*?)\" to the text \"(.*?)\"$")
    public void swipeToTextByDirection(String direction, String text) throws SwipeException {
        super.swipeToTextByDirection(direction, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user swipes with match strategy \"(.*?)\" to the text \"(.*?)\"$")
    public void swipeToTextByMatch(String strategy, String text) throws SwipeException {
        super.swipeToTextByMatch(strategy, text);
    }
}
