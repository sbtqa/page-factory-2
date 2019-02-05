package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

import static java.lang.ThreadLocal.withInitial;

public class HtmlSetupSteps {

    private static final ThreadLocal<WebDriverService> storage = withInitial(WebDriverService::new);

    public static void initHtml() {
        if (Environment.isDriverEmpty()) {
            Environment.setDriverService(storage.get());
        }
    }
}
