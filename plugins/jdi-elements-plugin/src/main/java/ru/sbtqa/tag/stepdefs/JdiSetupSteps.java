package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.jdi.utils.JDIUtils;

public class JdiSetupSteps {

    private static final ThreadLocal<Boolean> isJdiInited = ThreadLocal.withInitial(() -> false);

    public synchronized void initJDI() {
        if (isJdiInited.get()) {
            return;
        } else {
            isJdiInited.set(true);
        }
        JDIUtils.setJDIConfig(() -> Environment.getDriverService().getDriver());
    }
}
