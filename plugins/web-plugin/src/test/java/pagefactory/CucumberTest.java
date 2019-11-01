package pagefactory;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, plugin = {"pretty"},
        glue = {"ru.sbtqa.tag.stepdefs",  "setting", "pagefactory.stepdefs", "ru.sbtqa.tag.pagefactory.transformer"},
        features = {"src/test/resources/features"}
)
public class CucumberTest {}