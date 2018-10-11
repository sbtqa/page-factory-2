package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.stepdefs.MobileGenericSteps;

public class MobileSteps extends MobileGenericSteps {

    @Before(order = 1)
    @Override
    public void initMobile() {
        super.initMobile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь свайпает в направлении \"(.*?)\" до текста \"(.*?)\"$")
    public void swipeToTextByDirection(String direction, String text) throws SwipeException {
        super.swipeToTextByDirection(direction, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^пользователь свайпает по стратегии совпадения \"(.*?)\" до текста \"(.*?)\"$")
    public void swipeToTextByMatch(String strategy, String text) throws SwipeException {
        super.swipeToTextByMatch(strategy, text);
    }
}
