package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.repository.ApiRepository;
import ru.sbtqa.tag.api.storage.BlankStorage;

public class ApiSetupSteps {

    private static final ThreadLocal<Boolean> isInitApi = ThreadLocal.withInitial(() -> false);

    public void initApi() {
        if (isAlreadyPerformed(isInitApi)) {
            return;
        }

        ApiEnvironment.setRepository(new ApiRepository());
        ApiEnvironment.setBlankStorage(new BlankStorage());
        EndpointManager.cacheEndpoints();
    }

    private synchronized boolean isAlreadyPerformed(ThreadLocal<Boolean> t) {
        if (t.get()) {
            return true;
        } else {
            t.set(true);
            return false;
        }
    }

}
