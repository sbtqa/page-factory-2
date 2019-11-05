package ru.sbtqa.tag.stepdefs.ru;

import cucumber.api.java.Before;
import cucumber.api.java.bg.И;
import cucumber.api.java.en.And;
import cucumber.api.java.ru.Тогда;
import io.cucumber.datatable.DataTable;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.junit.ApiSetupSteps;
import ru.sbtqa.tag.api.junit.ApiSteps;
import ru.sbtqa.tag.api.storage.BlankStorage;

import static ru.sbtqa.tag.api.utils.CastUtils.toMap;


public class ApiStepDefs {

    ApiSteps apiSteps = ApiSteps.getInstance();

    @Before
    public void iniApi() {
        ApiSetupSteps.initApi();
    }

    @And("^(?:пользователь |он )?отправляет запрос$")
    public void send() {
        apiSteps.send();
    }

    @И("^(?:пользователь |он )?отправляет запрос \"([^\"]*)\"$")
    public void send(String endpoint) {
        apiSteps.send(endpoint);
    }

    @И("^(?:пользователь |он )?отправляет запрос \"([^\"]*)\" с параметрами:?$")
    public void send(String endpoint, DataTable dataTable) {
        apiSteps.send(endpoint, toMap(dataTable));
    }

    @И("^(?:пользователь |он )?проверяет ответ$")
    @Тогда("система возвращает ответ$")
    public void validate() {
        apiSteps.validate();
    }

    @И("^(?:пользователь |он )?проверяет (?:ответ )?\"([^\"]*)\"$")
    @Тогда("^система возвращает (?:ответ )?\"([^\"]*)\"$")
    public void validate(String rule) {
        apiSteps.validate(rule);
    }

    @И("^(?:пользователь |он )?проверяет (?:ответ )?\"([^\"]*)\" с параметрами:?$")
    @Тогда("^система возвращает (?:ответ )?\"([^\"]*)\" с параметрами:?$")
    public void validate(String rule, DataTable dataTable) {
        EndpointContext.getCurrentEndpoint().validate(rule, dataTable);
    }

    @And("^(?:пользователь |он )?заполняет запрос \"([^\"]*)\"$")
    public void fill(String title) {
        apiSteps.fill(title);
    }

    @And("^(?:пользователь |он )?добавляет (query|header|body) параметр с именем \"([^\"]*)\" и значением \"([^\"]*)\"$")
    public void add(String parameterType, String name, String value) {
        apiSteps.add(parameterType, name, value);
    }

    @And("^(?:пользователь |он )?добавляет (query|header|body) параметры")
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
    @And("^(?:пользователь |он )?добавляет (query|header|body) параметр \"([^\"]*)\" из ответа на запрос \"([^\"]*)\" (путь|header) \"([^\"]*)\"$")
    public void add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter) {
        add(parameterType, parameterName, fromEndpointTitle, fromParameterType, fromParameter, "");
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
    public void add(String parameterType, String parameterName, String fromEndpointTitle, String fromParameterType, String fromParameter, String mask) {
        if (fromParameterType.equals("header")) {
            apiSteps.addToHeader(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        } else {
            apiSteps.addToBody(parameterType, parameterName, fromEndpointTitle, fromParameter, mask);
        }
    }
}
