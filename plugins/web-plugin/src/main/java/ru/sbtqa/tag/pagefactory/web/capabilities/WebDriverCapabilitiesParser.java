package ru.sbtqa.tag.pagefactory.web.capabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.web.environment.WebEnvironment;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;
import ru.sbtqa.tag.qautils.properties.Props;

public class WebDriverCapabilitiesParser implements CapabilitiesParser {

    private static final String BROWSER_NAME = Props.get("webdriver.browser.name").toLowerCase();

    // prefix is a 'webdriver.ie.capability.' or 'webdriver.*.capability.' part
    private static final String CAPABILITY_WITH_PREFIX_REGEX = "webdriver.(" + BROWSER_NAME + "|\\*).capability.(.*)";

    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    private final Map<String, Object> chromeOptions = new HashMap<>();

    @Override
    public DesiredCapabilities parse() {
        Set<String> properties = new HashSet<>();
        properties.addAll(Props.getProps().stringPropertyNames());
        properties.addAll(System.getenv().keySet());
        properties.addAll(System.getProperties().stringPropertyNames());
        List<String> capabilitiesWithPrefix = getCapabilitiesWithPrefix(properties, CAPABILITY_WITH_PREFIX_REGEX);

        for (String capabilityWithPrefix : capabilitiesWithPrefix) {

            String capabilityName = cutPrefix(capabilityWithPrefix);
            String capabilityValue = Props.get(capabilityWithPrefix);
            if (System.getenv(capabilityWithPrefix) != null){
                capabilityValue = System.getenv(capabilityWithPrefix);
            }
            if (System.getProperty(capabilityWithPrefix) != null) {
                capabilityValue = System.getProperty(capabilityWithPrefix);
            }

            if (BrowserName.CHROME.equals(WebEnvironment.getBrowserName())) {
                cacheChromeOptions(capabilityName, capabilityValue);
            }

            setCapability(capabilityName, capabilityValue);
        }

        if (!chromeOptions.isEmpty()) {
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        return capabilities;
    }

    private List<String> getCapabilitiesWithPrefix(Set<String> properties, String capabilityRegex) {
        List<String> capabilitiesInProperties = new ArrayList<>();

        for (String property : properties) {
            if (property.matches(capabilityRegex)) {
                capabilitiesInProperties.add(property);
            }
        }

        return capabilitiesInProperties;
    }

    private String cutPrefix(String capabilityWithPrefix) {
        Matcher matcher = Pattern.compile(CAPABILITY_WITH_PREFIX_REGEX).matcher(capabilityWithPrefix);
        matcher.find();
        return matcher.group(2);
    }

    private void cacheChromeOptions(String capabilityName, String capabilityValue) {
        if (capabilityName.startsWith("options")) {
            capabilityName = capabilityName.replaceFirst("options.", "");
            chromeOptions.putAll(getOptionsChromeCapabilities(capabilityName, capabilityValue));
        }
    }

    private Map<String, Object> getOptionsChromeCapabilities(String capabilityName, String capabilityValue) {
        Map<String, Object> options = new HashMap<>();
        String[] valueItems = capabilityValue.split(",");

        switch (capabilityName) {
            case "args":
            case "extensions":
            case "excludeSwitches":
            case "windowTypes":
                List<String> listOfStrings = new ArrayList<>();

                for (String item : valueItems) {
                    listOfStrings.add(item.trim());
                }

                if (!listOfStrings.isEmpty()) {
                    options.put(capabilityName, listOfStrings.toArray());
                }
                break;

            case "prefs":
            case "mobileEmulation":
            case "perfLoggingPrefs":
                Map<String, Object> dictionary = new HashMap<>();

                for (String item : valueItems) {
                    String[] keyVal = item.split("=>");
                    dictionary.put(keyVal[0], keyVal[1].trim());
                }

                if (!dictionary.isEmpty()) {
                    options.put(capabilityName, dictionary);
                }
                break;

            default:
                options.put(capabilityName, capabilityValue);
                break;
        }

        return options;
    }

    private void setCapability(String capabilityName, String capabilityValue) {
        if (isBoolean(capabilityValue)) {
            capabilities.setCapability(capabilityName, Boolean.valueOf(capabilityValue));
        } else {
            capabilities.setCapability(capabilityName, capabilityValue);
        }
    }

    private boolean isBoolean(String string) {
        return "true".equalsIgnoreCase(string) || "false".equalsIgnoreCase(string);
    }
}
