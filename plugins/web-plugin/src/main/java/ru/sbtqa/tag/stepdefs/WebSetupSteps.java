package ru.sbtqa.tag.stepdefs;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;
import ru.sbtqa.tag.pagefactory.web.tasks.KillAlertTask;

public class WebSetupSteps {

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    private WebSetupSteps() {}

    public static synchronized void initWeb() {
        PageManager.cachePages();
        PageContext.resetContext();

        if (isNewDriverNeeded()) {
            Environment.setDriverService(new WebDriverService());
        }
    }

    private static boolean isNewDriverNeeded() {
        return Environment.isDriverEmpty()
                || (!Environment.isDriverEmpty() && !PROPERTIES.getShared());
    }

    public static synchronized void disposeWeb() {
        TaskHandler.addTask(new KillAlertTask());
    }
}