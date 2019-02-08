package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import cucumber.api.java.ru.И;
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
    @И("^(?:пользователь |он )?свайпает в направлении \"(.*?)\" до текста \"(.*?)\"$")
    public MobileStepDefs swipeToTextByDirection(String direction, String text) throws SwipeException {
        return super.swipeToTextByDirection(direction, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?свайпает по стратегии совпадения \"(.*?)\" до текста \"(.*?)\"$")
    public MobileStepDefs swipeToTextByMatch(String strategy, String text) throws SwipeException {
        return super.swipeToTextByMatch(strategy, text);
    }
}
