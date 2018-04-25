package ru.sbtqa.tag.pagefactory.support.properties;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/application.properties")
public interface Configuration extends Config {

    @Key("driver.environment")
    String getEnvironment();

    
    @Key("page.load.timeout")
    @DefaultValue("20000")
    int getTimeout();

    @Key("page.package")
    String getPagesPackage();

    @Key("page.aspect.enabled")
    @DefaultValue("true")
    boolean isAspectEnabled();


    @Key("webdriver.browser.name")
    String getBrowserName();

    @Key("webdriver.browser.version")
    @DefaultValue("")
    String getBrowserVersion();

    @Key("webdriver.browser.path")
    @DefaultValue("")
    String getBrowserPath();

    @Key("webdriver.browser.size")
    @DefaultValue("")
    String getBrowserSize();

    @Key("webdriver.browser.ie.killOnDispose")
    @DefaultValue("false")
    boolean isIEKillOnDispose();
    
    @Key("webdriver.create.attempts")
    @DefaultValue("3")
    int getWebDriverCreateAttempts();

    @Key("webdriver.starting.url")
    @DefaultValue("about:blank")
    String getStartingUrl();

    @Key("webdriver.drivers.path")
    @DefaultValue("")
    String getDriversPath();

    @Key("webdriver.version")
    @DefaultValue("")
    String getWebDriverVersion();

    @Key("webdriver.nexus.url")
    @DefaultValue("")
    String getNexusUrl();

    @Key("webdriver.os.arch")
    @DefaultValue("")
    String getOsArchitecture();

    @Key("webdriver.url")
    @DefaultValue("")
    String getWebDriverUrl();

    @Key("webdriver.proxy")
    @DefaultValue("")
    String getProxy();

    @Key("webdriver.shared")
    @DefaultValue("false")
    boolean isWebDriverShared();
    
    
    @Key("video.highlight.enabled")
    @DefaultValue("false")
    boolean isVideoHighlightEnabled();
    
    @Key("video.enabled")
    @DefaultValue("false")
    boolean isVideoEnabled();

    @Key("tasks.to.kill")
    @DefaultValue("")
    String getTasksToKill();

    @Key("screenshot.strategy")
    @DefaultValue("raw")
    String getScreenshotStrategy();

    
    @Key("appium.url")
    @DefaultValue("")
    String getAppiumUrl();
    
    @Key("appium.device.name")
    @DefaultValue("")
    String getAppiumDeviceName();
    
    @Key("appium.device.platform")
    @DefaultValue("")
    String getAppiumDevicePlatform();
    
    @Key("appium.app.package")
    @DefaultValue("")
    String getAppiumAppPackage();
    
    @Key("appium.app.activity")
    @DefaultValue("")
    String getAppiumAppActivity();
    
    @Key("appium.fill.adb")
    @DefaultValue("false")
    boolean isAppiumFillAdb();
    
    @Key("appium.click.adb")
    @DefaultValue("false")
    boolean isAppiumClickAdb();
    
    
    @Key("selenoid.browserVersion")
    @DefaultValue("")
    String getSelenoidBrowserVersion();

    @Key("selenoid.enableVNC")
    @DefaultValue("")
    String getSelenoidEnableVNC();

    @Key("selenoid.screenResolution")
    @DefaultValue("")
    String getSelenoidScreenResolution();

    @Key("selenoid.enableVideo")
    @DefaultValue("false")
    boolean getSelenoidEnableVideo();

    @Key("selenoid.video.name")
    @DefaultValue("")
    String getSelenoidVideoName();

    @Key("selenoid.video.screenSize")
    @DefaultValue("")
    String getSelenoidVideoScreenSize();

    @Key("selenoid.video.frameRate")
    @DefaultValue("")
    String getSelenoidVideoFrameRate();

    @Key("selenoid.nameOfTests")
    @DefaultValue("")
    String getSelenoidNameOfTests();

    @Key("selenoid.timeZone")
    @DefaultValue("")
    String getSelenoidTimeZone();

    @Key("selenoid.hostEntries")
    @DefaultValue("")
    String getSelenoidHostEntries();

    @Key("selenoid.applicationContainers")
    @DefaultValue("")
    String getSelenoidApplicationContainers();

    @Key("selenoid.containerLables")
    @DefaultValue("")
    String getSelenoidContainerLables();
}
