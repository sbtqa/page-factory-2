package ru.sbtqa.tag.apifactory.entries.apirequest;

import cucumber.api.DataTable;
import java.util.Map;
import org.junit.Assert;
import ru.sbtqa.tag.apifactory.ApiEntry;
import ru.sbtqa.tag.apifactory.ApiFactory;
import ru.sbtqa.tag.apifactory.annotation.ApiAction;
import ru.sbtqa.tag.apifactory.annotation.ApiRequestHeader;
import ru.sbtqa.tag.apifactory.annotation.ApiRequestParam;
import ru.sbtqa.tag.apifactory.annotation.ApiValidationRule;
import ru.sbtqa.tag.apifactory.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.parsers.JsonParser;
import ru.sbtqa.tag.parsers.core.exceptions.ParserException;

@ApiAction(method = HTTP.GET, path = "client/get-with-params?" + Default.PARAMETER_NAME1 + "=%param", title = "api request with params test")
public class ApiRequestWithParamsEntry extends ApiEntry {

    @ApiRequestParam(title = "param-title")
    private String param;

    @ApiRequestParam(title = "header-title")
    @ApiRequestHeader(header = Default.HEADER_NAME)
    private String header;

    @ApiValidationRule(title = "result")
    public void validate(DataTable dataTable) throws ParserException {
        JsonParser parser = new JsonParser();
        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());

        Map<String, String> expectedResultsTable =  dataTable.asMap(String.class, String.class);
        String expectedResult = expectedResultsTable.get("header-title") + expectedResultsTable.get("param-title");
        String actualResult = parser.read(response, "$.result");

        Assert.assertEquals(expectedResult, actualResult);
    }


}
