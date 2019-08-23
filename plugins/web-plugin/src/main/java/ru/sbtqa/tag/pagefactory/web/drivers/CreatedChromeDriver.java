package ru.sbtqa.tag.pagefactory.web.drivers;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.web.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

import java.util.function.Supplier;

public class CreatedChromeDriver implements Supplier<WebDriver> {

    private final ChromeOptions options;

    public CreatedChromeDriver(final DesiredCapabilities capabilities) {
        options = new ChromeOptions().merge(capabilities);
    }

    @Override
    public WebDriver get() {
        WebDriverManagerConfigurator.configureDriver(ChromeDriverManager.getInstance(), BrowserName.CHROME.getName());
        return new ChromeDriver(options);
    }
}
