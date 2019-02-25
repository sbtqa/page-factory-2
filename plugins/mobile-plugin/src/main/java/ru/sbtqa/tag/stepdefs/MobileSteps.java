package ru.sbtqa.tag.stepdefs;

import static java.lang.ThreadLocal.withInitial;

public class MobileSteps extends MobileGenericSteps<MobileSteps> {

    static final ThreadLocal<MobileSteps> storage = withInitial(MobileSteps::new);

    public static MobileSteps getInstance() {
        return storage.get();
    }
}
