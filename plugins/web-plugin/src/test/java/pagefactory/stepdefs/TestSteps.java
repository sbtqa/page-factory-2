package pagefactory.stepdefs;

import cucumber.api.java.en.Given;
import ru.sbtqa.tag.qautils.errors.AutotestError;

public class TestSteps {

    @Given("^failed step$")
    public void fail(){
        throw new AutotestError("It's error");
    }
}
