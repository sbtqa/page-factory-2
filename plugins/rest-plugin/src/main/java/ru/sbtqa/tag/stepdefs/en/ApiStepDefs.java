package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.junit.ApiSetupSteps;
import ru.sbtqa.tag.api.junit.ApiSteps;
import ru.sbtqa.tag.api.storage.BlankStorage;

import static ru.sbtqa.tag.api.utils.CastUtils.toMap;

public class ApiStepDefs {

    private final ApiSteps apiSteps = ApiSteps.getInstance();

    @Before
    public void iniApi() {
        ApiSetupSteps.initApi();
    }

    @And("^user sends request$")
    public void send() {
        apiSteps.send();
    }

    @And("^user sends request (?:for|to|about) \"([^\"]*)\"$")
    public void send(String endpoint) {
        apiSteps.send(endpoint);
    }

    @And("^user sends request (?:for|to|about) \"([^\"]*)\" with parameters:?$")
    public void send(String endpoint, DataTable dataTable) {
        apiSteps.send(endpoint, toMap(dataTable));
    }

    @And("^system returns \"([^\"]*)\"$")
    public void validate(String rule) {
        apiSteps.validate(rule);
    }

    @And("^user validates response$")
    public void validate() {
        apiSteps.validate();
    }

    @And("^system returns \"([^\"]*)\" with parameters:?$")
    public void validate(String rule, DataTable dataTable) {
        apiSteps.validate(rule, dataTable);
    }

    @And("^user fill the request \"([^\"]*)\"$")
    public void fill(String title) {
        apiSteps.fill(title);
    }

    @And("^user add a (query|header|body) parameter with name \"([^\"]*)\" and value \"([^\"]*)\"$")
    public void add(String parameterType, String name, String value) {
        apiSteps.add(parameterType, name, value);
    }

    @And("^user add a (query|header|body) parameters$")
    public void add(String parameterType, DataTable dataTable) {
        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        apiSteps.add(type, toMap(dataTable));
    }

    /**
     * Add parameter with {@link ParameterType} to the last endpoint blank in {@link BlankStorage}.
     * Get value from header of one of the previous responses
     *
     * @param parameterType {@link ParameterType} of parameter to add
     * @param parameterName with this name the parameter will be added to endpoint blank
     * @param fromEndpointTitle get response with this title
     * @param fromParameterType get value from header or body
     * @param fromParameter get value from header with this name
     */
    @And("^user add (query|header|body) parameter \"([^\"]*)\" from response on \"([^\"]*)\" (body|header) \"([^\"]*)\"$")
    public void add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter) {
        add(parameterType, parameterName, fromEndpointTitle, fromParameterType, fromParameter, "");
    }

    @And("^user add (query|header|body) parameter \"([^\"]*)\" from response on \"([^\"]*)\" (body|header) \"([^\"]*)\" mask \"([^\"]*)\"$")
    public void add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter, String mask) {
        if (fromParameterType.equals("header")) {
            apiSteps.addToHeader(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        } else {
            apiSteps.addToBody(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        }
    }
}
