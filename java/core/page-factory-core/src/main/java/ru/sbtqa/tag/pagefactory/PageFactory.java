package ru.sbtqa.tag.pagefactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.drivers.TagMobileDriver;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.pagefactory.exceptions.FactoryRuntimeException;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.pagefactory.support.properties.Configuration;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;

public class PageFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PageFactory.class);

    private static final Map<Class<? extends Page>, Map<Field, String>> PAGES_REPOSITORY = new HashMap<>();

    private static Actions actions;
    private static PageManager pageManager;

    private static final Configuration PROPERTIES = Properties.getProperties();
    private static boolean aspectsEnabled = PROPERTIES.isAspectEnabled();
    private static boolean sharingIsActive = false;

    private static final String ENVIRONMENT_WEB = "web";
    private static final String ENVIRONMENT_MOBILE = "mobile";

    public static void dispose() {
        pageManager = null;
        switch (getEnvironment()) {
            case WEB:
                TagWebDriver.dispose();
                break;
            case MOBILE:
                TagMobileDriver.dispose();
                break;
            default:
                throw new FactoryRuntimeException("Failed to dispose");
        }
    }
    
    public static Environment getEnvironment() {
        String environment = PROPERTIES.getEnvironment();
        switch (environment) {
            case ENVIRONMENT_WEB:
                return Environment.WEB;
            case ENVIRONMENT_MOBILE:
                return Environment.MOBILE;
            default:
                throw new FactoryRuntimeException("Environment '" + environment + "' is not supported");
        }
    }
    
    public static void initElements(WebDriver driver, Object page) {
        org.openqa.selenium.support.PageFactory.initElements(driver, page);
    }

    public static void initElements(FieldDecorator decorator, Object page) {
        org.openqa.selenium.support.PageFactory.initElements(decorator, page);
    }

    public static PageManager getInstance() {
        if (null == pageManager) {
            pageManager = new PageManager(getPagesPackage());
        }
        return pageManager;
    }

    public static String getPagesPackage() {
        return PROPERTIES.getPagesPackage();
    }

    public static Actions getActions() {
        if (null == actions) {
            actions = new Actions(PageContext.getCurrentPage().getDriver());
        }
        return actions;
    }
    
    public static int getTimeOutInSeconds() {
        return getTimeOut() / 1000;
    }

    public static int getTimeOut() {
        return PROPERTIES.getTimeout();
    }

    public static Map<Class<? extends Page>, Map<Field, String>> getPageRepository() {
        return PAGES_REPOSITORY;
    }
        
    public static boolean isAspectsEnabled() {
        return aspectsEnabled;
    }

    public static void setAspectsEnabled(boolean aAspectsEnabled) {
        aspectsEnabled = aAspectsEnabled;
    }

    public static boolean isVideoRecorderEnabled() {
        return PROPERTIES.isVideoEnabled();
    }
    
    public static boolean isSharingActive() {
        return sharingIsActive;
    }

    public static void setSharingIsActive(boolean aSharingProcessing) {
        sharingIsActive = aSharingProcessing;
    }

    public static boolean isDriverInitialized(){
        return TagWebDriver.isDriverInitialized() || TagMobileDriver.isDriverInitialized();
    }
}
