package ru.sbtqa.tag.pagefactory.tasks;

import java.io.File;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;

public class ConnectToLogTask implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(CoreSetupSteps.class);
    private static final String DEFAULT_LOG_PROPERTIES_PATH = "src/test/resources/config/log4j.properties";

    @Override
    public void handle() {
        if (new File(DEFAULT_LOG_PROPERTIES_PATH).exists()) {
            PropertyConfigurator.configure(DEFAULT_LOG_PROPERTIES_PATH);
            LOG.info("Log4j properties were picked up on the path {}", DEFAULT_LOG_PROPERTIES_PATH);
        } else {
            LOG.warn("There is no log4j.properties on the path {}", DEFAULT_LOG_PROPERTIES_PATH);
        }
    }
}
