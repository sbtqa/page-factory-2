package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.api.ApiEnvironment;
import ru.sbtqa.tag.api.ApiRepository;

public class ApiSetupSteps {

    private static final ThreadLocal<Boolean> isApiInited = ThreadLocal.withInitial(() -> false);

    public synchronized void initApi() {
        if (isApiInited.get()) {
            return;
        } else {
            isApiInited.set(true);
        }
        ApiEnvironment.setRepository(new ApiRepository());
    }
}
