package ru.sbtqa.tag.pagefactory;

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
import ru.sbtqa.tag.pagefactory.support.properties.Configuration;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;

public class MobileDriverService implements DriverService {

    private final Logger LOG = LoggerFactory.getLogger(MobileDriverService.class);

    private static final Configuration PROPERTIES = Properties.getProperties();
    private AppiumDriver<AndroidElement> mobileDriver;
    private String deviceUdId;

    @Override
    public void mountDriver() {
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

    @Override
    public void demountDriver() {
        if (mobileDriver == null) {
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
        return mobileDriver;
    }

    public boolean isAppiumFillAdb() {
        return PROPERTIES.isAppiumFillAdb();
    }

    public boolean getAppiumClickAdb() {
        return PROPERTIES.isAppiumClickAdb();
    }

    public String getDeviceUDID() {
        return deviceUdId;
    }
    
    public void setMobileDriver(AppiumDriver<AndroidElement> aMobileDriver) {
        mobileDriver = aMobileDriver;
    }
    
    public boolean isDriverInitialized(){
        return mobileDriver != null;
    }
}
