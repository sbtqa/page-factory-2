package ru.sbtqa.tag.pagefactory.support;

import java.util.HashMap;
import static org.openqa.selenium.remote.BrowserType.OPERA;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.pagefactory.support.properties.Configuration;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;

public class SelenoidCapabilitiesProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SelenoidCapabilitiesProvider.class);

    private static final Configuration PROPERTIES = Properties.INSTANCE.getProperties();
    
    public static void apply(DesiredCapabilities capabilities) {

        setVersion(capabilities, PROPERTIES.getSelenoidBrowserVersion());

        setCapability(capabilities, "enableVNC", PROPERTIES.getSelenoidEnableVNC());
        setCapability(capabilities, "screenResolution", PROPERTIES.getSelenoidScreenResolution());
        setCapability(capabilities, "enableVideo", PROPERTIES.getSelenoidEnableVideo());
        setCapability(capabilities, "videoName", PROPERTIES.getSelenoidVideoName());
        setCapability(capabilities, "videoScreenSize", PROPERTIES.getSelenoidVideoScreenSize());
        setCapability(capabilities, "videoFrameRate", PROPERTIES.getSelenoidVideoFrameRate());
        setCapability(capabilities, "name", PROPERTIES.getSelenoidNameOfTests());
        setCapability(capabilities, "timeZone", PROPERTIES.getSelenoidTimeZone());
        setCapability(capabilities, "hostsEntries", PROPERTIES.getSelenoidHostEntries());
        setCapability(capabilities, "applicationContainers", PROPERTIES.getSelenoidApplicationContainers());
        setCapability(capabilities, "labels", PROPERTIES.getSelenoidContainerLables());
        
        if (TagWebDriver.getBrowserName().equalsIgnoreCase(OPERA)) {
            capabilities.setCapability("operaOptions", new HashMap<String, String>() {
                {
                    put("binary", "/usr/bin/opera");
                }
            });
        }
    }

    private static void setCapability(DesiredCapabilities capabilities, String capabilityName, String capabilityValue) {
        if (!capabilityValue.isEmpty()) {
            capabilities.setCapability(capabilityName, capabilityValue);
        } else {
            LOG.info("Capability \"" + capabilityName + "\" for Selenoid isn't set. Using default capability.");
        }
    }

    private static void setCapability(DesiredCapabilities capabilities, String capabilityName, boolean capabilityValue) {
        capabilities.setCapability(capabilityName, capabilityValue);
    }

    private static void setVersion(DesiredCapabilities capabilities, String capabilityValue) {
        if (!capabilityValue.isEmpty()) {
            capabilities.setVersion(capabilityValue);
        } else {
            LOG.info("Capability \"browserVersion\" for Selenoid isn't set. Using default capability.");
        }
    }

}
