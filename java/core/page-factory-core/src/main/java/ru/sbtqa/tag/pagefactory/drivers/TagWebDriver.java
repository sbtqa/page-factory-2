package ru.sbtqa.tag.pagefactory.drivers;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.exceptions.UnsupportedBrowserException;
import ru.sbtqa.tag.pagefactory.support.DesiredCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.pagefactory.support.SelenoidCapabilitiesProvider;
import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.FIREFOX;
import static org.openqa.selenium.remote.BrowserType.IE;
import static org.openqa.selenium.remote.BrowserType.IEXPLORE;
import static org.openqa.selenium.remote.BrowserType.IE_HTA;
import static org.openqa.selenium.remote.BrowserType.SAFARI;
import org.openqa.selenium.remote.CapabilityType;
import static ru.sbtqa.tag.pagefactory.PageFactory.getTimeOutInSeconds;
import ru.sbtqa.tag.pagefactory.drivers.configure.ProxyConfigurator;
import ru.sbtqa.tag.pagefactory.drivers.configure.WebDriverManagerConfigurator;
import ru.sbtqa.tag.pagefactory.support.properties.Configuration;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;

public class TagWebDriver {

    private static final Logger LOG = LoggerFactory.getLogger(TagWebDriver.class);

    private static WebDriver webDriver;
    private static final Configuration PROPERTIES = Properties.INSTANCE.getProperties();

    private TagWebDriver() {
    }

    public static WebDriver getDriver() {
        if (Environment.WEB != PageFactory.getEnvironment()) {
            throw new FactoryRuntimeException("Failed to get web driver while environment is not web");
        }

        if (null == webDriver) {
            startDriver();
        }

        return webDriver;
    }

    private static void startDriver() {
        for (int i = 1; i <= PROPERTIES.getWebDriverCreateAttempts(); i++) {
            LOG.info("Attempt {} to start web driver", i);
            try {
                createDriver();
                break;
            } catch (UnreachableBrowserException e) {
                LOG.warn("Failed to create web driver. Attempt number {}", i, e);
                dispose();
            } catch (UnsupportedBrowserException | MalformedURLException e) {
                LOG.error("Failed to create web driver", e);
                break;
            }
        }
    }

    private static void createDriver() throws UnsupportedBrowserException, MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilitiesParser().parse();

        if (!PROPERTIES.getProxy().isEmpty()) {
            Proxy seleniumProxy = ProxyConfigurator.configureProxy();
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        }

        String browserName = getBrowserName();
        capabilities.setBrowserName(browserName);

        String webDriverUrl = PROPERTIES.getWebDriverUrl();
        if (!webDriverUrl.isEmpty()) {
            setWebDriver(createRemoteWebDriver(webDriverUrl, capabilities));
        } else {
            if (browserName.equalsIgnoreCase(FIREFOX)) {
                setWebDriver(new FirefoxDriver(capabilities));
            } else if (browserName.equalsIgnoreCase(SAFARI)) {
                setWebDriver(new SafariDriver(capabilities));
            } else if (browserName.equalsIgnoreCase(CHROME)) {
                WebDriverManagerConfigurator.configureDriver(ChromeDriverManager.getInstance(), CHROME);
                setWebDriver(new ChromeDriver(capabilities));
            } else if (isIE()) {
                WebDriverManagerConfigurator.configureDriver(InternetExplorerDriverManager.getInstance(), IE);
                setWebDriver(new InternetExplorerDriver(capabilities));
            } else {
                throw new UnsupportedBrowserException("'" + browserName + "' is not supported yet");
            }
        }

        webDriver.manage().timeouts().pageLoadTimeout(getTimeOutInSeconds(), TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        webDriver.get(PROPERTIES.getStartingUrl());
    }

    private static WebDriver createRemoteWebDriver(String webDriverUrl, DesiredCapabilities capabilities) throws MalformedURLException {
        URL remoteUrl = new URL(webDriverUrl);
        SelenoidCapabilitiesProvider.apply(capabilities);
        return new RemoteWebDriver(remoteUrl, capabilities);
    }

    private static boolean isIE() {
        String browserName = getBrowserName();
        return browserName.equalsIgnoreCase(IE)
                || browserName.equalsIgnoreCase(IE_HTA)
                || browserName.equalsIgnoreCase(IEXPLORE);
    }

    public static void dispose() {
        if (webDriver == null) {
            return;
        }

        closeAllAlerts();
        closeAllWindowHandles();

        if (isIE() && PROPERTIES.isIEKillOnDispose()) {
            terminateProcessIE();
        }

        try {
            webDriver.quit();
        } finally {
            setWebDriver(null);
        }
    }

    private static void closeAllAlerts() {
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

    private static void closeAllWindowHandles() {
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

    private static void terminateProcessIE() {
        try {
            LOG.info("Trying to terminate iexplorer processes");
            Runtime.getRuntime().exec("taskkill /f /im iexplore.exe").waitFor();
            LOG.info("All iexplorer processes were terminated");
        } catch (IOException | InterruptedException e) {
            LOG.warn("Failed to wait for browser processes finish", e);
        }
    }

    public static void setWebDriver(WebDriver aWebDriver) {
        webDriver = aWebDriver;
    }

    public static String getBrowserName() {
        return adaptBrowserName(PROPERTIES.getBrowserName());
    }

    private static String adaptBrowserName(String browserName) {
        return browserName.equalsIgnoreCase("ie") ? IE : browserName;
    }

    public static boolean isDriverInitialized() {
        return webDriver != null;
    }
}
