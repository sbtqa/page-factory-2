package ru.sbtqa.tag.api.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class MyStepdefs {
    public MyStepdefs() {
    }

    @When("^print text \"([^\"]*)\"$")
    public void println(String arg0) {
        System.out.println(arg0);
    }

    @And("^print text \"([^\"]*)\" , \"([^\"]*)\" , \"([^\"]*)\"$")
    public void printText(String arg0, String arg1, String arg2) {
        System.out.println(String.format("1=%s, \n 2=%s, \n 3=%s", arg0, arg1, arg2));

    }
}

