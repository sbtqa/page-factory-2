package ru.sbtqa.tag.pagefactory.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SelenoidCapabilitiesParser implements CapabilitiesParser {

    private static final Logger LOG = LoggerFactory.getLogger(SelenoidCapabilitiesParser.class);

    private static final Configuration PROPERTIES = Configuration.create();
    private static final String VIDEONAME_FORMAT = new SimpleDateFormat("dd.MM.yyyy_hh:mm:ss").format(new Date()) + "_" + UUID.randomUUID().toString() + "_%s";

    private DesiredCapabilities capabilities = new DesiredCapabilities();

    @Override
    public DesiredCapabilities parse() {
        setVersion(PROPERTIES.getSelenoidVersion());
        
        setCapability("enableVNC", PROPERTIES.getSelenoidEnableVNC());
        setCapability("enableLog", PROPERTIES.getSelenoidEnableLog());
        setCapability("screenResolution", PROPERTIES.getSelenoidScreenResolution());
        setCapability("enableVideo", PROPERTIES.getSelenoidEnableVideo());
        setCapability("videoScreenSize", PROPERTIES.getSelenoidVideoScreenSize());
        setCapability("videoFrameRate", PROPERTIES.getSelenoidVideoFrameRate());
        String name = getProperty(PROPERTIES.getSelenoidNameOfTests());
        setCapability("name", name);
        String logName = getProperty(PROPERTIES.getSelenoidLogName());
        setCapability("logName", logName + ".log", VIDEONAME_FORMAT);
        String videoName = getProperty(PROPERTIES.getSelenoidVideoName());
        setCapability("videoName", videoName + ".mp4", VIDEONAME_FORMAT);
        setCapability("timeZone", PROPERTIES.getSelenoidTimeZone());
        setCapability("hostsEntries", PROPERTIES.getSelenoidHostEntries());
        setCapability("applicationContainers", PROPERTIES.getSelenoidApplicationContainers());
        setCapability("labels", PROPERTIES.getSelenoidContainerLables());
        setCapability("sessionTimeout", PROPERTIES.getSelenoidSessionTimeout());

        return capabilities;
    }

    private String getProperty(String property) {
        if (property.trim().isEmpty() && Environment.getScenario() != null) {
            return Environment.getScenario().getName();
        } else {
            return property;
        }
    }

    private void setCapability(String capabilityName, String capabilityValue, String format) {
        if (!capabilityValue.isEmpty()) {
            capabilities.setCapability(capabilityName, String.format(format, capabilityValue));
        } else {
            LOG.info("Capability \"{}\" for Selenoid isn't set. Using default capability.", capabilityName);
        }
    }

    private void setCapability(String capabilityName, String capabilityValue) {
        setCapability(capabilityName, capabilityValue, "%s");
    }

    private void setCapability(String capabilityName, boolean capabilityValue) {
        capabilities.setCapability(capabilityName, capabilityValue);
    }

    private void setVersion(String capabilityValue) {
        if (!capabilityValue.isEmpty()) {
            capabilities.setVersion(capabilityValue);
        } else {
            LOG.info("Capability \"version\" for Selenoid isn't set. Using default capability.");
        }
    }
}
