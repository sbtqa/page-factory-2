package ru.sbtqa.tag.pagefactory.tasks;

import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class StopVideoTask implements Task {

    private static final Configuration PROPERTIES = Configuration.create();

    @Override
    public void handle() {
        if (PROPERTIES.isVideoEnabled()) {
            VideoRecorder.stopRecording();
        }
    }
}
