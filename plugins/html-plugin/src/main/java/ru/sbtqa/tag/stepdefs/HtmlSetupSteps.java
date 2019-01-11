package ru.sbtqa.tag.stepdefs;

import static java.lang.ThreadLocal.withInitial;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

import ru.sbtqa.tag.pagefactory.find.HtmlFindUtils;

public class HtmlSetupSteps {

    static final ThreadLocal<WebDriverService> storage = withInitial(WebDriverService::new);

    private HtmlSetupSteps() {
    }

    public static void initHtml() {
        if (Environment.isDriverEmpty()) {
            Environment.setDriverService(storage.get());
        }
        Environment.setReflection(new HtmlReflection());
        Environment.setFindUtils(new HtmlFindUtils());
    }
}
