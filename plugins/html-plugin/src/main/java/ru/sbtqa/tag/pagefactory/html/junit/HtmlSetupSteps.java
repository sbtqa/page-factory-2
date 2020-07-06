package ru.sbtqa.tag.pagefactory.html.junit;

import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

import static java.lang.ThreadLocal.withInitial;

public class HtmlSetupSteps {

    private static final ThreadLocal<WebDriverService> storage = withInitial(WebDriverService::new);

    public static void initHtml() {
        if (Environment.isDriverEmpty() || !(Environment.getDriverService() instanceof WebDriverService)) {
            Environment.setDriverService(storage.get());
        }
    }

    public static void tearDown() {
        storage.remove();
    }
}
