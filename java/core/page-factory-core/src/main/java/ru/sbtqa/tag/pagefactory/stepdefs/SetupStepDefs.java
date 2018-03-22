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
import ru.sbtqa.tag.pagefactory.support.properties.Properties;
import ru.sbtqa.tag.qautils.reflect.ClassUtilsExt;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class SetupStepDefs {

    private static final Logger LOG = LoggerFactory.getLogger(SetupStepDefs.class);

    @Before
    public void setUp() {

        //try to connect logger property file if exists
        String path = "src/test/resources/config/log4j.properties";
        if (new File(path).exists()) {
            PropertyConfigurator.configure(path);
            LOG.info("Log4j proprties were picked up on the path " + path);
        } else {
            LOG.warn("There is no log4j.properties on the path " + path);
        }

        try {
            String[] tasks = Properties.getProperties().getTasksToKill().split(",");
            if (tasks.length > 0) {
                for (String task : tasks) {
                    Runtime.getRuntime().exec("taskkill /IM " + task.trim() + " /F");
                }
            }
        } catch (IOException e) {
            LOG.debug("Failed to kill one of task to kill", e);
        }

        PageFactory.getDriver();
        PageFactory.getInstance();
        // reset page context. In the start of all tests we must have a clear context
        PageContext.resetContext();

        Reflections reflections;
        reflections = new Reflections(PageFactory.getPagesPackage());

        Collection<String> allClassesString = reflections.getStore().get("SubTypesScanner").values();
        Set<Class<?>> allClasses = new HashSet();
        for (String clazz : allClassesString) {
            try {
                allClasses.add(Class.forName(clazz));
            } catch (ClassNotFoundException e) {
                LOG.warn("Cannot add all classes to set from package storage", e);
            }
        }

        for (Class<?> page : allClasses) {
            List<Class> supers = ClassUtilsExt.getSuperclassesWithInheritance(page);
            // TODO: var we can't know which plugin we are using
//            if (!supers.contains(WebElementsPage.class) && !supers.contains(HtmlElement.class)) {
//                if (page.getName().contains("$")) {
//                    continue; //We allow private additional classes but skip it if its not extends WebElementsPage
//                } else {
//                    throw new FactoryRuntimeException("Class " + page.getName() + " is not extended from WebElementsPage class. Check you webdriver.pages.package property.");
//                }
//            }
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

    @After
    public void tearDown() {
        if (VideoRecorder.getInstance().isVideoStarted()) {
            String videoPath = VideoRecorder.getInstance().stopRecording();
            if (videoPath != null) {
                ParamsHelper.addVideoParameter(VideoRecorder.getInstance().getVideoPath());
                VideoRecorder.getInstance().resetVideoRecorder();
            }
        }
        PageFactory.dispose();
    }
}
