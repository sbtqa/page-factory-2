package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.jdi.actions.JdiPageActions;
import ru.sbtqa.tag.pagefactory.jdi.utils.JDIUtils;

public class JdiSetupSteps {

    private static final ThreadLocal<Boolean> isJdiInited = ThreadLocal.withInitial(() -> false);

    public synchronized void initJDI() {
        if (isJdiInited.get()) {
            return;
        } else {
            isJdiInited.set(true);
        }
        Environment.setPageActions(new JdiPageActions());
        JDIUtils.setJDIConfig(() -> Environment.getDriverService().getDriver());
    }
}
