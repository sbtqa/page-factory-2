package ru.sbtqa.tag.api.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.junit.Assert;

import static java.lang.String.format;

public class MyStepdefs {

    @And("^print text \"([^\"]*)\" , \"([^\"]*)\" , \"([^\"]*)\"$")
    public void checkData(String brand, String year, String model) {
        Assert.assertEquals("BMW 3er 2014", format("%s %s %s", brand, model, year));
    }
}

