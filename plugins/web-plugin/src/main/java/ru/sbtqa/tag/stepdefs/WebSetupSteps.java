package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;
import ru.sbtqa.tag.pagefactory.web.tasks.KillAlertTask;

public class WebSetupSteps {

    static final ThreadLocal<WebDriverService> storage = new ThreadLocal<WebDriverService>() {
        @Override
        protected WebDriverService initialValue() {
            return new WebDriverService();
        }

    };

    private WebSetupSteps() {
    }

    public static synchronized void initWeb() {
        PageManager.cachePages();

        if (Environment.isDriverEmpty()) {
            Environment.setDriverService(storage.get());
        }
    }

    public static synchronized void disposeWeb() {
        TaskHandler.addTask(new KillAlertTask());
    }
}
