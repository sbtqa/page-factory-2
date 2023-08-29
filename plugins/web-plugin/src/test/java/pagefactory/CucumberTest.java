package pagefactory;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, plugin = {"pretty"},
        glue = {"ru.sbtqa.tag.stepdefs",  "setting", "pagefactory.stepdefs"},
        features = {"src/test/resources/features"}
)
public class CucumberTest {
    public CucumberTest() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
    }
}