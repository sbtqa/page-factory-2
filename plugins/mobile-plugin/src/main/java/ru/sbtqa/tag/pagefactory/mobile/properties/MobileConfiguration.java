package ru.sbtqa.tag.pagefactory.mobile.properties;

import ru.sbtqa.tag.pagefactory.properties.Configuration;

public interface MobileConfiguration extends Configuration {

    @Key("appium.url")
    @DefaultValue("")
    String getAppiumUrl();

    @Key("appium.version")
    @DefaultValue("")
    String getAppiumVersion();

    @Key("appium.app")
    @DefaultValue("")
    String getAppiumApp();

    @Key("appium.app.package")
    @DefaultValue("")
    String getAppiumAppPackage();

    @Key("appium.app.activity")
    @DefaultValue("")
    String getAppiumAppActivity();

    @Key("appium.device.orientation")
    @DefaultValue("")
    String getAppiumDeviceOrientation();

    @Key("appium.device.name")
    @DefaultValue("")
    String getAppiumDeviceName();

    @Key("appium.device.platform.version")
    @DefaultValue("")
    String getAppiumPlatformVersion();

    @Key("appium.browser.name")
    @DefaultValue("")
    String getAppiumBrowserName();

    @Key("appium.device.platform.name")
    @DefaultValue("")
    String getAppiumPlatformName();

    @Key("appium.strategies.reset")
    @DefaultValue("")
    String getAppiumResetStrategy();

    static MobileConfiguration create() {
        return Configuration.init(MobileConfiguration.class);
    }
}
