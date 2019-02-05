package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.storage.BlankStorage;
import ru.sbtqa.tag.stepdefs.ApiSteps;

import static ru.sbtqa.tag.api.utils.CastUtils.toMap;

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
    @And("^user sends request (?:for|to|about) \"([^\"]*)\" with parameters:?$")
    public ApiSteps send(String endpoint, DataTable dataTable) {
        return super.send(endpoint, toMap(dataTable));
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
    @And("^user validates response$")
    public void validate() {
        super.validate();
    }

    /**
     * {@inheritDoc}
     */
    @And("^system returns \"([^\"]*)\" with parameters:?$")
    public void validate(String rule, DataTable dataTable) {
        EndpointContext.getCurrentEndpoint().validate(rule, dataTable);
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
    @And("^user add a (query|header|body) parameters$")
    public ApiSteps add(String parameterType, DataTable dataTable) {
        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        return super.add(type, toMap(dataTable));
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
    public ApiSteps add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter) {
        return add(parameterType, parameterName, fromEndpointTitle, fromParameterType, fromParameter, "");
    }

    /**
     * {@inheritDoc}
     */
    @And("^user add (query|header|body) parameter \"([^\"]*)\" from response on \"([^\"]*)\" (body|header) \"([^\"]*)\" mask \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter, String mask) {
        if (fromParameterType.equals("header")) {
            return super.addToHeader(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        } else {
            return super.addToBody(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        }
    }
}
