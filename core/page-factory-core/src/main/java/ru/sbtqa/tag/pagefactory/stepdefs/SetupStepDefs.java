package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.TestEnvironment;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class SetupStepDefs {

    private static final Logger LOG = LoggerFactory.getLogger(SetupStepDefs.class);

    private static final String DEFAULT_LOG_PROPERTIES_PATH = "src/test/resources/config/log4j.properties";

    @Before(order = 10001)
    public void setUp(Scenario scenario) {
        TestEnvironment.getDriverService().mountDriver();
        TestEnvironment.setProperties(Properties.getProperties());
        ScenarioContext.setScenario(scenario);
        connectToLogProperties();
        stopTasksToKill();
        PageFactory.getInstance().cachePages();
        PageContext.resetContext();
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

    @After
    public void tearDown() {
        attachScreenshotToReport();
        stopVideo();
        TestEnvironment.getDriverService().demountDriver();
    }

    private void attachScreenshotToReport() {
//        boolean isScenarioFailed = ScenarioContext.getScenario().isFailed();
//        if (isScenarioFailed && PageFactory.isDriverInitialized()) {
//            ParamsHelper.addAttachmentToRender(ScreenShooter.take(), "Screenshot", Type.PNG);
//        }
    }

    private void stopVideo() {
        if (PageFactory.isVideoRecorderEnabled() && VideoRecorder.getInstance().isVideoStarted()) {
//            ParamsHelper.addParam("Video url", VideoRecorder.getInstance().stopRecording());
            VideoRecorder.getInstance().resetVideoRecorder();
        }
    }
}
