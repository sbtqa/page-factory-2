package ru.sbtqa.tag.apifactory.entries.aipurl;

import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiRequestHeader;
import ru.sbtqa.tag.api.annotation.ApiUrlParam;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.parsers.core.exceptions.ParserException;

@ApiAction(method = HTTP.GET, path = "client/get-with-params", title = "api url test")
public class ApiUrlEntry extends ApiEntry {

    @ApiUrlParam(title = Default.PARAMETER_NAME1)
    private String param = Default.PARAMETER_VALUE1;

    @ApiRequestHeader(header = Default.HEADER_NAME)
    private String header = Default.HEADER_VALUE;

    @ApiValidationRule(title = "result")
    public void validate() throws ParserException {
//        JsonParser parser = new JsonParser();
//        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());
//
//        String expectedResult = header + param;
//        String actualResult = parser.read(response, "$.result");
//
//        Assert.assertEquals(expectedResult, actualResult);

        System.out.println("\n" +
                "@ApiAction(method = HTTP.GET, path = \"client/get-with-params\", title = \"api url test\")");
    }


}
