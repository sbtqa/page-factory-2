package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
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

        PageManager.cachePages();
        PageContext.resetContext();
        Environment.setDriverService(new MobileDriverService());
    }
}