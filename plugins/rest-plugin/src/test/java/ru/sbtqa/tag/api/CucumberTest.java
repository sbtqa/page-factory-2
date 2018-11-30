package ru.sbtqa.tag.api;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import ru.sbtqa.tag.api.utils.JettyServiceUtils;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"ru.sbtqa.tag.stepdefs"},
        features = {"src/test/resources/features"},
        plugin = {"pretty"}
        ,tags = {"@dаtatable"}
)
public class CucumberTest {

    private static Server server;

    @BeforeClass
    public static void setup() {
        server = JettyServiceUtils.startJetty();
    }

    @AfterClass
    public static void teardown() throws Exception {
        server.stop();
    }
}
