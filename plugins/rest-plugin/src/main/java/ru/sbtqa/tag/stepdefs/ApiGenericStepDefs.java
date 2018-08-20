package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import java.util.Map;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.storage.EndpointBlank;
import ru.sbtqa.tag.api.utils.FromResponseUtils;

/**
 * Basic step definitions, that should be available on every project
 *
 * <p>
 * To pass a Cucumber {@link cucumber.api.DataTable} as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | name 1| name 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a {@link cucumber.api.DataTable} object.
 * First line is not enforced to be a name.
 * <p>
 * To pass a list as parameter, use flattened table as follows: | value 1 | }
 * value 2 |
 *
 * @see <a href="https://cucumber.io/docs/reference#step-definitions">Cucumber
 * documentation</a>
 */
public class ApiGenericStepDefs extends ApiSetupSteps {

    /**
     * TODO
     */
    public void sendRequest() {
        String endpoint = ApiEnvironment.getBlankStorage().getLast().getTitle();
        EndpointManager.getEndpoint(endpoint).send();
    }

    /**
     * Execute endpoint endpoint (request) with no parameters
     *
     * @param endpoint name value of the endpoint annotation to execute
     * @throws RestPluginException if there is an error while endpoint executing
     */
    public void sendRequest(String endpoint) {
        EndpointManager.getEndpoint(endpoint).send();
    }

    /**
     * Execute endpoint endpoint (request) with parameters from given
     * {@link cucumber.api.DataTable}
     *
     * @param endpoint name value of the endpoint annotation to execute
     * @param dataTable table of parameters
     * @throws RestPluginException if there is an error while endpoint executing
     */
    public void sendRequestDatatable(String endpoint, DataTable dataTable) {
        EndpointManager.getEndpoint(endpoint).send(dataTable);
    }

    /**
     * Execute a validation rule annotated by
     * {@link ru.sbtqa.tag.api.annotation.Validation} on current
     * endpoint
     *
     * @param rule name of the validation rule (name value of the
     * {@link ru.sbtqa.tag.api.annotation.Validation} annotation)
     * @throws RestPluginException if there is an error while validation rule executing
     */
    public void validate(String rule) {
        EndpointContext.getCurrentEndpoint().validate(rule);
    }

    /**
     * Execute a validation rule annotated by
     * {@link ru.sbtqa.tag.api.annotation.Validation} on current
     * endpoint with parameters from given {@link cucumber.api.DataTable}
     *
     * @param rule name of the validation rule (name value of the
     * {@link ru.sbtqa.tag.api.annotation.Validation} annotation)
     * @param dataTable table of parameters
     * @throws RestPluginException if there is an error while validation rule executing
     */
    public void validateTable(String rule, DataTable dataTable) {
        EndpointContext.getCurrentEndpoint().validate(rule, dataTable);
    }

    /**
     * TODO
     * @param title
     */
    public void fillRequest(String title) {
        ApiEnvironment.getBlankStorage().add(new EndpointBlank(title));
    }

    /**
     * TODO
     * @param parameterType
     * @param name
     * @param value
     */
    public void addParameter(String parameterType, String name, String value) {
        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        ApiEnvironment.getBlankStorage().getLast().addParameter(type, name, value);
    }

    /**
     * TODO
     * @param parameterType
     * @param dataTable
     */
    public void addParameters(String parameterType, DataTable dataTable) {
        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            ApiEnvironment.getBlankStorage().getLast().addParameter(type, dataTableRow.getKey(), dataTableRow.getValue());
        }
    }

    /**
     * TODO
     * @param parameterType
     * @param parameterName
     * @param fromEndpointTitle
     * @param path
     * @param mask
     */
    public void addParameterFromResponseBody(String parameterType, String parameterName, String fromEndpointTitle, String path, String mask) {
        Class fromEndpoint = ApiEnvironment.getRepository().get(fromEndpointTitle);
        String value = (String) FromResponseUtils.getValueFromResponse(fromEndpoint, false, "", path, mask, true);

        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        ApiEnvironment.getBlankStorage().getLast().addParameter(type, parameterName, value);
    }

    /**
     * TODO
     * @param parameterType
     * @param parameterName
     * @param fromEndpointTitle
     * @param headerName
     * @param mask
     */
    public void addParameterFromResponseHeader(String parameterType, String parameterName, String fromEndpointTitle, String headerName, String mask) {
        Class fromEndpoint = ApiEnvironment.getRepository().get(fromEndpointTitle);
        String value = (String) FromResponseUtils.getValueFromResponse(fromEndpoint, false, headerName, "", mask, true);

        ParameterType type = ParameterType.valueOf(parameterType.toUpperCase());
        ApiEnvironment.getBlankStorage().getLast().addParameter(type, parameterName, value);
    }
}
