package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import io.restassured.response.ValidatableResponse;
import java.util.Map;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.storage.EndpointBlank;
import ru.sbtqa.tag.api.utils.RegexUtils;
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
    @And("^user sends request (?:for|to|about) \"([^\"]*)\"$")
    public void userSendRequestNoParams(String endpoint) {
        super.userSendRequestNoParams(endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^user sends request (?:for|to|about) \"([^\"]*)\" with parameters:?$")
    public void userSendRequestTableParam(String endpoint, DataTable dataTable) {
        super.userSendRequestTableParam(endpoint, dataTable);
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


    // TODO добавить русские


    @And("^user fill the request \"([^\"]*)\"$")
    public void userFillTheRequest(String title) {
        ApiEnvironment.getBlankStorage().add(new EndpointBlank(title));
    }

    // TODO свернуть вы один шаг
    @And("^user add a header parameter with name \"([^\"]*)\" and value \"([^\"]*)\"$")
    public void userAddHeader(String name, String value) {
        ApiEnvironment.getBlankStorage().getLast().addHeader(name, value);
    }

    // TODO свернуть вы один шаг
    @And("^user add a header parameters$")
    public void userAddHeaders(DataTable dataTable) {
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            ApiEnvironment.getBlankStorage().getLast().addHeader(dataTableRow.getKey(), dataTableRow.getValue());
        }
    }

    @And("^user add a query parameter with name \"([^\"]*)\" and value \"([^\"]*)\"$")
    public void userAddQuery(String name, String value) {
        ApiEnvironment.getBlankStorage().getLast().addQuery(name, value);
    }

    @And("^user add a query parameters")
    public void userAddQueries(DataTable dataTable) {
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            ApiEnvironment.getBlankStorage().getLast().addQuery(dataTableRow.getKey(), dataTableRow.getValue());
        }
    }

    @And("^user add a body parameter with name \"([^\"]*)\" and value \"([^\"]*)\"$")
    public void userAddBody(String name, String value) {
        ApiEnvironment.getBlankStorage().getLast().addBodyParameter(name, value);
    }

    @And("^user add a body parameters")
    public void userAddBodies(DataTable dataTable) {
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            ApiEnvironment.getBlankStorage().getLast().addBodyParameter(dataTableRow.getKey(), dataTableRow.getValue());
        }
    }

    @And("^user sends request$")
    public void userSendRequestNoParams() {
        String endpoint = ApiEnvironment.getBlankStorage().getLast().getTitle();
        EndpointManager.getEndpoint(endpoint).send();
    }


    @And("^user add a (query|header|body) parameter with name \"([^\"]*)\" and get value from response on \"([^\"]*)\" in body by path \"([^\"]*)\"( and apply mask \"([^\"]*)\")?$")
    public void addParameterFromResponseBody(String parameterType, String parameterName, String fromEndpointTitle, String path, String maskString, String mask) {
        ValidatableResponse fromEndpoint = ApiEnvironment.getRepository().get(fromEndpointTitle).getResponse();
        String value = fromEndpoint.extract().body().path(path).toString();
        if (maskString != null) {
            value = RegexUtils.getFirstMatcherGroup(value, mask);
        }

        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        switch (type) {
            case QUERY:
                ApiEnvironment.getBlankStorage().getLast().addQuery(parameterName, value);
                break;
            case HEADER:
                ApiEnvironment.getBlankStorage().getLast().addHeader(parameterName, value);
                break;
            case BODY:
                ApiEnvironment.getBlankStorage().getLast().addBodyParameter(parameterName, value);
                break;
        }
    }

    //TODO разнести на методы
    @And("^user add a (query|header|body) parameter with name \"([^\"]*)\" and get value from response on \"([^\"]*)\" in header with name \"([^\"]*)\"( and apply mask \"([^\"]*)\")?$")
    public void addParameterFromResponseHeader(String parameterType, String parameterName, String fromEndpointTitle, String headerName, String maskString, String mask) {
        ValidatableResponse fromEndpoint = ApiEnvironment.getRepository().get(fromEndpointTitle).getResponse();
        String value = fromEndpoint.extract().header(headerName);
        if (maskString != null) {
            value = RegexUtils.getFirstMatcherGroup(value, mask);
        }

        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        switch (type) {
            case QUERY:
                ApiEnvironment.getBlankStorage().getLast().addQuery(parameterName, value);
                break;
            case HEADER:
                ApiEnvironment.getBlankStorage().getLast().addHeader(parameterName, value);
                break;
            case BODY:
                ApiEnvironment.getBlankStorage().getLast().addBodyParameter(parameterName, value);
                break;
        }
    }
}
