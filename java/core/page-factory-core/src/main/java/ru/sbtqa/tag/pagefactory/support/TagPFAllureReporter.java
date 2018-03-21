package ru.sbtqa.tag.pagefactory.support;

import gherkin.formatter.model.Result;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.allurehelper.TagAllureReporter;
import ru.sbtqa.tag.allurehelper.Type;
import ru.sbtqa.tag.pagefactory.PageFactory;

public class TagPFAllureReporter extends TagAllureReporter {

    private static final String FAILED = "failed";
    private static final String SCREENSHOT = "Screenshot";
    
    @Override
    public void result(final Result result) {
        super.result(result);
        if (FAILED.equals(result.getStatus()) && PageFactory.isDriverInitialized()) {
            ParamsHelper.addAttachment(ScreenShooter.take(), SCREENSHOT, Type.PNG);
        }
    }
}
