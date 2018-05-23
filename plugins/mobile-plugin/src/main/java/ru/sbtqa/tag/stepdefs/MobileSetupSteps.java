package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.drivers.MobileDriverService;

public class MobileSetupSteps {


    private static final ThreadLocal<Boolean> isMobileInited = ThreadLocal.withInitial(() -> false);

    public synchronized void initMobile() {
        if (isMobileInited.get()) {
            return;
        } else {
            isMobileInited.set(true);
        }
        Environment.setDriverService(new MobileDriverService());
    }
}