package ru.sbtqa.tag.pagefactory.mobile.junit;

import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.drivers.MobileDriverService;

import static java.lang.ThreadLocal.withInitial;

public class MobileSetupSteps {

    private static final ThreadLocal<MobileDriverService> storage = withInitial(MobileDriverService::new);

    private MobileSetupSteps() {}

    public static synchronized void initMobile() {
        PageManager.cachePages();

        if (Environment.isDriverEmpty() || !(Environment.getDriverService() instanceof MobileDriverService)) {
            Environment.setDriverService(storage.get());
        }
    }
}