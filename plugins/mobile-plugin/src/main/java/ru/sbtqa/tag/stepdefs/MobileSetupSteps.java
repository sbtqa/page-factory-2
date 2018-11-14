package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.drivers.MobileDriverService;

public class MobileSetupSteps {

    public static void initMobile() {
//        PageManager.cachePages();
//        PageContext.resetContext();
        Environment.setDriverService(new MobileDriverService());
    }
}