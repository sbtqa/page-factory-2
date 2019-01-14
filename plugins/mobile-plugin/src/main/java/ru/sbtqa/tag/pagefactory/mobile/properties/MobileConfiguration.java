package ru.sbtqa.tag.pagefactory.mobile.properties;

import ru.sbtqa.tag.pagefactory.properties.Configuration;

public interface MobileConfiguration extends Configuration {

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

    @Key("appium.strategies.reset")
    @DefaultValue("")
    String getAppiumResetStrategy();

    static MobileConfiguration create() {
        return Configuration.init(MobileConfiguration.class);
    }

}
