package ru.sbtqa.tag.stepdefs;

import cucumber.api.DataTable;
import java.util.Map;
import ru.sbtqa.tag.apifactory.ApiFactory;
import ru.sbtqa.tag.apifactory.exception.ApiException;

/**
 * Basic step definitions, that should be available on every project
 *
 * <p>
 * To pass a Cucumber {@link cucumber.api.DataTable} as a parameter to method,
 * supply a table in the following format after a step ini feature:
 * <p>
 * | header 1| header 2 | | value 1 | value 2 |
 * <p>
 * This table will be converted to a {@link cucumber.api.DataTable} object.
 * First line is not enforced to be a header.
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
     * @param action title value of the api entry annotation to execute
     * @throws ApiException if there is an error while api entry executing
     */
    public void userSendRequestNoParams(String action) throws ApiException {
        ApiFactory.getApiFactory().getApiEntry(action);
        ApiFactory.getApiFactory().getCurrentApiEntry().fireRequest();
    }

    /**
     * Execute api entry action (request) with parameters from given
     * {@link cucumber.api.DataTable}
     *
     * @param action title value of the api entry annotation to execute
     * @param dataTable table of parameters
     * @throws ApiException if there is an error while api entry executing
     */
    public void userSendRequestTableParam(String action, DataTable dataTable) throws ApiException {
        ApiFactory.getApiFactory().getApiEntry(action);
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            ApiFactory.getApiFactory().getCurrentApiEntry().setParamValueByTitle(dataTableRow.getKey(), dataTableRow.getValue());
        }
        ApiFactory.getApiFactory().getCurrentApiEntry().fireRequest();
    }

    /**
     * Execute a validation rule annotated by
     * {@link ru.sbtqa.tag.apifactory.annotation.ApiValidationRule} on current
     * api entry
     *
     * @param rule name of the validation rule (title value of the
     * {@link ru.sbtqa.tag.apifactory.annotation.ApiValidationRule} annotation)
     * @throws ApiException if there is an error while validation rule executing
     */
    public void userValidate(String rule) throws ApiException {
        ApiFactory.getApiFactory().getCurrentApiEntry().fireValidationRule(rule);
    }

    /**
     * Execute a validation rule annotated by
     * {@link ru.sbtqa.tag.apifactory.annotation.ApiValidationRule} on current
     * api entry with parameters from given {@link cucumber.api.DataTable}
     *
     * @param rule name of the validation rule (title value of the
     * {@link ru.sbtqa.tag.apifactory.annotation.ApiValidationRule} annotation)
     * @param dataTable table of parameters
     * @throws ApiException if there is an error while validation rule executing
     */
    public void userValidateTable(String rule, DataTable dataTable) throws ApiException {
        ApiFactory.getApiFactory().getCurrentApiEntry().fireValidationRule(rule, dataTable);
    }
}
