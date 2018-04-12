package ru.sbtqa.tag.pagefactory.stepdefs;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.pagefactory.PageFactory;
import ru.sbtqa.tag.pagefactory.TestEnvironment;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.context.ScenarioContext;
import ru.sbtqa.tag.pagefactory.events.AttachScreenshotToReportTask;
import ru.sbtqa.tag.pagefactory.events.ConnectToLogTask;
import ru.sbtqa.tag.pagefactory.events.KillProcessesTask;
import ru.sbtqa.tag.pagefactory.events.StartVideoTask;
import ru.sbtqa.tag.pagefactory.events.StopVideoTask;
import ru.sbtqa.tag.pagefactory.events.TaskHandler;
import ru.sbtqa.tag.pagefactory.support.properties.Properties;

public class SetupStepDefs {

    @Before(order = 10001)
    public void setUp(Scenario scenario) {
        TestEnvironment.setProperties(Properties.getProperties());
        TestEnvironment.getDriverService().mountDriver();
        ScenarioContext.setScenario(scenario);

        TaskHandler.addTask(new ConnectToLogTask());
        TaskHandler.addTask(new KillProcessesTask());
        TaskHandler.addTask(new StartVideoTask());
        TaskHandler.handleTasks();

        PageFactory.getInstance().cachePages();
        PageContext.resetContext();
    }

    @After
    public void tearDown() {
        TaskHandler.addTask(new AttachScreenshotToReportTask());
        TaskHandler.addTask(new StopVideoTask());
        TaskHandler.handleTasks();

        TestEnvironment.getDriverService().demountDriver();
    }

}
