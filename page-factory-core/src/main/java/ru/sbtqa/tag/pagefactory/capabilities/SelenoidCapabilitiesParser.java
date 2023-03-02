package ru.sbtqa.tag.pagefactory.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class SelenoidCapabilitiesParser implements CapabilitiesParser {

    private static final Logger LOG = LoggerFactory.getLogger(SelenoidCapabilitiesParser.class);

    private static final Configuration PROPERTIES = Configuration.create();
    private static final String UNIQUE_NAME_FORMAT = new SimpleDateFormat("dd.MM.yyyy_hh:mm:ss").format(new Date()) + "_%s_" + UUID.randomUUID();

    protected final DesiredCapabilities capabilities = new DesiredCapabilities();

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

        HashMap<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", PROPERTIES.getSelenoidEnableVNC());
        selenoidOptions.put("enableLog", PROPERTIES.getSelenoidEnableLog());
        selenoidOptions.put("screenResolution", PROPERTIES.getSelenoidScreenResolution());
        selenoidOptions.put("enableVideo", PROPERTIES.getSelenoidEnableVideo());
        selenoidOptions.put("videoScreenSize", PROPERTIES.getSelenoidVideoScreenSize());
        if (!PROPERTIES.getSelenoidSkin().equals("")) {
            selenoidOptions.put("skin", PROPERTIES.getSelenoidSkin());
        }
        if (!PROPERTIES.getSelenoidVideoFrameRate().equals("")) {
            selenoidOptions.put("videoFrameRate", Integer.parseInt(PROPERTIES.getSelenoidVideoFrameRate()));
        }
        selenoidOptions.put("name", name);
        selenoidOptions.put("logName", String.format(UNIQUE_NAME_FORMAT + ".log", logName));
        selenoidOptions.put("videoName", String.format(UNIQUE_NAME_FORMAT + ".mp4", videoName));
        selenoidOptions.put("timeZone", PROPERTIES.getSelenoidTimeZone());

        if (!PROPERTIES.getSelenoidApplicationContainers().equals("")) {
            selenoidOptions.put("applicationContainers", Arrays.asList(PROPERTIES.getSelenoidApplicationContainers().split(",")));
        }
        if (!PROPERTIES.getSelenoidHostEntries().equals("")) {
            selenoidOptions.put("hostsEntries", Arrays.asList(PROPERTIES.getSelenoidHostEntries().split(",")));
        }
        if (!PROPERTIES.getSelenoidContainerLabels().equals("")) {
            selenoidOptions.put("labels", Arrays.asList(PROPERTIES.getSelenoidContainerLabels().split(",")));
        }
        selenoidOptions.put("sessionTimeout", PROPERTIES.getSelenoidSessionTimeout());

        capabilities.setCapability("selenoid:options", selenoidOptions);
        return capabilities;
    }

    private String getProperty(String property) {
        return property.trim().isEmpty() && Environment.getScenario() != null ? Environment.getScenario().getName() : property;
    }

    private void setVersion(String capabilityValue) {
        if (!capabilityValue.isEmpty()) {
            capabilities.setVersion(capabilityValue);
        } else {
            LOG.info("Capability \"version\" for Selenoid isn't set. Using default capability.");
        }
    }
}
