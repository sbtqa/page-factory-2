package ru.sbtqa.tag.pagefactory.tasks;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.allure.ParamsHelper;
import ru.sbtqa.tag.pagefactory.allure.Type;
import ru.sbtqa.tag.pagefactory.junit.CoreSetupSteps;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StopVideoTask implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(StopVideoTask.class);
    private static final Configuration PROPERTIES = Configuration.create();

    @Override
    public void handle() {
        if (PROPERTIES.isVideoEnabled()) {
            if (VideoRecorder.isVideoRecording()) {
                String filePath = VideoRecorder.stopRecording();
                try {
                    byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
                    ParamsHelper.addAttachmentToRender(fileContent, "video", Type.VIDEO);
                } catch (IOException e) {
                    LOG.error("Failed to save video", e);
                }
            }
        }
    }
}
