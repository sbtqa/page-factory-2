package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.bg.И;
import cucumber.api.java.en.And;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.stepdefs.ApiSteps;

import static ru.sbtqa.tag.api.utils.CastUtils.toMap;


public class ApiStepDefs extends ru.sbtqa.tag.stepdefs.ApiSteps {

    @Before
    public void iniApi() {
        super.initApi();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?отправляет запрос$")
    public ApiSteps send() {
        return super.send();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?отправляет запрос \"([^\"]*)\"$")
    public ApiSteps send(String endpoint) {
        return super.send(endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @И("^(?:пользователь |он )?отправляет запрос \"([^\"]*)\" с параметрами:?$")
    public ApiSteps send(String endpoint, DataTable dataTable) {
        return super.send(endpoint, dataTable.asMap(String.class, String.class));
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
    @И("^(?:пользователь |он )?проверяет ответ$")
    public void validate() {
        super.validate();
    }

    /**
     * {@inheritDoc}
     */
    @И("^система возвращает ответ \"([^\"]*)\" с параметрами:?$")
    public void validate(String rule, DataTable dataTable) {
        EndpointContext.getCurrentEndpoint().validate(rule, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )? заполняет запрос \"([^\"]*)\"$")
    public ApiSteps fill(String title) {
        return super.fill(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )? добавляет (query|header|body) параметр с именем \"([^\"]*)\" и значением \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String name, String value) {
        return super.add(parameterType, name, value);
    }

    /**
     * {@inheritDoc}
     */
    @And("^(?:пользователь |он )? добавляет (query|header|body) параметры")
    public ApiSteps add(String parameterType, DataTable dataTable) {
        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        return super.add(type, toMap(dataTable));
    }

    /**
     * {@inheritDoc}
     */
    @And("^(?:пользователь |он )? добавляет (query|header|body) параметр \"([^\"]*)\" из ответа на запрос \"([^\"]*)\". (Путь|Header) \"([^\"]*)\" маскa \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter, String mask) {
        if (fromParameterType.equals("Header")) {
            return super.addToHeader(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        } else {
            return super.addToBody(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        }
    }
}
