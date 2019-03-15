package ru.sbtqa.tag.pagefactory.mobile.junit;

import static java.lang.ThreadLocal.withInitial;

public class MobileSteps extends MobileStepsImpl<MobileSteps> {

    static final ThreadLocal<MobileSteps> storage = withInitial(MobileSteps::new);

    public static MobileSteps getInstance() {
        return storage.get();
    }
}
