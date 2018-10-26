package ru.sbtqa.tag.stepdefs;

import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;
import ru.sbtqa.tag.pagefactory.web.tasks.KillAlertTask;

public class WebSetupSteps {

    private static final ThreadLocal<Boolean> isWebInited = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isWebDisposed = ThreadLocal.withInitial(() -> false);

    public synchronized void initWeb() {
        if (isWebInited.get()) {
            return;
        } else {
            isWebInited.set(true);
        }

        PageManager.cachePages();
        PageContext.resetContext();
        Environment.setDriverService(new WebDriverService());
    }

    public void disposeWeb() {
        if (isWebDisposed.get()) {
            return;
        } else {
            isWebDisposed.set(true);
        }

        TaskHandler.addTask(new KillAlertTask());
    }
}
