package ru.sbtqa.tag.pagefactory.web.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.web.environment.WebEnvironment;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelenoidCapabilitiesParser implements CapabilitiesParser {

    private static final Logger LOG = LoggerFactory.getLogger(SelenoidCapabilitiesParser.class);

    private static final WebConfiguration PROPERTIES = WebConfiguration.create();
    private static final String VIDEONAME_FORMAT = new SimpleDateFormat("dd.MM.yyyy'_'hh:mm:ss").format(new Date()) + '_' + UUID.randomUUID().toString() + "_%s";

    private final DesiredCapabilities capabilities = new DesiredCapabilities();

    @Override
    public DesiredCapabilities parse() {
        setVersion(PROPERTIES.getSelenoidBrowserVersion());
        
        setCapability("enableVNC", PROPERTIES.getSelenoidEnableVNC());
        setCapability("screenResolution", PROPERTIES.getSelenoidScreenResolution());
        setCapability("enableVideo", PROPERTIES.getSelenoidEnableVideo());
        setCapability("videoName", PROPERTIES.getSelenoidVideoName(), VIDEONAME_FORMAT);
        setCapability("videoScreenSize", PROPERTIES.getSelenoidVideoScreenSize());
        setCapability("videoFrameRate", PROPERTIES.getSelenoidVideoFrameRate());
        setCapability("name", PROPERTIES.getSelenoidNameOfTests());
        setCapability("timeZone", PROPERTIES.getSelenoidTimeZone());
        setCapability("hostsEntries", PROPERTIES.getSelenoidHostEntries());
        setCapability("applicationContainers", PROPERTIES.getSelenoidApplicationContainers());
        setCapability("labels", PROPERTIES.getSelenoidContainerLables());
        setCapability("sessionTimeout", PROPERTIES.getSelenoidSessionTimeout());

        setBrowserDefaultCapabilities();

        return capabilities;
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
            LOG.info("Capability \"browserVersion\" for Selenoid isn't set. Using default capability.");
        }
    }

    private void setBrowserDefaultCapabilities() {
        switch (WebEnvironment.getBrowserName()) {
            case OPERA:
                handleOpera();
                break;
            case IE:
                handleIe();
                break;
            default:
                break;
        }
    }

    private void handleOpera() {
        Map<String, String> operaOptions = new HashMap<>();
        operaOptions.put("binary", "/usr/bin/opera");

        capabilities.setCapability("operaOptions", operaOptions);
    }

    private void handleIe() {
        capabilities.setCapability("ie.usePerProcessProxy", true);
        capabilities.setCapability("ie.browserCommandLineSwitches", "-private");
        capabilities.setCapability("ie.ensureCleanSession", true);
        capabilities.setCapability("requireWindowFocus", false);
    }
}
