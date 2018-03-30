package ru.sbtqa.tag.pagefactory.support;

import ru.sbtqa.tag.allurehelper.TagAllureReporter;

public class TagPFAllureReporter extends TagAllureReporter {

    private static final String FAILED = "failed";
    private static final String SCREENSHOT = "Screenshot";
    
//    @Override
//    public void result(final Result result) {
//        super.result(result);
//        if (FAILED.equals(result.getStatus()) && PageFactory.isDriverInitialized()) {
//            ParamsHelper.addAttachment(ScreenShooter.take(), SCREENSHOT, Type.PNG);
//        }
//    }
}
