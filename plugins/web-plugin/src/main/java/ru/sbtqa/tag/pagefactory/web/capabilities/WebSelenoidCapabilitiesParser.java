package ru.sbtqa.tag.pagefactory.web.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.capabilities.SelenoidCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.web.environment.WebEnvironment;

import java.util.HashMap;
import java.util.Map;

public class WebSelenoidCapabilitiesParser extends SelenoidCapabilitiesParser {

    private final DesiredCapabilities capabilities = new DesiredCapabilities();

    @Override
    public DesiredCapabilities parse() {
        super.parse();
        setBrowserDefaultCapabilities();

        return capabilities;
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