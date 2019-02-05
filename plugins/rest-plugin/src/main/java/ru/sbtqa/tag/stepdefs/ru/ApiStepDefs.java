package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.bg.И;
import cucumber.api.java.en.And;
import cucumber.api.java.ru.Тогда;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.storage.BlankStorage;
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
        return super.send(endpoint, toMap(dataTable));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?проверяет ответ$")
    @Тогда("система возвращает ответ$")
    public void validate() {
        super.validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @И("^(?:пользователь |он )?проверяет (?:ответ )?\"([^\"]*)\"$")
    @Тогда("^система возвращает (?:ответ )?\"([^\"]*)\"$")
    public void validate(String rule) {
        super.validate(rule);
    }

    /**
     * {@inheritDoc}
     */
    @И("^(?:пользователь |он )?проверяет (?:ответ )?\"([^\"]*)\" с параметрами:?$")
    @Тогда("^система возвращает (?:ответ )?\"([^\"]*)\" с параметрами:?$")
    public void validate(String rule, DataTable dataTable) {
        EndpointContext.getCurrentEndpoint().validate(rule, dataTable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?заполняет запрос \"([^\"]*)\"$")
    public ApiSteps fill(String title) {
        return super.fill(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @And("^(?:пользователь |он )?добавляет (query|header|body) параметр с именем \"([^\"]*)\" и значением \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String name, String value) {
        return super.add(parameterType, name, value);
    }

    /**
     * {@inheritDoc}
     */
    @And("^(?:пользователь |он )?добавляет (query|header|body) параметры")
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
    @And("^(?:пользователь |он )?добавляет (query|header|body) параметр \"([^\"]*)\" из ответа на запрос \"([^\"]*)\" (путь|header) \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter) {
        return add(parameterType, parameterName, fromEndpointTitle, fromParameterType, fromParameter, "");
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
     * @param mask apply mask on this value
     */
    @And("^(?:пользователь |он )?добавляет (query|header|body) параметр \"([^\"]*)\" из ответа на запрос \"([^\"]*)\" (путь|header) \"([^\"]*)\" маска \"([^\"]*)\"$")
    public ApiSteps add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter, String mask) {
        if (fromParameterType.equals("header")) {
            return super.addToHeader(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        } else {
            return super.addToBody(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        }
    }
}
