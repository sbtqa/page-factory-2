package ru.sbtqa.tag.pagefactory.web.drivers;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.web.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

import java.util.function.Supplier;

public class CreatedChromeDriver implements Supplier<WebDriver> {

    private final DesiredCapabilities capabilities;

    public CreatedChromeDriver(final DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public WebDriver get() {
        WebDriverManagerConfigurator.configureDriver(ChromeDriverManager.getInstance(), BrowserName.CHROME.getName());
        return new ChromeDriver(capabilities);
    }
}
