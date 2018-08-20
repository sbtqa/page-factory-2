package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.stepdefs.ApiGenericStepDefs;


public class ApiStepDefs extends ApiGenericStepDefs {

    @Before
    public void iniApi() {
        super.initApi();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request$")
    public void sendRequest() {
        super.sendRequest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request (?:for|to|about) \"([^\"]*)\"$")
    public void sendRequest(String endpoint) {
        super.sendRequest(endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request (?:for|to|about) \"([^\"]*)\" with parameters:?$")
    public void sendRequestDatatable(String endpoint, DataTable dataTable) {
        super.sendRequestDatatable(endpoint, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^system returns \"([^\"]*)\"$")
    public void validate(String rule) {
        super.validate(rule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^system returns \"([^\"]*)\" with parameters:?$")
    public void validateTable(String rule, DataTable dataTable) {
        super.validateTable(rule, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user fill the request \"([^\"]*)\"$")
    public void fillRequest(String title) {
        super.fillRequest(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user add a (query|header|body) parameter with name \"([^\"]*)\" and value \"([^\"]*)\"$")
    public void addParameter(String parameterType, String name, String value) {
        super.addParameter(parameterType, name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user add a (query|header|body) parameters$")
    public void addParameters(String parameterType, DataTable dataTable) {
        super.addParameters(parameterType, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user add a (query|header|body) parameter with name \"([^\"]*)\" and get value from response on \"([^\"]*)\" from body by path \"([^\"]*)\" and apply mask \"([^\"]*)\"$")
    public void addParameterFromResponseBody(String parameterType, String parameterName, String fromEndpointTitle, String path, String mask) {
        super.addParameterFromResponseBody(parameterType, parameterName, fromEndpointTitle, path, mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user add a (query|header|body) parameter with name \"([^\"]*)\" and get value from response on \"([^\"]*)\" from header with name \"([^\"]*)\" and apply mask \"([^\"]*)\"$")
    public void addParameterFromResponseHeader(String parameterType, String parameterName, String fromEndpointTitle, String headerName, String mask) {
        super.addParameterFromResponseHeader(parameterType, parameterName, fromEndpointTitle, headerName, mask);
    }
}
