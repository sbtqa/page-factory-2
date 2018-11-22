package ru.sbtqa.tag.pagefactory.tasks;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class StartVideoTask implements Task {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    @Override
    public void handle() {
        if(PROPERTIES.isVideoEnabled()) {
            VideoRecorder.setVideoFolder(PROPERTIES.getVideoPath());
            VideoRecorder.startRecording();
        }
    }
}
