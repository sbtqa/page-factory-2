package ru.sbtqa.tag.pagefactory.mobile.drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.drivers.DriverService;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.mobile.properties.MobileConfiguration;

public class MobileDriverService implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(MobileDriverService.class);
    private static final MobileConfiguration PROPERTIES = MobileConfiguration.create();

    private AppiumDriver<AndroidElement> mobileDriver;
    private String deviceUdId;

    @Override
    public void mountDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", PROPERTIES.getAppiumApp());
        capabilities.setCapability("appPackage", PROPERTIES.getAppiumAppPackage());
        capabilities.setCapability("appActivity", PROPERTIES.getAppiumAppActivity());

        capabilities.setCapability("appiumVersion", PROPERTIES.getAppiumVersion());

        capabilities.setCapability("deviceName", PROPERTIES.getAppiumDeviceName());
        capabilities.setCapability("platformName",PROPERTIES.getAppiumPlatformName());
        capabilities.setCapability("platformVersion", PROPERTIES.getAppiumPlatformVersion());
        capabilities.setCapability("deviceOrientation", PROPERTIES.getAppiumDeviceOrientation());
        capabilities.setCapability("browserName", PROPERTIES.getAppiumBrowserName());

        capabilities.setCapability("autoGrantPermissions", "true");
        capabilities.setCapability("unicodeKeyboard", "true");
        capabilities.setCapability("resetKeyboard", "true");
        if (PROPERTIES.getAppiumResetStrategy().equalsIgnoreCase("noreset")) {
            capabilities.setCapability("noReset","true");
        } else if (PROPERTIES.getAppiumResetStrategy().equalsIgnoreCase("fullreset")) {
            capabilities.setCapability("fullReset","true");
        }

        LOG.info("Capabilities are {}", capabilities);

        URL url;
        try {
            url = new URL(PROPERTIES.getAppiumUrl());
        } catch (MalformedURLException e) {
            throw new FactoryRuntimeException("Could not parse appium url. Check 'appium.url' property", e);
        }

        mobileDriver = new AndroidDriver<>(url, capabilities);
        LOG.info("Mobile driver created {}", mobileDriver);
        deviceUdId = (String) mobileDriver.getSessionDetails().get("deviceUDID");
    }

    @Override
    public void demountDriver() {
        if (isDriverEmpty()) {
            return;
        }

        try {
            mobileDriver.quit();
        } finally {
            setMobileDriver(null);
        }
    }

    @Override
    public AppiumDriver<AndroidElement> getDriver() {
        if (isDriverEmpty()) {
            mountDriver();
        }

        return mobileDriver;
    }

    public String getDeviceUDID() {
        return deviceUdId;
    }
    
    public void setMobileDriver(AppiumDriver<AndroidElement> aMobileDriver) {
        mobileDriver = aMobileDriver;
    }

    @Override
    public boolean isDriverEmpty() {
        return mobileDriver == null;
    }
}
