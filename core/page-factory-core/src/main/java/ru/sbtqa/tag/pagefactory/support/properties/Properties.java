package ru.sbtqa.tag.pagefactory.support.properties;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.support.Environment;

public class Properties {

    private static Configuration config;

    Properties() {
        throw new IllegalAccessError("Utility class");
    }
    
    public static synchronized Configuration getProperties() {
        if (config == null) {
            config = ConfigFactory.create(Configuration.class);
            verificate();
        }
        return config;
    }

    private static void verificate() {
        checkOnEmpty(config.getEnvironment(), "driver.environment");
        checkOnEmpty(config.getPagesPackage(), "page.package");
        if (config.getEnvironment().equalsIgnoreCase(Environment.WEB.toString())) {
            checkOnEmpty(config.getBrowserName(), "webdriver.browser.name");
        }
    }

    private static <T extends Object> void checkOnEmpty(T property, String key) {
        if (null == property || property instanceof String && ((String) property).isEmpty()) {
            throw new PropertyMissingRuntimeException("Missing required parameter '" + key + "'");
        }
    }
}
