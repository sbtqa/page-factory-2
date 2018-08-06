package ru.sbtqa.tag.apifactory.entries.apirequest;

import cucumber.api.DataTable;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Parameter;
import ru.sbtqa.tag.api.annotation.Vallidation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.parsers.core.exceptions.ParserException;

@Endpoint(method = HTTP.GET, path = "client/get-with-params?" + Default.PARAMETER_NAME1 + "=%param", title = "api request with params test")
public class ApiRequestWithParamsEntry extends ApiEntry {

    @Parameter(name = "param-name")
    private String param;

    @Parameter(name = "name-name")
    @Header(name = Default.HEADER_NAME)
    private String header;

    @Vallidation(title = "result")
    public void validate(DataTable dataTable) {
//        JsonParser parser = new JsonParser();
//        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());
//
//        Map<String, String> expectedResultsTable =  dataTable.asMap(String.class, String.class);
//        String expectedResult = expectedResultsTable.get("name-name") + expectedResultsTable.get("param-name");
//        String actualResult = parser.read(response, "$.result");
//
//        Assert.assertEquals(expectedResult, actualResult);

        System.out.println("@Endpoint(method = HTTP.GET, path = \"client/get-with-params?\" + Default.PARAMETER_NAME1 + \"=%param\", name = \"api request with params test\")");
    }


}
