package ru.sbtqa.tag.pagefactory.web.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.function.Supplier;

public class CreatedFirefoxDriver implements Supplier<WebDriver> {

    private final DesiredCapabilities capabilities;

    public CreatedFirefoxDriver(final DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public WebDriver get() {
        return new FirefoxDriver();
    }
}
