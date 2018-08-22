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
    @And("^user sends request$")
    public ApiSteps send() {
        return super.send();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request (?:for|to|about) \"([^\"]*)\"$")
    public ApiSteps send(String endpoint) {
        return super.send(endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request (?:for|to|about) \"([^\"]*)\" with parameters:?$")
    public ApiSteps send(String endpoint, DataTable dataTable) {
        return super.send(endpoint, dataTable);
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
    public void validate(String rule, DataTable dataTable) {
        super.validate(rule, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user fill the request \"([^\"]*)\"$")
    public ApiSteps fill(String title) {
        return super.fill(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user add a (query|header|body) parameter with name \"([^\"]*)\" and value \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String name, String value) {
        return super.add(parameterType, name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user add a (query|header|body) parameters$")
    public ApiSteps add(String parameterType, DataTable dataTable) {
        return super.add(parameterType, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @And("^user add (query|header|body) parameter \"([^\"]*)\" from response on \"([^\"]*)\". (Body|Header) \"([^\"]*)\" mask \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter, String mask) {
        if (fromParameterType.equals("Header")) {
            return super.addToHeader(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        } else {
            return super.addToBody(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        }
    }
}
