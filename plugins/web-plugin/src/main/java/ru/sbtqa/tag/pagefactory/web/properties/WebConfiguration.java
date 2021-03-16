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


    @Key("aspects.highlight.enabled")
    @DefaultValue("false")
    boolean isHighlightEnabled();

    @Key("aspects.click.actions.enabled")
    @DefaultValue("false")
    boolean isClickViaSeleniumActionsEnabled();

    @Key("aspects.scroll.to.element.enabled")
    @DefaultValue("false")
    boolean isScrollToElementEnabled();

    static WebConfiguration create() {
        return Configuration.init(WebConfiguration.class);
    }

}
