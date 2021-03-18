package ru.sbtqa.tag.pagefactory.mobile.properties;

import ru.sbtqa.tag.pagefactory.mobile.utils.PlatformName;
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
    @DefaultValue("portrait")
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
    @DefaultValue("IOS")
    PlatformName getAppiumPlatformName();

    @Key("appium.fullReset")
    @DefaultValue("")
    String getAppiumFullReset();

    @Key("appium.noReset")
    @DefaultValue("")
    String getAppiumNoReset();

    @Key("appium.permissions")
    String getAppiumPermissions();

    @Key("appium.automation.name")
    @DefaultValue("")
    String getAppiumAutomationName();

    @Key("appium.permissions.autogrant")
    @DefaultValue("")
    String getAppiumAutoGrantPermissions();

    @Key("appium.keyboard.unicode")
    @DefaultValue("")
    String getAppiumKeyboardUnicode();

    @Key("appium.keyboard.reset")
    @DefaultValue("")
    String getAppiumKeyboardReset();

    @Key("appium.udid")
    @DefaultValue("")
    String getAppiumUdid();

    @Key("appium.bundleid")
    @DefaultValue("")
    String getAppiumBundleId();

    @Key("appium.alerts.autoaccept")
    @DefaultValue("")
    String getAppiumAlertsAutoAccept();

    @Key("appium.timeout")
    @DefaultValue("60")
    String getNewCommandTimeout();

    @Key("appium.video.enabled")
    @DefaultValue("false")
    boolean getAppiumVideoEnabled();

    @Key("appium.video.folder")
    @DefaultValue("target")
    String getAppiumVideoFolder();

    @Key("appium.video.name")
    @DefaultValue("mobiletest")
    String getAppiumVideoName();

    @Key("appium.video.extension")
    @DefaultValue("mp4")
    String getAppiumVideoExtension();

    @Key("appium.video.type")
    @DefaultValue("h264")
    String getAppiumVideoType();

    @Key("appium.video.scale")
    @DefaultValue("1920:1080")
    String getAppiumVideoScale();

    @Key("appium.video.timeLimit")
    @DefaultValue("180")
    long getAppiumTimeLimit();

    @Key("appium.video.quality")
    @DefaultValue("MEDIUM")
    String getAppiumVideoQuality();

    @Key("appium.video.fps")
    @DefaultValue("10")
    int getAppiumVideoFps();

    @Key("appium.video.bitrate")
    @DefaultValue("200000000")
    int getAppiumVideoBitRate();

    @Key("appium.video.videoSize")
    @DefaultValue("1280x720")
    String getAppiumVideoSize();

    @Key("appium.video.bugReport")
    @DefaultValue("false")
    boolean getAppiumVideoBugReport();

    @Key("appium.xcodeOrgId")
    @DefaultValue("")
    String getAppiumXcodeOrgId();

    @Key("appium.xcodeSigningId")
    @DefaultValue("")
    String getAppiumXcodeSigningId();

    @Key("appium.useJSONSource")
    @DefaultValue("false")
    String getAppiumUseJSONSource();

    @Key("appium.simpleIsVisibleCheck")
    @DefaultValue("false")
    String getAppiumSimpleIsVisibleCheck();

    @Key("appium.showIOSLog")
    @DefaultValue("false")
    String getAppiumShowIOSLog();

    @Key("appium.useNewWDA")
    @DefaultValue("false")
    String getAppiumUseNewWDA();

    @Key("appium.derivedDataPath")
    @DefaultValue("")
    String getAppiumDerivedDataPath();

    @Key("appium.usePrebuiltWDA")
    @DefaultValue("false")
    String getAppiumUsePrebuiltWDA();

    static MobileConfiguration create() {
        return Configuration.init(MobileConfiguration.class);
    }
}
