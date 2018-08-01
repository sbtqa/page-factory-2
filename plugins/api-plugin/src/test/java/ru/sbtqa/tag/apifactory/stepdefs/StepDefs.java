package ru.sbtqa.tag.api.stepdefs;

import cucumber.api.java.en.Given;
import ru.sbtqa.tag.api.ApiFactory;
import ru.sbtqa.tag.api.rest.RestEntityImpl;
import ru.sbtqa.tag.api.rest.RestFormImpl;
import ru.sbtqa.tag.api.rest.RestRawImpl;

public class StepDefs {

    @Given("^user activate raw rest$")
    public void activateRawRest() {
        ApiFactory.getApiFactory().setRest(RestRawImpl.class);
    }

    @Given("^user activate entity rest$")
    public void activateEntityRest() {
        ApiFactory.getApiFactory().setRest(RestEntityImpl.class);
    }

    @Given("^user activate form rest$")
    public void activateFormRest() {
        ApiFactory.getApiFactory().setRest(RestFormImpl.class);
    }
}
