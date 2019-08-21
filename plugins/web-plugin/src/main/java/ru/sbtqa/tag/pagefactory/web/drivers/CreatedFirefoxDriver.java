package ru.sbtqa.tag.pagefactory.web.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.function.Supplier;

public class CreatedFirefoxDriver implements Supplier<WebDriver> {

    private final FirefoxOptions options;

    public CreatedFirefoxDriver(final DesiredCapabilities capabilities) {
        options = new FirefoxOptions();
        options.merge(capabilities);
    }

    @Override
    public WebDriver get() {
        return new FirefoxDriver(options);
    }
}
