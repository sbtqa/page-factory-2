package ru.sbtqa.tag.stepdefs;

import cucumber.api.Scenario;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.reflection.ReflectionImpl;
import ru.sbtqa.tag.pagefactory.tasks.ConnectToLogTask;
import ru.sbtqa.tag.pagefactory.tasks.KillProcessesTask;
import ru.sbtqa.tag.pagefactory.tasks.StartVideoTask;
import ru.sbtqa.tag.pagefactory.tasks.StopVideoTask;
import ru.sbtqa.tag.pagefactory.tasks.TaskHandler;

public class CoreSetupSteps {

    private static final ThreadLocal<Boolean> isPreSetUp = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isSetUp = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> isTearDown = ThreadLocal.withInitial(() -> false);

    public void preSetUp(Scenario scenario) {
        if (isAlreadyPerformed(isPreSetUp)) {
            return;
        }

        TaskHandler.addTask(new ConnectToLogTask());
        TaskHandler.addTask(new KillProcessesTask());
        TaskHandler.addTask(new StartVideoTask());

        Environment.setReflection(new ReflectionImpl());
    }

    public void setUp(Scenario scenario) {
        if (isAlreadyPerformed(isSetUp)) {
            return;
        }

        TaskHandler.handleTasks();
        ScenarioContext.setScenario(scenario);
    }

    public void tearDown() {
        if (isAlreadyPerformed(isTearDown)) {
            return;
        }

        TaskHandler.addTask(new StopVideoTask());
        TaskHandler.handleTasks();

        if (!Environment.isDriverEmpty()) {
            Environment.getDriverService().demountDriver();
        }
    }


    private synchronized boolean isAlreadyPerformed(ThreadLocal<Boolean> t) {
        if (t.get()) {
            return true;
        } else {
            t.set(true);
            if (t.equals(isPreSetUp)) {
                isSetUp.remove();
                isTearDown.remove();
            } else if (t.equals(isSetUp)) {
                isTearDown.remove();
            } else if (t.equals(isTearDown)) {
                isPreSetUp.remove();
                isSetUp.remove();
            }
            return false;
        }
    }
}
