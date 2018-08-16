package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.stepdefs.ApiSteps;


public class ApiStepDefs extends ApiSteps {

    @Before
    public void iniApi() {
        super.initApi();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request (?:for|to|about) \"([^\"]*)\"$")
    public void userSendRequestNoParams(String action) {
        super.userSendRequestNoParams(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request (?:for|to|about) \"([^\"]*)\" with parameters:?$")
    public void userSendRequestTableParam(String action, DataTable dataTable) {
        super.userSendRequestTableParam(action, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^system returns \"([^\"]*)\"$")
    public void userValidate(String rule) {
        super.userValidate(rule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^system returns \"([^\"]*)\" with parameters:?$")
    public void userValidateTable(String rule, DataTable dataTable) {
        super.userValidateTable(rule, dataTable);
    }
}
