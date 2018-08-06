package ru.sbtqa.tag.apifactory.entries.apirequest;

import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Parameter;
import ru.sbtqa.tag.api.annotation.Vallidation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.parsers.core.exceptions.ParserException;

@Endpoint(method = HTTP.GET, path = "client/get-with-params?" + Default.PARAMETER_NAME1 + "=%param", title = "api request test")
public class ApiRequestEntry extends ApiEntry {

    @Parameter(name = "some name")
    private String param = Default.PARAMETER_VALUE1;

    @Header(name = Default.HEADER_NAME)
    private String header = Default.HEADER_VALUE;

    @Vallidation(title = "result")
    public void validate() {
//        JsonParser parser = new JsonParser();
//        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());
//
//        String expectedResult = name + param;
//        String actualResult = parser.read(response, "$.result");
//
//        Assert.assertEquals(expectedResult, actualResult);

        System.out.println("@Endpoint(method = HTTP.GET, path = \"client/get-with-params?\" + Default.PARAMETER_NAME1 + \"=%param\", name = \"api request test\")");
    }
}
