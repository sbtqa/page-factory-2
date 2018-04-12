package ru.sbtqa.tag.pagefactory.events;

import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.videorecorder.VideoRecorder;

public class StartVideoTask implements Task {

    @Override
    public void handle() {
        if (PageFactory.isVideoRecorderEnabled() && VideoRecorder.getInstance().isVideoStarted()) {
//            ParamsHelper.addParam("Video url", VideoRecorder.getInstance().stopRecording());
            VideoRecorder.getInstance().resetVideoRecorder();
        }
    }
}
