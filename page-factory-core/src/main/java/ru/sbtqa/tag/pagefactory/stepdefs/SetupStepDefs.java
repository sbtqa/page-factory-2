package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.Scenario;
import ru.sbtqa.tag.pagefactory.PageManager;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.tasks.ConnectToLogTask;
import ru.sbtqa.tag.pagefactory.tasks.KillProcessesTask;
import ru.sbtqa.tag.pagefactory.tasks.StartVideoTask;
import ru.sbtqa.tag.pagefactory.tasks.StopVideoTask;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;

public class SetupStepDefs {

    private static final ThreadLocal<Boolean> ispreSetUp = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isSetUp = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isTearDown = ThreadLocal.withInitial(() -> false);

    private SetupStepDefs() {
        throw new AssertionError("Instantiating utility class.");
    }

    public static void preSetUp(Scenario scenario) {
        if (isAlreadyPerformed(ispreSetUp)) {
            return;
        }

        TaskHandler.addTask(new ConnectToLogTask());
        TaskHandler.addTask(new KillProcessesTask());
        TaskHandler.addTask(new StartVideoTask());
        TaskHandler.handleTasks();
    }

    public static void setUp(Scenario scenario) {
        if (isAlreadyPerformed(ispreSetUp)) {
            return;
        }

        Environment.getDriverService().mountDriver();
        ScenarioContext.setScenario(scenario);
        PageManager.cachePages();
        PageContext.resetContext();
    }

    public static void tearDown() {
        if (isAlreadyPerformed(ispreSetUp)) {
            return;
        }

        TaskHandler.addTask(new StopVideoTask());
        TaskHandler.handleTasks();

        Environment.getDriverService().demountDriver();
    }


    private static synchronized boolean isAlreadyPerformed(ThreadLocal<Boolean> t) {
        if (t.get()) {
            return true;
        } else {
            t.set(true);
            if (t.equals(ispreSetUp)) {
                isSetUp.remove();
                isTearDown.remove();
            } else if (t.equals(isSetUp)) {
                isTearDown.remove();
            } else if (t.equals(isTearDown)) {
                ispreSetUp.remove();
                isSetUp.remove();
            }
            return false;
        }
    }

}
