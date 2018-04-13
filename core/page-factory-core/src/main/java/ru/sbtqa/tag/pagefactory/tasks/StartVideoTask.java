package ru.sbtqa.tag.pagefactory.tasks;

import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class StartVideoTask implements Task {

    @Override
    public void handle() {
            VideoRecorder.getInstance().startRecording();
    }
}
