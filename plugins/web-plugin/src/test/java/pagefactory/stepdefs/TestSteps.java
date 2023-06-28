package pagefactory.stepdefs;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.pagefactory.context.PageContext;
import ru.sbtqa.tag.pagefactory.environment.Environment;
import ru.sbtqa.tag.pagefactory.exceptions.AllureNonCriticalError;
import ru.sbtqa.tag.pagefactory.exceptions.PageException;
import ru.sbtqa.tag.pagefactory.web.junit.WebSteps;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.util.List;
import java.util.Map;

public class TestSteps {

    @Before(order = 10001, value = "@test-stash-1")
    public void setUp1() {
        Stash.put("prm", "test1");
    }

    @Before(order = 10001, value = "@test-stash-2")
    public void setUp2() {
        Stash.put("prm", "test2");
    }
    private static final Logger LOG = LoggerFactory.getLogger(TestSteps.class);

    @Given("^failed step$")
    public void fail() {
        throw new AutotestError("It's error");
    }

    @Given("^do nothing$")
    public void doNothing() {
        System.out.println(Stash.getValue("TEMPLATE").toString());
    }

    @Given("^test non critical user error$")
    public void fail_non_critical() {
        throw new AllureNonCriticalError("It's non critical error");
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

    @Then("^check action list parameters \\((.*?)\\)$")
    public void checkAction(String action, List<String> list) throws NoSuchMethodException {
        Environment.getReflection().executeMethodByTitle(PageContext.getCurrentPage(), action, list);
    }

    @Then("^check step list parameters$")
    public void checkStep(List<String> elementName) {
        LOG.info("Check is correct!");
    }


}
