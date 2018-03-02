package ru.sbtqa.tag.pagefactory.support.properties;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.support.Environment;

public enum Properties {

    INSTANCE;
    private final Configuration config;

    Properties() {
        config = ConfigFactory.create(Configuration.class);
        verificate();
    }

    public Configuration getProperties() {
        return config;
    }

    private void verificate() {
        checkOnEmpty(config.getEnvironment(), "driver.environment");
        checkOnEmpty(config.getPagesPackage(), "page.package");
        if (config.getEnvironment().equalsIgnoreCase(Environment.WEB.toString())) {
            checkOnEmpty(config.getBrowserName(), "webdriver.browser.name");
        }
    }

    private <T extends Object> void checkOnEmpty(T property, String key) {
        if (null == property || property instanceof String && ((String) property).isEmpty()) {
            throw new PropertyMissingRuntimeException("Missing required parameter '" + key + "'");
        }
    }
}
