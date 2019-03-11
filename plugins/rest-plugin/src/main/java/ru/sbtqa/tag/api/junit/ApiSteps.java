package ru.sbtqa.tag.api.junit;

import static java.lang.ThreadLocal.withInitial;

public class ApiSteps extends ApiStepsImpl<ApiSteps> {

    static final ThreadLocal<ApiSteps> storage = withInitial(ApiSteps::new);

    public static ApiSteps getInstance() {
        return storage.get();
    }
}
