package ru.sbtqa.tag.pagefactory;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

public class PageFactory {

    private static PageManager pageManager;

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);
    private static boolean aspectsEnabled = PROPERTIES.isAspectEnabled();
    private static boolean sharingIsActive = false;
    
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

    public static int getTimeOutInSeconds() {
        return getTimeOut() / 1000;
    }

    public static int getTimeOut() {
        return PROPERTIES.getTimeout();
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

//    public static boolean isDriverInitialized(){
//        return TagWebDriver.isDriverInitialized() || TagMobileDriver.isDriverInitialized();
//    }
}
