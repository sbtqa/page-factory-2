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
    private static final String UNIQUE_NAME_FORMAT = new SimpleDateFormat("dd.MM.yyyy_hh:mm:ss").format(new Date()) + "_%s_" + UUID.randomUUID();

    private final DesiredCapabilities capabilities = new DesiredCapabilities();

    @Override
    public DesiredCapabilities parse() {
        String version = PROPERTIES.getSelenoidVersion();
        if (version.isEmpty()) {
            return capabilities;
        }

        setVersion(version);

        String name = getProperty(PROPERTIES.getSelenoidNameOfTests());
        String logName = getProperty(PROPERTIES.getSelenoidLogName());
        String videoName = getProperty(PROPERTIES.getSelenoidVideoName());


        setCapability("enableVNC", PROPERTIES.getSelenoidEnableVNC());
        setCapability("enableLog", PROPERTIES.getSelenoidEnableLog());
        setCapability("screenResolution", PROPERTIES.getSelenoidScreenResolution());
        setCapability("enableVideo", PROPERTIES.getSelenoidEnableVideo());
        setCapability("videoScreenSize", PROPERTIES.getSelenoidVideoScreenSize());
        setCapability("videoFrameRate", PROPERTIES.getSelenoidVideoFrameRate());
        setCapability("name", name);
        setCapability("logName", logName, UNIQUE_NAME_FORMAT + ".log");
        setCapability("videoName", videoName, UNIQUE_NAME_FORMAT + ".mp4");
        setCapability("timeZone", PROPERTIES.getSelenoidTimeZone());
        setCapability("hostsEntries", PROPERTIES.getSelenoidHostEntries());
        setCapability("applicationContainers", PROPERTIES.getSelenoidApplicationContainers());
        setCapability("labels", PROPERTIES.getSelenoidContainerLabels());
        setCapability("sessionTimeout", PROPERTIES.getSelenoidSessionTimeout());

        return capabilities;
    }

    private String getProperty(String property) {
        return property.trim().isEmpty() && Environment.getScenario() != null ? Environment.getScenario().getName() : property;
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
