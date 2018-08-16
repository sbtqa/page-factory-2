package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.exception.RestPluginException;

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
public class ApiSteps extends ApiSetupSteps {

    public static final ApiSteps api = new ApiSteps();

    public ApiSteps() {
        initApi();
    }

    /**
     * Execute endpoint action (request) with no parameters
     *
     * @param action name value of the endpoint annotation to execute
     * @throws RestPluginException if there is an error while endpoint executing
     */
    public void userSendRequestNoParams(String action) {
        EndpointManager.getEndpoint(action).send();
    }

    public void userSendRequestNoParams(Class endpoint) {
        EndpointManager.getEndpoint(endpoint).send();
    }

    /**
     * Execute endpoint action (request) with parameters from given
     * {@link cucumber.api.DataTable}
     *
     * @param action name value of the endpoint annotation to execute
     * @param dataTable table of parameters
     * @throws RestPluginException if there is an error while endpoint executing
     */
    public void userSendRequestTableParam(String action, DataTable dataTable) {
        EndpointManager.getEndpoint(action).send(dataTable);
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
    public void userValidate(String rule) {
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
    public void userValidateTable(String rule, DataTable dataTable) {
        EndpointContext.getCurrentEndpoint().validate(rule, dataTable);
    }
}
