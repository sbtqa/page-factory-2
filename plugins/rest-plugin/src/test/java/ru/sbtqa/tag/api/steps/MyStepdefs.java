package ru.sbtqa.tag.api.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import static java.lang.String.format;

public class MyStepdefs {

    @And("^print text \"([^\"]*)\" , \"([^\"]*)\" , \"([^\"]*)\"$")
    public void checkData(String brand, String year, String model) {
        Assert.assertEquals("BMW 3er 2014", format("%s %s %s", brand, model, year));
    }

    @And("^user prepares a data array to stash value \"([^\"]*)\":$")
    public void prepareArray(String stashValueName, DataTable data) {
        String array = String.join(",", data.asList());
        Stash.put(stashValueName, array);
    }

    @Before(order = 10001, value = "@test-stash-1")
    public void setUp1() {
        Stash.put("header", "header-parameter-value-1");
    }

    @Before(order = 10001, value = "@test-stash-2")
    public void setUp2() {
        Stash.put("header", "aaaaaaa");
    }

    @Given("^failed step$")
    public void fail() {
        throw new AutotestError("It's error");
    }

    @Given("^do nothing$")
    public void doNothing() {
        System.out.println(1);
    }
}

