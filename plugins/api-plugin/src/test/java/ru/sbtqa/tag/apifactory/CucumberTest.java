package ru.sbtqa.tag.apifactory;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import ru.sbtqa.tag.api.ApiFactory;
import ru.sbtqa.tag.apifactory.utils.JettyServiceUtils;
import ru.sbtqa.tag.parsers.JsonParser;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"ru.sbtqa.tag.stepdefs", "ru.sbtqa.tag.apifactory.stepdefs"},
        features = {"src/test/resources/features"},
        plugin = {"pretty"}
        ,tags = {"@post"}

)
public class CucumberTest {

    private static Server server;

    @BeforeClass
    public static void setup() {
        server = JettyServiceUtils.startJetty();
        ApiFactory.getApiFactory().setParser(JsonParser.class);
    }

    @AfterClass
    public static void teardown() throws Exception {
        server.stop();
    }
}
