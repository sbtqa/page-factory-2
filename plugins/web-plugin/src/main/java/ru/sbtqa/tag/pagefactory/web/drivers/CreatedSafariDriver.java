package ru.sbtqa.tag.pagefactory.web.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.function.Supplier;

public class CreatedSafariDriver implements Supplier<WebDriver> {

    private final SafariOptions options;

    public CreatedSafariDriver(final DesiredCapabilities capabilities) {
        options = new SafariOptions().merge(capabilities);
    }

    @Override
    public WebDriver get() {
        return new SafariDriver(options);
    }
}
