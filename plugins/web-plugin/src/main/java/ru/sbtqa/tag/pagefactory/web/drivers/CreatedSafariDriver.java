package ru.sbtqa.tag.pagefactory.web.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import java.util.function.Supplier;

public class CreatedSafariDriver implements Supplier<WebDriver> {

    private final DesiredCapabilities capabilities;

    public CreatedSafariDriver(final DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public WebDriver get() {
        return new SafariDriver(capabilities);
    }
}
