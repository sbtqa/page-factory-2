package ru.sbtqa.tag.pagefactory.web.drivers;

import io.github.bonigarcia.wdm.EdgeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.sbtqa.tag.pagefactory.web.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

import java.util.function.Supplier;

public class CreatedEdgeDriver implements Supplier<WebDriver> {

    private final DesiredCapabilities capabilities;

    public CreatedEdgeDriver(final DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public WebDriver get() {
        WebDriverManagerConfigurator.configureDriver(EdgeDriverManager.getInstance(), BrowserName.EDGE.getName());
        return new EdgeDriver();
    }
}
