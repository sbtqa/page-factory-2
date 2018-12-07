package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class HtmlSetupSteps {

    static final ThreadLocal<WebDriverService> storage = new ThreadLocal<WebDriverService>() {
        @Override
        protected WebDriverService initialValue() {
            return new WebDriverService();
        }

    };

    private HtmlSetupSteps() {
    }

    public static void initHtml() {
        if (Environment.isDriverEmpty()) {
            Environment.setDriverService(storage.get());
        }
        Environment.setReflection(new HtmlReflection());
    }
}
