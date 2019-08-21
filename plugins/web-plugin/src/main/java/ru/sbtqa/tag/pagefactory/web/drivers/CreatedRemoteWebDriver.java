package ru.sbtqa.tag.pagefactory.web.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.sbtqa.tag.pagefactory.web.capabilities.SelenoidCapabilitiesParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

public class CreatedRemoteWebDriver implements Supplier<WebDriver> {

    private final URL remoteUrl;
    private final DesiredCapabilities capabilities;

    public CreatedRemoteWebDriver(String webDriverUrl, DesiredCapabilities capabilities) throws MalformedURLException {
        this.remoteUrl = new URL(webDriverUrl);
        this.capabilities = capabilities;
        this.capabilities.merge(new SelenoidCapabilitiesParser().parse());
    }

    @Override
    public WebDriver get() {
        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(remoteUrl, capabilities);
        remoteWebDriver.setFileDetector(new LocalFileDetector());
        return remoteWebDriver;
    }
}
