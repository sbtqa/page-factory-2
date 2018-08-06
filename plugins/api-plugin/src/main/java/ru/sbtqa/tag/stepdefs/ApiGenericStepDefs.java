package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import ru.sbtqa.tag.api.ApiFactory;
import ru.sbtqa.tag.api.exception.ApiException;

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
     * Execute api entry action (request) with no parameters
     *
     * @param action name value of the api entry annotation to execute
     * @throws ApiException if there is an error while api entry executing
     */
    public void userSendRequestNoParams(String action) {
        ApiFactory.getApiFactory().getApiEntry(action);
        ApiFactory.getApiFactory().getCurrentApiEntry().send();
    }

    /**
     * Execute api entry action (request) with parameters from given
     * {@link cucumber.api.DataTable}
     *
     * @param action name value of the api entry annotation to execute
     * @param dataTable table of parameters
     * @throws ApiException if there is an error while api entry executing
     */
    public void userSendRequestTableParam(String action, DataTable dataTable) {
        ApiFactory.getApiFactory().getApiEntry(action);
        ApiFactory.getApiFactory().getCurrentApiEntry().send(dataTable);
    }

    /**
     * Execute a validation rule annotated by
     * {@link ru.sbtqa.tag.api.annotation.Validation} on current
     * api entry
     *
     * @param rule name of the validation rule (name value of the
     * {@link ru.sbtqa.tag.api.annotation.Validation} annotation)
     * @throws ApiException if there is an error while validation rule executing
     */
    public void userValidate(String rule) {
        ApiFactory.getApiFactory().getCurrentApiEntry().validate(rule);
    }

    /**
     * Execute a validation rule annotated by
     * {@link ru.sbtqa.tag.api.annotation.Validation} on current
     * api entry with parameters from given {@link cucumber.api.DataTable}
     *
     * @param rule name of the validation rule (name value of the
     * {@link ru.sbtqa.tag.api.annotation.Validation} annotation)
     * @param dataTable table of parameters
     * @throws ApiException if there is an error while validation rule executing
     */
    public void userValidateTable(String rule, DataTable dataTable) {
        ApiFactory.getApiFactory().getCurrentApiEntry().validate(rule, dataTable);
    }
}
