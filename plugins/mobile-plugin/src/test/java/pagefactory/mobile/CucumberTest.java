package pagefactory.mobile;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, format = {"pretty"},
      glue = {"ru.sbtqa.tag.pagefactory.mobile.stepdefs"},
      features = {"src/test/resources/features/"},
      tags = {"@install-software"}
)
public class CucumberTest {

}
