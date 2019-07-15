package ru.sbtqa.tag.pagefactory.junit;

import static java.lang.ThreadLocal.withInitial;

public class CoreSteps extends CoreStepsImpl<CoreSteps> {

    static final ThreadLocal<CoreSteps> storage = withInitial(CoreSteps::new);

    public static CoreSteps getInstance() {
        return storage.get();
    }
}
