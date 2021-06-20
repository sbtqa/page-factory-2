package ru.sbtqa.tag.pagefactory.web.drivers;

import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.web.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

import java.util.function.Supplier;

public class CreatedInternetExplorerDriver implements Supplier<WebDriver> {

    private final DesiredCapabilities capabilities;

    public CreatedInternetExplorerDriver(final DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public WebDriver get() {
        WebDriverManagerConfigurator.configureDriver(InternetExplorerDriverManager.getInstance(), BrowserName.IE.getName());
        return new InternetExplorerDriver(new InternetExplorerOptions().merge(capabilities));
    }
}
