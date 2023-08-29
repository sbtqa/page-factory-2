package ru.sbtqa.tag.pagefactory.web.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.web.capabilities.WebDriverCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.web.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

import java.util.function.Supplier;

public class CreatedFirefoxDriver implements Supplier<WebDriver> {

    private final DesiredCapabilities capabilities;

    public CreatedFirefoxDriver(final DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public WebDriver get() {
        WebDriverManagerConfigurator.configureDriver(WebDriverManager.getInstance(), BrowserName.FIREFOX.getName());
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments(new WebDriverCapabilitiesParser().getOptions(capabilities));

        return new FirefoxDriver(options);
    }
}
