package pagefactory.mobile;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, format = {"pretty"},
        glue = {"ru.sbtqa.tag.stepdefs"},
        features = {"src/test/resources/features/"}
        ,tags = {"not @test"}
)
public class CucumberTest {

}
