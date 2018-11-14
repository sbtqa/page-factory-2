package ru.sbtqa.tag.pagefactory.web.properties;

import org.aeonbits.owner.Config.Sources;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

@Sources("classpath:config/application.properties")
public interface WebConfiguration extends Configuration {

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

    @Key("selenoid.sessionTimeout")
    @DefaultValue("")
    String getSelenoidSessionTimeout();


    @Key("aspects.highlight.enabled")
    @DefaultValue("false")
    boolean isHighlightEnabled();

    @Key("aspects.click.ru.sbtqa.tag.pagefactory.mobile.actions.enabled")
    @DefaultValue("false")
    boolean isClickViaSeleniumActionsEnabled();

    @Key("aspects.scroll.to.element.enabled")
    @DefaultValue("false")
    boolean isScrollToElementEnabled();
}
