package ru.sbtqa.tag.stepdefs;

import static java.lang.ThreadLocal.withInitial;

public class WebSteps extends WebGenericSteps<WebSteps> {

    static final ThreadLocal<WebSteps> storage = withInitial(WebSteps::new);

    public static WebSteps getInstance() {
        return storage.get();
    }
}
