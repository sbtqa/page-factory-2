package ru.sbtqa.tag.pagefactory.web.configure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.github.bonigarcia.wdm.Architecture;
import io.github.bonigarcia.wdm.BrowserManager;
import io.github.bonigarcia.wdm.OperativeSystem;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

public class WebDriverManagerConfigurator {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverManagerConfigurator.class);
    private static final WebConfiguration PROPERTIES = WebConfiguration.create();

    private static final String MAPPING_FILES_PATH = "drivers/mapping/";
    private static final String MAPPING_FILES_EXTENSION = ".json";

    private WebDriverManagerConfigurator() {}

    public static void configureDriver(BrowserManager webDriverManager, String browserType) {
        if (!PROPERTIES.getDriversPath().isEmpty()) {
            System.setProperty("webdriver." + browserType.toLowerCase() + ".driver", new File(PROPERTIES.getDriversPath()).getAbsolutePath());
        } else {
            LOG.warn("The value of property 'webdriver.drivers.path' is not specified."
                    + " Trying to automatically download and setup driver.");

            configureWebDriverManagerParams(webDriverManager, browserType);
            webDriverManager.setup();
        }
    }

    private static void configureWebDriverManagerParams(BrowserManager webDriverManager, String browserType) {
        configureWebDriverManagerVersion(webDriverManager, browserType);
        configureWebDriverManagerArch(webDriverManager);
        configureWebDriverManagerNexusLink(webDriverManager);
    }

    private static void configureWebDriverManagerVersion(BrowserManager webDriverManager, String browserType) {
        String driverVersion = null;
        if (PROPERTIES.getWebDriverVersion().isEmpty()) {
            LOG.info("Trying to determine driver version based on browser version.");
            if (PROPERTIES.getBrowserVersion().isEmpty()) {
                if (browserType.equalsIgnoreCase(BrowserName.IE.toString())) {
                    LOG.warn("You use IE browser. Switching to LATEST driver version. "
                            + "You can specify driver version by using 'webdriver.version' param.");
                } else {
                    driverVersion = parseDriverVersionFromMapping(detectBrowserVersion(), browserType.toLowerCase());
                }
            } else {
                driverVersion = parseDriverVersionFromMapping(PROPERTIES.getBrowserVersion(), browserType.toLowerCase());
            }
        } else {
            LOG.info("Forcing driver version to {}", PROPERTIES.getWebDriverVersion());
            driverVersion = PROPERTIES.getWebDriverVersion();
        }
        if (driverVersion == null && !browserType.equalsIgnoreCase(BrowserName.IE.toString())) {
            LOG.warn("Can't determine driver version. Rolling back to LATEST by default.");
        }
        webDriverManager.version(driverVersion);
    }

    private static void configureWebDriverManagerNexusLink(BrowserManager webDriverManager) {
        if (!PROPERTIES.getNexusUrl().isEmpty()) {
            webDriverManager.useNexus(PROPERTIES.getNexusUrl());
        }
    }

    private static void configureWebDriverManagerArch(BrowserManager webDriverManager) {
        String osArchitecture = PROPERTIES.getOsArchitecture();
        if (!osArchitecture.isEmpty()) {
            switch (Architecture.valueOf("X" + PROPERTIES.getOsArchitecture())) {
                case X32:
                    LOG.info("Forcing driver arch to X{}", PROPERTIES.getOsArchitecture());
                    webDriverManager.arch32();
                    break;
                case X64:
                    LOG.info("Forcing driver arch to X{}", PROPERTIES.getOsArchitecture());
                    webDriverManager.arch64();
                    break;
            }
        }
    }

    private static String parseDriverVersionFromMapping(String browserVersion, String browserType) {
        if (browserVersion == null) {
            return null;
        }
        LOG.info("Trying to find driver corresponding to {} browser version.", browserVersion);

        JsonObject mappingObject = getResourceJsonFileAsJsonObject(MAPPING_FILES_PATH + browserType + MAPPING_FILES_EXTENSION);
        JsonElement browserVersionElement;
        if (mappingObject != null && (browserVersionElement = mappingObject.get(browserVersion)) != null) {
            return browserVersionElement.getAsString();
        } else {
            LOG.warn("Can't get corresponding driver for {} browser version. "
                    + "Using LATEST driver version.", browserVersion);
            return null;
        }
    }

    private static JsonObject getResourceJsonFileAsJsonObject(String filePath) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(filePath)));
        JsonReader reader = new JsonReader(new BufferedReader(isr));
        JsonParser parser = new JsonParser();
        return parser.parse(reader).getAsJsonObject();
    }

    private static String detectBrowserVersion() {
        LOG.info("The value of property 'webdriver.browser.version' is not specified. "
                + "Trying to detect your browser version automatically.");

        final String recommendMessage = "Please specify your browser version by "
                + "setting 'webdriver.browser.version' param.";
        final String errorMessage = "Error while detecting browser version.";

        OperativeSystem os = getDefaultOS();
        List<String> commandsToGetVersion;

        if (os != null) {
            commandsToGetVersion = getChromeCommands(os);
        } else {
            LOG.error("{} Can't get current OS. {}", errorMessage, recommendMessage);
            return null;
        }

        if (commandsToGetVersion.isEmpty() {
            return null;
        }

        ProcessBuilder builder = new ProcessBuilder(commandsToGetVersion);
        builder.redirectErrorStream(true);
        Process p;
        try {
            p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while ((line = r.readLine()) != null) {
                Pattern versionPattern = Pattern.compile("(\\d+)\\S*");
                Matcher versionMatcher = versionPattern.matcher(line);
                if (versionMatcher.find()) {
                    return versionMatcher.group(1);
                }
            }
        } catch (IOException e) {
            LOG.error("Error while reading browser version from terminal.", e);
            return null;
        }
        LOG.error("Can't find browser binary in default location.");
        return null;
    }

    private static OperativeSystem getDefaultOS() {
        OperativeSystem os = null;
        if (IS_OS_WINDOWS) {
            os = OperativeSystem.WIN;
        } else if (IS_OS_LINUX) {
            os = OperativeSystem.LINUX;
        } else if (IS_OS_MAC) {
            os = OperativeSystem.MAC;
        }
        return os;
    }

    private static List<String> getChromeCommands(OperativeSystem os) {
        final String recommendMessage = "Please specify your browser version by "
                + "setting 'webdriver.browser.version' param.";
        List<String> commands = new ArrayList<>();
        String path = PROPERTIES.getBrowserPath();

        switch (os) {
            case MAC: {
                LOG.warn("This OS is not supported for 'browser-driver' mapping yet. {}", recommendMessage);
                break;
            }
            case WIN: {
                if (path.isEmpty()) {
                    path = "C:\\\\Program Files (x86)\\\\Google\\\\Chrome\\\\Application\\\\";
                }
                commands = Arrays.asList("cmd.exe", "/c", "wmic datafile where name=\"" + path + "chrome.exe\" get Version /value");
                break;
            }
            case LINUX: {
                if (path.isEmpty()) {
                    path = "/usr/bin/";
                }
                commands = Arrays.asList("/bin/bash", "-c", path + "google-chrome --version");
                break;
            }
        }
        return commands;
    }
}
