package ru.sbtqa.tag.stepdefs;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.reflection.HtmlReflection;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;

public class HtmlSetupSteps {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    public static void initHtml() {
        if (isNewDriverNeeded()) {
            Environment.setDriverService(new WebDriverService());
        }
        Environment.setReflection(new HtmlReflection());
    }

    private static boolean isNewDriverNeeded() {
        return Environment.isDriverEmpty()
                || (!Environment.isDriverEmpty() && !PROPERTIES.getShared());
    }
}
