package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import cucumber.api.java.ru.Когда;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.exceptions.SwipeException;
import ru.sbtqa.tag.pagefactory.mobile.junit.MobileSetupSteps;
import ru.sbtqa.tag.pagefactory.mobile.junit.MobileSteps;

public class MobileStepDefs {

    private MobileSteps mobileSteps = MobileSteps.getInstance();

    @Before(order = 1)
    public void initMobile() {
        MobileSetupSteps.initMobile();
    }

    @Когда("^(?:пользователь |он )?свайпает в направлении \"(.*?)\" до текста \"(.*?)\"$")
    public void swipeToTextByDirection(String direction, String text) throws SwipeException {
        mobileSteps.swipeToTextByDirection(direction, text);
    }

    @Когда("^(?:пользователь |он )?свайпает по стратегии совпадения \"(.*?)\" до текста \"(.*?)\"$")
    public void swipeToTextByMatch(String strategy, String text) throws SwipeException {
        mobileSteps.swipeToTextByMatch(strategy, text);
    }

    @Когда("^(?:пользователь |он )?нажимает на \"([^\"]*)\"$")
    public void pressOn(String elementTitle) throws PageException {
        mobileSteps.press(elementTitle);
    }

    @Когда("^(?:пользователь |он )?тапает по \"(.*?)\"$")
    public void tapOn(String elementTitle) throws PageException {
        mobileSteps.tap(elementTitle);
    }
}
