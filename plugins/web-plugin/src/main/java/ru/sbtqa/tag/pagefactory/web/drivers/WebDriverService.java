package ru.sbtqa.tag.pagefactory.web.drivers;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;
import ru.sbtqa.tag.pagefactory.exceptions.UnsupportedBrowserException;
import ru.sbtqa.tag.pagefactory.web.capabilities.SelenoidCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.web.capabilities.WebDriverCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.web.configure.ProxyConfigurator;
import ru.sbtqa.tag.pagefactory.web.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.web.environment.WebEnvironment;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;
import ru.sbtqa.tag.pagefactory.web.support.BrowserName;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebDriverService implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverService.class);
    private static final WebConfiguration PROPERTIES = ConfigFactory.create(WebConfiguration.class);

    private WebDriver webDriver;

    @Override
    public WebDriver getDriver() {
        return webDriver;
    }

    @Override
    public void mountDriver() {
        for (int i = 1; i <= PROPERTIES.getWebDriverCreateAttempts(); i++) {
            LOG.info("Attempt {} to start web driver", i);
            try {
                createDriver();
            } catch (UnreachableBrowserException e) {
                LOG.warn("Failed to create web driver. Attempt number {}", i, e);
                dispose();
                continue;
            } catch (UnsupportedBrowserException | MalformedURLException e) {
                LOG.error("Failed to create web driver", e);
            }
            return;
        }
    }

    private void createDriver() throws UnsupportedBrowserException, MalformedURLException {
        DesiredCapabilities capabilities = new WebDriverCapabilitiesParser().parse();

        if (!PROPERTIES.getProxy().isEmpty()) {
            Proxy seleniumProxy = ProxyConfigurator.configureProxy();
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        }

        BrowserName browserName = WebEnvironment.getBrowserName();
        capabilities.setBrowserName(browserName.getName());

        String webDriverUrl = PROPERTIES.getWebDriverUrl();
        if (!webDriverUrl.isEmpty()) {
            setWebDriver(createRemoteWebDriver(webDriverUrl, capabilities));
        } else {
            if (browserName.equals(BrowserName.FIREFOX)) {
                setWebDriver(new FirefoxDriver(capabilities));
            } else if (browserName.equals(BrowserName.SAFARI)) {
                setWebDriver(new SafariDriver(capabilities));
            } else if (browserName.equals(BrowserName.CHROME)) {
                WebDriverManagerConfigurator.configureDriver(ChromeDriverManager.getInstance(), BrowserName.CHROME.getName());
                setWebDriver(new ChromeDriver(capabilities));
            } else if (browserName.equals(BrowserName.INTERNET_EXPLORER)) {
                WebDriverManagerConfigurator.configureDriver(InternetExplorerDriverManager.getInstance(), BrowserName.IE.getName());
                setWebDriver(new InternetExplorerDriver(capabilities));
            } else {
                throw new UnsupportedBrowserException("'" + browserName + "' is not supported yet");
            }
        }

        webDriver.manage().timeouts().pageLoadTimeout(PROPERTIES.getTimeout(), TimeUnit.SECONDS);
        setBrowserSize();
        webDriver.get(PROPERTIES.getStartingUrl());
    }

    private WebDriver createRemoteWebDriver(String webDriverUrl, DesiredCapabilities capabilities) throws MalformedURLException {
        URL remoteUrl = new URL(webDriverUrl);
        capabilities.merge(new SelenoidCapabilitiesParser().parse());
        return new RemoteWebDriver(remoteUrl, capabilities);
    }

    private void setBrowserSize() {
        if (PROPERTIES.getBrowserSize().isEmpty()) {
            webDriver.manage().window().maximize();
        } else {
            String[] size = PROPERTIES.getBrowserSize().split("x");
            int width = Integer.parseInt(size[0]);
            int height = Integer.parseInt(size[1]);
            webDriver.manage().window().setSize(new Dimension(width, height));
        }
    }


    @Override
    public void demountDriver() {
        dispose();
    }

    public void dispose() {
        if (webDriver == null) {
            return;
        }

        closeAllAlerts();
        closeAllWindowHandles();

        if (BrowserName.IE.equals(WebEnvironment.getBrowserName())
                && PROPERTIES.isIEKillOnDispose()) {
            terminateProcessIE();
        }

        try {
            webDriver.quit();
        } finally {
            setWebDriver(null);
        }
    }

    private void closeAllAlerts() {
        try {
            LOG.info("Checking any alert opened");
            WebDriverWait alertAwaiter = new WebDriverWait(webDriver, 2);
            alertAwaiter.until(ExpectedConditions.alertIsPresent());
            Alert alert = webDriver.switchTo().alert();
            LOG.info("Got an alert: '{}'. Closing it...", alert.getText());
            alert.dismiss();
        } catch (WebDriverException e) {
            LOG.debug("No alert opened. Closing webdriver...", e);
        }
    }

    private void closeAllWindowHandles() {
        Set<String> windowHandlesSet = webDriver.getWindowHandles();
        try {
            if (windowHandlesSet.size() > 1) {
                for (String winHandle : windowHandlesSet) {
                    webDriver.switchTo().window(winHandle);
                    ((JavascriptExecutor) webDriver).executeScript(
                            "var objWin = window.self;"
                            + "objWin.open('','_self','');"
                            + "objWin.close();");
                }
            }
        } catch (Exception e) {
            LOG.warn("Failed to kill all of the windows", e);
        }
    }

    private void terminateProcessIE() {
        try {
            LOG.info("Trying to terminate iexplorer processes");
            Runtime.getRuntime().exec("taskkill /f /im iexplore.exe").waitFor();
            LOG.info("All iexplorer processes were terminated");
        } catch (IOException | InterruptedException e) {
            LOG.warn("Failed to wait for browser processes finish", e);
            Thread.currentThread().interrupt();
        }
    }

    public void setWebDriver(org.openqa.selenium.WebDriver aWebDriver) {
        webDriver = aWebDriver;
    }
}
