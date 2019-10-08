package ru.sbtqa.tag.pagefactory.web.junit;

import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;
import ru.sbtqa.tag.pagefactory.web.tasks.KillAlertTask;

import static java.lang.ThreadLocal.withInitial;

public class WebSetupSteps {

    static final ThreadLocal<WebDriverService> storage = withInitial(WebDriverService::new);

    private WebSetupSteps() {
    }

    public static synchronized void initWeb() {
        PageManager.cachePages();

        if (Environment.isDriverEmpty() || !(Environment.getDriverService() instanceof WebDriverService)) {
            Environment.setDriverService(storage.get());
        }
    }

    public static synchronized void disposeWeb() {
        TaskHandler.addTask(new KillAlertTask());
    }
}
