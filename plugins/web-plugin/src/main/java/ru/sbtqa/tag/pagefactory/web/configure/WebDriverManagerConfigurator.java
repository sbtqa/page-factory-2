package ru.sbtqa.tag.pagefactory.web.configure;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverManagerConfigurator {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverManagerConfigurator.class);
    private static final WebConfiguration PROPERTIES = WebConfiguration.create();

    private WebDriverManagerConfigurator() {
    }

    public static void configureDriver(WebDriverManager webDriverManager, String browserType) {
        System.setProperty("wdm." + browserType.toLowerCase() + "Version", PROPERTIES.getBrowserVersion());

        if (!PROPERTIES.getDriversPath().isEmpty()) {
            System.setProperty("webdriver." + browserType.toLowerCase() + ".driver",
                    new File(PROPERTIES.getDriversPath()).getAbsolutePath());
        } else {
            LOG.warn("The value of property 'webdriver.drivers.path' is not specified."
                    + " Trying to automatically download and setup driver.");

            webDriverManager.setup();
        }
    }
}
