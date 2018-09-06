package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.jdi.utils.JDIUtils;

public class JdiSetupSteps {

    public synchronized void initJDI() {
        JDIUtils.setJDIConfig(() -> Environment.getDriverService().getDriver());
    }
}
