package ru.sbtqa.tag.stepdefs;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.ApiDriverService;
import ru.sbtqa.tag.pagefactory.environment.Environment;

public class ApiSetupSteps {

    private static final Logger LOG = LoggerFactory.getLogger(ApiSetupSteps.class);

    private static final ThreadLocal<Boolean> isApiInited = ThreadLocal.withInitial(() -> false);

    public synchronized void initApi() {
        if (isApiInited.get()) {
            return;
        } else {
            isApiInited.set(true);
        }
        //try to connect logger property file if exists
        String path = "src/test/resources/config/log4j.properties";
        if (new File(path).exists()) {
            PropertyConfigurator.configure(path);
            LOG.info("Log4j proprties were picked up on the path " + path);
        } else {
            LOG.warn("There is no log4j.properties on the path " + path);
        }

        Environment.setDriverService(new ApiDriverService());
    }
}
