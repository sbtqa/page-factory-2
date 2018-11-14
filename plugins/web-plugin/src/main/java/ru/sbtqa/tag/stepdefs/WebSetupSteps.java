package ru.sbtqa.tag.stepdefs;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;
import ru.sbtqa.tag.pagefactory.web.drivers.WebDriverService;
import ru.sbtqa.tag.pagefactory.web.tasks.KillAlertTask;


// TODO переименовать и вынести
public class WebSetupSteps {

    private static final ThreadLocal<Boolean> isWebInited = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isWebDisposed = ThreadLocal.withInitial(() -> false);

    private static final Configuration PROPERTIES = ConfigFactory.create(Configuration.class);

    public static synchronized void initWeb() {
//        isAlreadyPerformed(isWebInited);

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
//        isAlreadyPerformed(isWebDisposed);

        TaskHandler.addTask(new KillAlertTask());
    }

    private static synchronized boolean isAlreadyPerformed(ThreadLocal<Boolean> t) {
        if (t.get()) {
            return true;
        } else {
            t.set(true);
            if (t.equals(isWebInited)) {
                isWebDisposed.remove();
            } else if (t.equals(isWebDisposed)) {
                isWebInited.remove();
            }
            return false;
        }
    }
}
