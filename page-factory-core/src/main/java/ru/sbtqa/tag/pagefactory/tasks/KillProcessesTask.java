package ru.sbtqa.tag.pagefactory.tasks;

import java.io.IOException;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;

public class KillProcessesTask implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(CoreSetupSteps.class);
    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    @Override
    public void handle() {
        stopTasksToKill();
    }

    private void stopTasksToKill() {
        String tasks = PROPERTIES.getTasksToKill();
        if (!tasks.isEmpty()) {
            for (String task : tasks.split(",")) {
                stopTask(task);
            }
        }
    }

    private void stopTask(String task) {
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                Runtime.getRuntime().exec("taskkill /IM " + task.trim() + " /F");
            } else {
                Runtime.getRuntime().exec("killall " + task.trim());
            }
        } catch (IOException e) {
            LOG.debug("Failed to kill " + task, e);
        }
    }
}
