package ru.sbtqa.tag.pagefactory.drivers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;
import static ru.sbtqa.tag.pagefactory.PageFactory.getTimeOutInSeconds;

import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.exceptions.UnsupportedBrowserException;
import ru.sbtqa.tag.pagefactory.support.DesiredCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.qautils.properties.Props;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class TagWebDriver {

    private static final Logger LOG = LoggerFactory.getLogger(TagWebDriver.class);

    private static WebDriver webDriver;
    private static BrowserMobProxy proxy;
    private static final int WEBDRIVER_CREATE_ATTEMPTS = Integer.parseInt(Props.get("webdriver.create.attempts", "3"));
    private static final String WEBDRIVER_PATH = Props.get("webdriver.drivers.path");
    private static final String WEBDRIVER_URL = Props.get("webdriver.url");
    private static final String WEBDRIVER_STARTING_URL = Props.get("webdriver.starting.url");
    private static final String WEBDRIVER_PROXY = Props.get("webdriver.proxy");
    private static final boolean WEBDRIVER_BROWSER_IE_KILL_ON_DISPOSE = Boolean.parseBoolean(Props.get("webdriver.browser.ie.killOnDispose", "false"));
    private static final String WEBDRIVER_BROWSER_NAME = Props.get("webdriver.browser.name").toLowerCase().equals("ie")
            // Normalize it for ie shorten name (ie)
            ? BrowserType.IE : Props.get("webdriver.browser.name").toLowerCase();
    private static final boolean IS_IE = WEBDRIVER_BROWSER_NAME.equals(BrowserType.IE.toLowerCase())
            || WEBDRIVER_BROWSER_NAME.equals(BrowserType.IE_HTA.toLowerCase())
            || WEBDRIVER_BROWSER_NAME.equals(BrowserType.IEXPLORE.toLowerCase());

    private static final String VIDEO_ENABLED = Props.get("video.enabled", "false");

    public static WebDriver getDriver() {
        if (Environment.WEB != PageFactory.getEnvironment()) {
            throw new FactoryRuntimeException("Failed to get web driver while environment is not web");
        }

        if (null == webDriver) {
            if (Boolean.valueOf(VIDEO_ENABLED)) {
                VideoRecorder.getInstance().startRecording();
            }

            for (int i = 1; i <= WEBDRIVER_CREATE_ATTEMPTS; i++) {
                LOG.info("Attempt #" + i + " to start web driver");
                try {
                    createDriver();
                    break;
                } catch (UnreachableBrowserException e) {
                    LOG.warn("Failed to create web driver. Attempt number {}", i, e);
                    if (null != webDriver) {
                        // Don't dispose when driver is already null, cuz it causes new driver creation at Init.getWebDriver()
                        dispose();
                    }
                } catch (UnsupportedBrowserException | MalformedURLException e) {
                    LOG.error("Failed to create web driver", e);
                    break;
                }
            }
        }
        return webDriver;
    }

    private static void createDriver() throws UnsupportedBrowserException, MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilitiesParser().parse();

        //Local proxy available on local webdriver instances only
        if (!WEBDRIVER_PROXY.isEmpty()) {
            setProxy(new BrowserMobProxyServer());
            proxy.start(0);
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        }
        capabilities.setBrowserName(WEBDRIVER_BROWSER_NAME);

        if (WEBDRIVER_BROWSER_NAME.equals(BrowserType.FIREFOX.toLowerCase())) {
            if (WEBDRIVER_URL.isEmpty()) {
                setWebDriver(new FirefoxDriver(capabilities));
            }
        } else if (WEBDRIVER_BROWSER_NAME.equals(BrowserType.SAFARI.toLowerCase())) {
            if (WEBDRIVER_URL.isEmpty()) {
                setWebDriver(new SafariDriver(capabilities));
            }
        } else if (WEBDRIVER_BROWSER_NAME.equals(BrowserType.CHROME.toLowerCase())) {
            if (!WEBDRIVER_PATH.isEmpty()) {
                System.setProperty("webdriver.chrome.driver", new File(WEBDRIVER_PATH).getAbsolutePath());
            } else {
                LOG.warn("The value of property 'webdriver.drivers.path is not specified."
                        + " Try to get {} driver from system PATH'", WEBDRIVER_BROWSER_NAME);
            }
            if (WEBDRIVER_URL.isEmpty()) {
                setWebDriver(new ChromeDriver(capabilities));
            }
        } else if (WEBDRIVER_BROWSER_NAME.equals(BrowserType.IE.toLowerCase())
                || WEBDRIVER_BROWSER_NAME.equals(BrowserType.IE_HTA.toLowerCase())
                || WEBDRIVER_BROWSER_NAME.equals(BrowserType.IEXPLORE.toLowerCase())) {
            if (!WEBDRIVER_PATH.isEmpty()) {
                System.setProperty("webdriver.ie.driver", new File(WEBDRIVER_PATH).getAbsolutePath());
            } else {
                LOG.warn("The value of property 'webdriver.drivers.path is not specified."
                        + " Try to get {} driver from system PATH'", WEBDRIVER_BROWSER_NAME);
            }
            if (WEBDRIVER_URL.isEmpty()) {
                setWebDriver(new InternetExplorerDriver(capabilities));
            }
        } else {
            throw new UnsupportedBrowserException("'" + WEBDRIVER_BROWSER_NAME + "' is not supported yet");
        }
        if (!WEBDRIVER_URL.isEmpty()) {
            URL remoteUrl = new URL(WEBDRIVER_URL);
            setWebDriver(new RemoteWebDriver(remoteUrl, capabilities));
        }
        webDriver.manage().timeouts().pageLoadTimeout(getTimeOutInSeconds(), TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
        webDriver.get(WEBDRIVER_STARTING_URL);
    }

    public static void dispose() {
        try {
            LOG.info("Checking any alert opened");
            WebDriverWait alertAwaiter = new WebDriverWait(webDriver, 2);
            alertAwaiter.until(ExpectedConditions.alertIsPresent());
            Alert alert = webDriver.switchTo().alert();
            LOG.info("Got an alert: " + alert.getText() + "\n Closing it.");
            alert.dismiss();
        } catch (WebDriverException e) {
            LOG.debug("No alert opened. Closing webdriver.", e);
        }

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
            LOG.warn("Failed to kill all of the iexplore windows", e);
        }

        if (IS_IE && WEBDRIVER_BROWSER_IE_KILL_ON_DISPOSE) {
            killIE();
        }

        try {
            webDriver.quit();
        } finally {
            setWebDriver(null);
        }
    }

    private static void killIE() {
        try {
            Runtime.getRuntime().exec("taskkill /f /im iexplore.exe").waitFor();
        } catch (IOException | InterruptedException e) {
            LOG.warn("Failed to wait for browser processes finish", e);
        }
    }

    /**
     * @param aWebDriver the webDriver to set
     */
    public static void setWebDriver(WebDriver aWebDriver) {
        webDriver = aWebDriver;
    }

    /**
     * @param aProxy the proxy to set
     */
    public static void setProxy(BrowserMobProxy aProxy) {
        proxy = aProxy;
    }

    /**
     * @return the WEBDRIVER_BROWSER_NAME
     */
    public static String getBrowserName() {
        return WEBDRIVER_BROWSER_NAME;
    }
}
