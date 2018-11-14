package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.stepdefs.MobileSetupSteps;
import ru.sbtqa.tag.stepdefs.MobileSteps;

public class MobileStepDefs extends MobileSteps {

    @Before(order = 1)
    public void initMobile() {
        MobileSetupSteps.initMobile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?свайпает в направлении \"(.*?)\" до текста \"(.*?)\"$")
    public void swipeToTextByDirection(String direction, String text) throws SwipeException {
        super.swipeToTextByDirection(direction, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?свайпает по стратегии совпадения \"(.*?)\" до текста \"(.*?)\"$")
    public void swipeToTextByMatch(String strategy, String text) throws SwipeException {
        super.swipeToTextByMatch(strategy, text);
    }
}
