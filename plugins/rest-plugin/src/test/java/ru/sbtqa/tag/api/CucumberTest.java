package ru.sbtqa.tag.api;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import ru.sbtqa.tag.api.utils.JettyServiceUtils;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"ru.sbtqa.tag.stepdefs", "ru.sbtqa.tag.api.steps"},
        features = {"src/test/resources/features"},
        plugin = {"pretty"}
)
public class CucumberTest {

    private static Server server;

    @BeforeClass
    public static void setup() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        server = JettyServiceUtils.startJetty();
    }

    @AfterClass
    public static void teardown() throws Exception {
        server.stop();
    }
}
