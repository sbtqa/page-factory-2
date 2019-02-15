package pagefactory.stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import java.util.Map;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.stepdefs.WebSteps;

public class TestSteps {

    @Given("^failed step$")
    public void fail() {
        throw new AutotestError("It's error");
    }

    @Given("^stores the value \"([^\"]*)\" in a variable \"([^\"]*)\"$")
    public void putInStash(String value, String key) {
        Stash.put(key, value);
    }

    @Then("^user fills the field from stash$")
    public void fillFieldFromStash(DataTable value) throws PageException {
        Map<String, String> map = value.asMap(String.class, String.class);
        for (Map.Entry entry : map.entrySet()) {
            WebSteps.getInstance().fill(entry.getKey().toString(), Stash.getValue(entry.getValue().toString()));
        }
    }

    @Then("^user fills form$")
    public void fillFields(DataTable value) throws PageException {
        Map<String, String> map = value.asMap(String.class, String.class);
        for (Map.Entry entry : map.entrySet()) {
            WebSteps.getInstance().fill(entry.getKey().toString(), entry.getValue().toString());
        }
    }
}
