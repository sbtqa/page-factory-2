package ru.sbtqa.tag.apifactory.entries.aipurl;

import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@Endpoint(method = HTTP.GET, path = "client/get-with-params", title = "api url test")
public class ApiUrlEntry extends ApiEntry {

    //TODO queryParam
//    @ApiUrlParam(name = Default.PARAMETER_NAME1)
    private String param = Default.PARAMETER_VALUE1;

    @Header(name = Default.HEADER_NAME)
    private String header = Default.HEADER_VALUE;

    @Validation(title = "result")
    public void validate() {
//        JsonParser parser = new JsonParser();
//        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());
//
//        String expectedResult = name + param;
//        String actualResult = parser.read(response, "$.result");
//
//        Assert.assertEquals(expectedResult, actualResult);

        System.out.println("\n" +
                "@Endpoint(method = HTTP.GET, path = \"client/get-with-params\", name = \"api url test\")");
    }


}
