package ru.sbtqa.tag.api.steps;

import cucumber.api.java.en.And;
import io.cucumber.datatable.DataTable;
import org.junit.Assert;
import ru.sbtqa.tag.datajack.Stash;

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
}

