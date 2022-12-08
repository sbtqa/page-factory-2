package ru.sbtqa.tag.pagefactory.mobile.drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.Setting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.allure.Type;
import ru.sbtqa.tag.pagefactory.capabilities.SelenoidCapabilitiesParser;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.mobile.properties.MobileConfiguration;
import ru.sbtqa.tag.pagefactory.mobile.utils.AppiumVideoRecorder;

import java.net.MalformedURLException;
import java.net.URL;

public class MobileDriverService implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(MobileDriverService.class);
    private static final MobileConfiguration PROPERTIES = MobileConfiguration.create();

    private AppiumDriver mobileDriver;
    private AppiumVideoRecorder appiumVideoRecorder;

    @Override
    public void mountDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        setCapability(capabilities, MobileCapabilityType.PLATFORM_NAME, PROPERTIES.getAppiumPlatformName());
        setCapability(capabilities, MobileCapabilityType.APP, PROPERTIES.getAppiumApp());
        setCapability(capabilities, MobileCapabilityType.DEVICE_NAME, PROPERTIES.getAppiumDeviceName());
        setCapability(capabilities, MobileCapabilityType.PLATFORM_VERSION, PROPERTIES.getAppiumPlatformVersion());
        setCapability(capabilities, MobileCapabilityType.BROWSER_NAME, PROPERTIES.getAppiumBrowserName());
        setCapability(capabilities, MobileCapabilityType.AUTOMATION_NAME, PROPERTIES.getAppiumAutomationName());
        setCapability(capabilities, MobileCapabilityType.UDID, PROPERTIES.getAppiumUdid());
        setCapability(capabilities, MobileCapabilityType.NEW_COMMAND_TIMEOUT, PROPERTIES.getNewCommandTimeout());
        setCapability(capabilities, MobileCapabilityType.FULL_RESET, PROPERTIES.getAppiumFullReset());
        setCapability(capabilities, MobileCapabilityType.NO_RESET, PROPERTIES.getAppiumNoReset());

        setCapability(capabilities, IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, PROPERTIES.getAppiumAlertsAutoAccept());

        setCapability(capabilities, "bundleId", PROPERTIES.getAppiumBundleId());
        setCapability(capabilities, "appPackage", PROPERTIES.getAppiumAppPackage());
        setCapability(capabilities, "appActivity", PROPERTIES.getAppiumAppActivity());
        setCapability(capabilities, "permissions", PROPERTIES.getAppiumPermissions());
        setCapability(capabilities, "autoGrantPermissions", PROPERTIES.getAppiumAutoGrantPermissions());
        setCapability(capabilities, "unicodeKeyboard", PROPERTIES.getAppiumKeyboardUnicode());
        setCapability(capabilities, "resetKeyboard", PROPERTIES.getAppiumKeyboardReset());
        capabilities.setCapability("connectHardwareKeyboard", false);
        setCapability(capabilities, "xcodeOrgId", PROPERTIES.getAppiumXcodeOrgId());
        setCapability(capabilities, "xcodeSigningId", PROPERTIES.getAppiumXcodeSigningId());
        setCapability(capabilities, "showIOSLog", PROPERTIES.getAppiumShowIOSLog());
        capabilities.setCapability("appium:disableWindowAnimation", PROPERTIES.getDisableWindowAnimation());
        setCapability(capabilities, "appium:useJSONSource", PROPERTIES.getAppiumUseJSONSource());
        setCapability(capabilities, "appium:simpleIsVisibleCheck", PROPERTIES.getAppiumSimpleIsVisibleCheck());
        setCapability(capabilities, "appium:useNewWDA", PROPERTIES.getAppiumUseNewWDA());
        setCapability(capabilities, "appium:usePrebuiltWDA", PROPERTIES.getAppiumUsePrebuiltWDA());
        setCapability(capabilities, "appium:derivedDataPath", PROPERTIES.getAppiumDerivedDataPath());

        if (PROPERTIES.getAppiumPlatformName().equals("ANDROID")) {
            capabilities.merge(new SelenoidCapabilitiesParser().parse());
        }

        LOG.info(String.valueOf(capabilities));

        URL url;
        try {
            url = new URL(PROPERTIES.getAppiumUrl());
        } catch (MalformedURLException e) {
            throw new FactoryRuntimeException("Could not parse appium url. Check 'appium.url' property", e);
        }

        mobileDriver = PROPERTIES.getAppiumPlatformName().equals("IOS") ? new IOSDriver(url, capabilities) : new AndroidDriver(url, capabilities);

        if (PROPERTIES.getAppiumPlatformName().equals("ANDROID")) {
            mobileDriver.setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, PROPERTIES.getWaitForIdleTimeout());
        }

        if (PROPERTIES.getAppiumVideoEnabled()) {
            appiumVideoRecorder = new AppiumVideoRecorder(Environment.getScenario());
            appiumVideoRecorder.startRecord();
        }
    }

    private void setCapability(DesiredCapabilities capabilities, String name, String value) {
        if (value != null && !value.equals("")) {
            capabilities.setCapability(name, value);
        }
    }

    @Override
    public void demountDriver() {
        if (isDriverEmpty()) {
            return;
        }

        if (PROPERTIES.getAppiumVideoEnabled() && appiumVideoRecorder.isRecording()) {
            byte[] video = appiumVideoRecorder.stopRecord();
            ParamsHelper.addAttachmentToRender(video, appiumVideoRecorder.getVideoFileName(), Type.VIDEO);
        }

        try {
            mobileDriver.quit();
        } finally {
            setMobileDriver(null);
        }
    }

    @Override
    public AppiumDriver getDriver() {
        if (isDriverEmpty()) {
            mountDriver();
        }

        return mobileDriver;
    }

    public void setMobileDriver(AppiumDriver aMobileDriver) {
        mobileDriver = aMobileDriver;
    }

    @Override
    public boolean isDriverEmpty() {
        return mobileDriver == null;
    }

    public AppiumVideoRecorder getAppiumVideoRecorder() {
        return appiumVideoRecorder;
    }
}
