package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class HtmlSetupSteps {

    private HtmlSetupSteps() {}

    public static void initHtml() {
        if (Environment.isDriverEmpty()) {
            Environment.setDriverService(new WebDriverService());
        }
        Environment.setReflection(new HtmlReflection());
    }
}
