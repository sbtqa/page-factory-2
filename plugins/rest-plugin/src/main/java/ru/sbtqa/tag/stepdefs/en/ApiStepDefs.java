package ru.sbtqa.tag.stepdefs.en;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import java.util.Map;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.storage.EndpointBlank;
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

    @And("^user fill the request \"([^\"]*)\"$")
    public void userFillTheRequest(String title) {
        ApiEnvironment.getBlankStorage().add(new EndpointBlank(title));
    }

    @And("^user add a header with name \"([^\"]*)\" and value \"([^\"]*)\"$")
    public void userAddHeader(String name, String value) {
        ApiEnvironment.getBlankStorage().getLast().addHeader(name, value);
    }

    @And("^user add a headers$")
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
}
