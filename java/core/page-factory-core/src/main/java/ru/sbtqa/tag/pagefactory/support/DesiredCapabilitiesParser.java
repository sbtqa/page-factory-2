package ru.sbtqa.tag.pagefactory.support;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.qautils.properties.Props;

import java.util.*;

public class DesiredCapabilitiesParser {

    /**
     * Parses desired capabilities from config
     *
     * @return built capabilities
     */
    public DesiredCapabilities parse() {
        final DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);

        final String capsPrefix = "webdriver." + TagWebDriver.getBrowserName().toLowerCase() + ".capability.";
        Set<String> propKeys = Props.getInstance().getProps().stringPropertyNames();
        List<String> capabilitiesFromProps = new ArrayList<>();

        for (String prop : propKeys) {
            if (prop.startsWith(capsPrefix)) {
                capabilitiesFromProps.add(prop);
            }
        }

        final Map<String, Object> options = new HashMap<>();

        for (String rawCapabilityKey : capabilitiesFromProps) {

            String capability = rawCapabilityKey.substring(capsPrefix.length());
            String capabilityValue = Props.get(rawCapabilityKey);

            if (capability.startsWith("options") && "Chrome".equals(TagWebDriver.getBrowserName())) {
                // For Chrome options must be parsed and specified as a data structure.
                // For non-chrome browsers options could be passed as string
                String optionsCapability = capability.substring("options.".length());
                switch (optionsCapability) {
                    case "args":
                    case "extensions":
                    case "excludeSwitches":
                    case "windowTypes":
                        String[] arrayOfStrings = capabilityValue.split(",");
                        final List<String> listOfStrings = new ArrayList<>();

                        for (String item : arrayOfStrings) {
                            listOfStrings.add(item.trim());
                        }

                        if (!listOfStrings.isEmpty()) {
                            options.put(optionsCapability, listOfStrings.toArray());
                        }
                        break;
                    case "prefs":
                    case "mobileEmulation":
                    case "perfLoggingPrefs":
                        final Map<String, Object> dictionary = new HashMap<>();
                        String[] dictRows = capabilityValue.split(",");

                        for (String row : dictRows) {
                            String[] keyVal = row.split("=>");
                            dictionary.put(keyVal[0], keyVal[1].trim());
                        }

                        if (!dictionary.isEmpty()) {
                            options.put(optionsCapability, dictionary);
                        }
                        break;
                    default:
                        options.put(optionsCapability, capabilityValue);
                        break;
                }
                if (!options.isEmpty()) {
                    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                }
            } else {
                if ("true".equalsIgnoreCase(capabilityValue) ||
                        "false".equalsIgnoreCase(capabilityValue)) {
                    capabilities.setCapability(capability, Boolean.valueOf(capabilityValue));
                } else {
                    capabilities.setCapability(capability, capabilityValue);
                }
            }
        }
        return capabilities;
    }
}
