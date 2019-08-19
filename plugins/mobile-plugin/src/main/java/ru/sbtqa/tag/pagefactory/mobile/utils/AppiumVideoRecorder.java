package ru.sbtqa.tag.pagefactory.mobile.utils;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions.VideoQuality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.mobile.properties.MobileConfiguration;
import ru.sbtqa.tag.pagefactory.utils.PathUtils;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static java.lang.String.format;

public class AppiumVideoRecorder {

    private static final Logger LOG = LoggerFactory.getLogger(AppiumVideoRecorder.class);
    private static final MobileConfiguration PROPERTIES = MobileConfiguration.create();
    private static final String VIDEO_FILENAME_TEMPLATE = PROPERTIES.getAppiumVideoName() + "%s." + PROPERTIES.getAppiumVideoExtension();


    private String videoFileName;
    private boolean isRecording = false;

    public AppiumVideoRecorder() {
        videoFileName = format(VIDEO_FILENAME_TEMPLATE, new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(new Date()));
    }

    public void startRecord() {
        if (Environment.isDriverEmpty()) {
            LOG.error("Can't start video recording because driver is null");
            return;
        }

        IOSStartScreenRecordingOptions startOptions = new IOSStartScreenRecordingOptions()
                .withVideoType(PROPERTIES.getAppiumVideoType())
                .withVideoScale(PROPERTIES.getAppiumVideoScale())
                .withVideoQuality(VideoQuality.HIGH);

        ((IOSDriver) Environment.getDriverService().getDriver()).startRecordingScreen(startOptions);
        isRecording = true;
    }

    public byte[] stopRecord() {
        if (Environment.isDriverEmpty()) {
            LOG.error("Can't stop and save video because driver is null");
            return null;
        }

        // get Base64 encoded video content
        String encodedString = ((IOSDriver) Environment.getDriverService().getDriver()).stopRecordingScreen();

        // convert to byte array
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString.getBytes());

        // save to file
        String path = PathUtils.unite(PROPERTIES.getAppiumVideoFolder(), videoFileName);
        try (FileOutputStream out = new FileOutputStream(path)) {
            out.write(decodedBytes);
            LOG.info("Video saved to {} successfully", path);
        } catch (Exception e) {
            LOG.error("An error occurred while saving the video to file", e);
        }

        isRecording = false;
        return decodedBytes;
    }

    public String getVideoFileName() {
        return videoFileName;
    }

    public boolean isRecording() {
        return isRecording;
    }
}
