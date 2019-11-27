package ru.sbtqa.tag.pagefactory.mobile.drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.allure.Type;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.mobile.properties.MobileConfiguration;
import ru.sbtqa.tag.pagefactory.mobile.utils.AppiumVideoRecorder;

import java.net.MalformedURLException;
import java.net.URL;

import static ru.sbtqa.tag.pagefactory.mobile.utils.PlatformName.IOS;

public class MobileDriverService implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(MobileDriverService.class);
    private static final MobileConfiguration PROPERTIES = MobileConfiguration.create();

    private AppiumDriver mobileDriver;
    private AppiumVideoRecorder appiumVideoRecorder;

    @Override
    public void mountDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PROPERTIES.getAppiumPlatformName());
        capabilities.setCapability(MobileCapabilityType.APP, PROPERTIES.getAppiumApp());
        capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, PROPERTIES.getAppiumVersion());
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, PROPERTIES.getAppiumDeviceName());
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, PROPERTIES.getAppiumPlatformVersion());
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, PROPERTIES.getAppiumBrowserName());
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, PROPERTIES.getAppiumAutomationName());
        capabilities.setCapability(MobileCapabilityType.UDID, PROPERTIES.getAppiumUdid());
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, PROPERTIES.getNewCommandTimeout());
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, PROPERTIES.getAppiumFullReset());
        capabilities.setCapability(MobileCapabilityType.NO_RESET, PROPERTIES.getAppiumNoReset());

        capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, PROPERTIES.getAppiumAlertsAutoAccept());

        capabilities.setCapability("bundleId", PROPERTIES.getAppiumBundleId());
        capabilities.setCapability("appPackage", PROPERTIES.getAppiumAppPackage());
        capabilities.setCapability("appActivity", PROPERTIES.getAppiumAppActivity());
        capabilities.setCapability("permissions", PROPERTIES.getAppiumPermissions());
        capabilities.setCapability("autoGrantPermissions", PROPERTIES.getAppiumAutoGrantPermissions());
        capabilities.setCapability("unicodeKeyboard", PROPERTIES.getAppiumKeyboardUnicode());
        capabilities.setCapability("resetKeyboard", PROPERTIES.getAppiumKeyboardReset());
        capabilities.setCapability("connectHardwareKeyboard", false);
        capabilities.setCapability("xcodeOrgId", PROPERTIES.getAppiumXcodeOrgId());
        capabilities.setCapability("xcodeSigningId", PROPERTIES.getAppiumXcodeSigningId());

        LOG.info(String.valueOf(capabilities));

        URL url;
        try {
            url = new URL(PROPERTIES.getAppiumUrl());
        } catch (MalformedURLException e) {
            throw new FactoryRuntimeException("Could not parse appium url. Check 'appium.url' property", e);
        }

        mobileDriver = PROPERTIES.getAppiumPlatformName() == IOS ? new IOSDriver(url, capabilities) : new AndroidDriver(url, capabilities);

        if (PROPERTIES.getAppiumVideoEnabled()) {
            appiumVideoRecorder = new AppiumVideoRecorder(Environment.getScenario());
            appiumVideoRecorder.startRecord();
        }
    }

    @Override
    public void demountDriver() {
        if (isDriverEmpty()) {
            return;
        }

        if (PROPERTIES.getAppiumVideoEnabled() && getAppiumVideoRecorder().isRecording()) {
            byte[] video = getAppiumVideoRecorder().stopRecord();
            ParamsHelper.addAttachmentToRender(video, getAppiumVideoRecorder().getVideoFileName(), Type.VIDEO);
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
