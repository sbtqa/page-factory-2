package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

import static java.lang.ThreadLocal.*;

public class HtmlSetupSteps {

    static final ThreadLocal<WebDriverService> storage = withInitial(WebDriverService::new);

    private HtmlSetupSteps() {
    }

    public static void initHtml() {
        if (Environment.isDriverEmpty()) {
            Environment.setDriverService(storage.get());
        }
        Environment.setReflection(new HtmlReflection());
    }
}
