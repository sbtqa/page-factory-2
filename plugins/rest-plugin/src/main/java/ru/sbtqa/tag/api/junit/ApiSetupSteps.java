package ru.sbtqa.tag.api.junit;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.ToLoggerPrintStream;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.api.repository.ApiRepository;
import ru.sbtqa.tag.api.storage.BlankStorage;

public class ApiSetupSteps {

    private static final ApiConfiguration PROPERTIES = ApiConfiguration.create();
    private static final ThreadLocal<Boolean> isInitApi = ThreadLocal.withInitial(() -> false);

    public static void initApi() {
        if (isAlreadyPerformed(isInitApi)) {
            return;
        }

        ApiEnvironment.setRepository(new ApiRepository());
        ApiEnvironment.setBlankStorage(new BlankStorage());
        EndpointManager.cacheEndpoints();

        if (PROPERTIES.isSslRelaxed()) {
            RestAssured.useRelaxedHTTPSValidation();
        }

        ToLoggerPrintStream toLoggerPrintStream = new ToLoggerPrintStream(LoggerFactory.getLogger(EndpointEntry.class));
        RestAssured.config = RestAssured.config().logConfig(new LogConfig(toLoggerPrintStream.getPrintStream(), true));
    }

    private static synchronized boolean isAlreadyPerformed(ThreadLocal<Boolean> t) {
        if (t.get()) {
            return true;
        } else {
            t.set(true);
            return false;
        }
    }

    public static void tearDown() {
        EndpointContext.clear();
    }
}
