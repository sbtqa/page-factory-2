package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.bg.И;
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
    @And("^(?:пользователь |он )?отправляет запрос$")
    public void sendRequest() {
        super.sendRequest();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?отправляет запрос \"([^\"]*)\"$")
    public void sendRequest(String endpoint) {
        super.sendRequest(endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?отправляет запрос \"([^\"]*)\" с параметрами:?$")
    public void sendRequestDatatable(String endpoint, DataTable dataTable) {
        super.sendRequestDatatable(endpoint, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^система возвращает ответ \"([^\"]*)\"$")
    public void validate(String rule) {
        super.validate(rule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^система возвращает ответ \"([^\"]*)\" с параметрами:?$")
    public void validateTable(String rule, DataTable dataTable) {
        super.validateTable(rule, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )? заполняет запрос \"([^\"]*)\"$")
    public void fillRequest(String title) {
        super.fillRequest(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )? добавляет (query|header|body) параметр с именем \"([^\"]*)\" и значением \"([^\"]*)\"$")
    public void addParameter(String parameterType, String name, String value) {
        super.addParameter(parameterType, name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )? добавляет (query|header|body) параметры")
    public void addParameters(String parameterType, DataTable dataTable) {
        super.addParameters(parameterType, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @And("^(?:пользователь |он )? добавляет (query|header|body) параметр \"([^\"]*)\" из ответа на запрос \"([^\"]*)\". (Путь|Header) \"([^\"]*)\" маскa \"([^\"]*)\"$")
    public void addParameter(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter, String mask) {
        if (fromParameterType.equals("Header")) {
            super.addParameterFromResponseHeader(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        } else {
            super.addParameterFromResponseBody(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        }
    }
}
