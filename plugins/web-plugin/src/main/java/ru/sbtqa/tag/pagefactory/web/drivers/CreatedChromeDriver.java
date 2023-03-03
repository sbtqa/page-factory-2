package ru.sbtqa.tag.pagefactory.web.drivers;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.internal.Require;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.web.capabilities.WebDriverCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.web.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CreatedChromeDriver implements Supplier<WebDriver> {

    private final DesiredCapabilities capabilities;

    public CreatedChromeDriver(final DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public WebDriver get() {
        WebDriverManagerConfigurator.configureDriver(ChromeDriverManager.getInstance(), BrowserName.CHROME.getName());

        // TODO: temporary workaround of ClassCast exception. Check every new selenium version
        ChromeExt options = new ChromeExt();
        options.mergeIn(capabilities);
        options.addArguments(new WebDriverCapabilitiesParser().getOptions(capabilities));

        return new ChromeDriver(options);
    }

    private static class ChromeExt extends ChromeOptions {
        public ChromeExt() {
            super();
        }

        public void mergeIn(Capabilities extraCapabilities) {
            Require.nonNull("Capabilities to merge", extraCapabilities);
            super.mergeInPlace(extraCapabilities);
            this.mergeInOptionsFromCaps(CAPABILITY, extraCapabilities);
        }

        protected void mergeInOptionsFromCaps(String capabilityName, Capabilities capabilities) {
            if (!(capabilities instanceof ChromiumOptions)) {
                Object object = capabilities.getCapability(capabilityName);

                if (object instanceof Map) {
                    @SuppressWarnings("unchecked") Map<String, Object> options = (Map<String, Object>) object;
                    Object args = options.getOrDefault("args", new ArrayList<String>());

                    @SuppressWarnings("unchecked")
                    List<String> arguments = args instanceof Object[]
                            ? Arrays.stream((Object[]) args).map(Object::toString).collect(Collectors.toList())
                            : (List<String>) args;
                    arguments.forEach(this::addArguments);

                    @SuppressWarnings("unchecked")
                    List<Object> extensionList =
                            (List<Object>) (options.getOrDefault("extensions", new ArrayList<>()));

                    extensionList.forEach(extension -> {
                        if (extension instanceof File) {
                            addExtensions((File) extension);
                        } else if (extension instanceof String) {
                            addEncodedExtensions((String) extension);
                        }
                    });

                    Object binary = options.get("binary");
                    if (binary instanceof String) {
                        setBinary((String) binary);
                    } else if (binary instanceof File) {
                        setBinary((File) binary);
                    }

                    options.forEach((k, v) -> {
                        if (!k.equals("binary") && !k.equals("extensions") && !k.equals("args")) {
                            setExperimentalOption(k, v);
                        }
                    });
                }
            }
        }
    }
}
