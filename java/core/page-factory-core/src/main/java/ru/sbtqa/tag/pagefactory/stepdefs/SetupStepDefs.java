package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebElement;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.pagefactory.Page;
import ru.sbtqa.tag.pagefactory.PageContext;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.annotations.ElementTitle;
import ru.sbtqa.tag.pagefactory.drivers.TagWebDriver;
import ru.sbtqa.tag.pagefactory.support.Environment;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class SetupStepDefs {

    private static final Logger LOG = LoggerFactory.getLogger(SetupStepDefs.class);

    private static final String DEFAULT_LOG_PROPERTIES_PATH = "src/test/resources/config/log4j.properties";

    @Before
    public void setUp() {
        connectToLogProperties();
        stopTasksToKill();

        PageFactory.getDriver();
        PageFactory.getInstance();
        PageContext.resetContext();

        cachePages();

        startVideo();
    }

    private void connectToLogProperties() {
        if (new File(DEFAULT_LOG_PROPERTIES_PATH).exists()) {
            PropertyConfigurator.configure(DEFAULT_LOG_PROPERTIES_PATH);
            LOG.info("Log4j properties were picked up on the path {}", DEFAULT_LOG_PROPERTIES_PATH);
        } else {
            LOG.warn("There is no log4j.properties on the path {}", DEFAULT_LOG_PROPERTIES_PATH);
        }
    }

    private void stopTasksToKill() {
        String tasks = Properties.getProperties().getTasksToKill();
        if (!PageFactory.isSharingActive() && !tasks.isEmpty()) {
            for (String task : tasks.split(",")) {
                stopTask(task);
            }
        }
    }

    private void stopTask(String task) {
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                Runtime.getRuntime().exec("taskkill /IM " + task.trim() + " /F");
            } else {
                Runtime.getRuntime().exec("killall " + task.trim());
            }
        } catch (IOException e) {
            LOG.debug("Failed to kill " + task, e);
        }
    }

    private void startVideo() {
        if (PageFactory.isVideoRecorderEnabled()) {
            VideoRecorder.getInstance().startRecording();
        }
    }

    private void cachePages() {
        Set<Class<?>> allClasses = new HashSet();
        allClasses.addAll(getAllClasses());
        
        for (Class<?> page : allClasses) {
            List<Field> fields = FieldUtilsExt.getDeclaredFieldsWithInheritance(page);
            Map<Field, String> fieldsMap = new HashMap<>();
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                if (fieldType.equals(WebElement.class)) {

                    ElementTitle titleAnnotation = field.getAnnotation(ElementTitle.class);
                    if (titleAnnotation != null) {
                        fieldsMap.put(field, titleAnnotation.value());
                    } else {
                        fieldsMap.put(field, field.getName());
                    }
                }
            }

            PageFactory.getPageRepository().put((Class<? extends Page>) page, fieldsMap);
        }
    }
    
    private Set<Class<?>> getAllClasses() {
        Set<Class<?>> allClasses = new HashSet();
        
        Reflections reflections = new Reflections(PageFactory.getPagesPackage());
        Collection<String> allClassesString = reflections.getStore().get("SubTypesScanner").values();
        
        for (String clazz : allClassesString) {
            try {
                allClasses.add(Class.forName(clazz));
            } catch (ClassNotFoundException e) {
                LOG.warn("Cannot add to cache class with name {}", clazz, e);
            }
        }
        
        return allClasses;
    }

    @After
    public void tearDown() {
        stopVideo();
        demountDriver();
    }

    private void stopVideo() {
        if (PageFactory.isVideoRecorderEnabled() && VideoRecorder.getInstance().isVideoStarted()) {
            ParamsHelper.addParam("Video url", VideoRecorder.getInstance().stopRecording());
            VideoRecorder.getInstance().resetVideoRecorder();
        }
    }

    private void demountDriver() {
        if (PageFactory.getEnvironment() == Environment.WEB && TagWebDriver.isWebDriverShared()) {
            LOG.info("Webdriver sharing is processing...");
            PageFactory.setSharingIsActive(true);
        } else {
            PageFactory.dispose();
        }
    }
}
