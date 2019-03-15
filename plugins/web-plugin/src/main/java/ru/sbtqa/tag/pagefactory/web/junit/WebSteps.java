package ru.sbtqa.tag.pagefactory.web.junit;

import static java.lang.ThreadLocal.withInitial;

public class WebSteps extends WebStepsImpl<WebSteps> {

    static final ThreadLocal<WebSteps> storage = withInitial(WebSteps::new);

    public static WebSteps getInstance() {
        return storage.get();
    }
}
