package ru.sbtqa.tag.pagefactory.drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.pagefactory.support.properties.Configuration;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class TagMobileDriver {

    private static final Logger LOG = LoggerFactory.getLogger(TagMobileDriver.class);

    private static final Configuration PROPERTIES = Properties.getProperties();
    private static AppiumDriver<AndroidElement> mobileDriver;
    private static String deviceUdId;

    public static AppiumDriver<AndroidElement> getDriver() {
        if (Environment.MOBILE != PageFactory.getEnvironment()) {
            throw new FactoryRuntimeException("Failed to get mobile driver while environment is not mobile");
        }

        if (null == mobileDriver) {
            if (PROPERTIES.isVideoEnabled()) {
                VideoRecorder.getInstance().startRecording();
            }

            createDriver();
        }
        return mobileDriver;
    }

    private static void createDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", PROPERTIES.getAppiumDeviceName());
        capabilities.setCapability("platformVersion", PROPERTIES.getAppiumDevicePlatform());
        capabilities.setCapability("appPackage", PROPERTIES.getAppiumAppPackage());
        capabilities.setCapability("appActivity", PROPERTIES.getAppiumAppActivity());
        capabilities.setCapability("autoGrantPermissions", "true");
        capabilities.setCapability("unicodeKeyboard", "true");
        capabilities.setCapability("resetKeyboard", "true");
        LOG.info("Capabilities are {}", capabilities);

        URL url;
        try {
            url = new URL(PROPERTIES.getAppiumUrl());
        } catch (MalformedURLException e) {
            throw new FactoryRuntimeException("Could not parse appium url. Check 'appium.url' property", e);
        }

        PageFactory.setAspectsEnabled(false);
        LOG.debug("Aspect disabled");
        mobileDriver = new AndroidDriver<>(url, capabilities);
        LOG.info("Mobile driver created {}", mobileDriver);
        deviceUdId = (String) mobileDriver.getSessionDetails().get("deviceUDID");
    }

    public static void dispose() {
        if (mobileDriver != null) {
            mobileDriver.quit();
        }
    }

    public static boolean isAppiumFillAdb() {
        return PROPERTIES.isAppiumFillAdb();
    }

    public static boolean getAppiumClickAdb() {
        return PROPERTIES.isAppiumClickAdb();
    }

    public static String getDeviceUDID() {
        return deviceUdId;
    }
    
    public static void setMobileDriver(AppiumDriver<AndroidElement> aMobileDriver) {
        mobileDriver = aMobileDriver;
    }
    
    public static boolean isDriverInitialized(){
        return mobileDriver != null;
    }
}
