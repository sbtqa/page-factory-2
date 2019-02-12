package stepdefs;

import cucumber.api.java.en.Given;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.datajack.exceptions.StashKeyNotFoundException;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.stepdefs.WebSteps;

public class TestStepDefs {

    @Given("^stores the value \"([^\"]*)\" in a variable \"([^\"]*)\"$")
    public void putInStash(String value, String key) {
        Stash.put(key, value);
    }

    @Given("^user fills the field from stash \"([^\"]*)\" \"([^\"]*)\"$")
    public void fillFieldFromStash(String value, String key) throws PageException {
        WebSteps.getInstance().fill(value, Stash.getValue(key));
    }

    @Given("^checks that the key \"([^\"]*)\" value is empty$")
    public void checkEmpty(String key) {
        try{
            Stash.getValue(key);
            throw new AutotestError("Stash shared for few scenarios!");
        } catch (StashKeyNotFoundException ex) {
        }
    }
}
