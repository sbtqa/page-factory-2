package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiRequestHeader;
import ru.sbtqa.tag.api.annotation.ApiRequestParam;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.parsers.core.exceptions.ParserException;

@ApiAction(method = HTTP.PUT, path = "client/put", title = "put test", template = "templates/Client.json")
public class PutEntry extends ApiEntry {

    @ApiRequestHeader(header = "Content-Type")
    private String header = "application/json";

    @ApiRequestParam(title = "id")
    private String id = String.valueOf(Default.ID);

    @ApiRequestParam(title = "name")
    private String name = Default.NAME;

    @ApiRequestParam(title = "email")
    private String email = Default.EMAIL;

    @ApiValidationRule(title = "result")
    public void validate() throws ParserException {
//        JsonParser parser = new JsonParser();
//        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());
//
//        String expectedResult = id + name + email;
//        String actualResult = parser.read(response, "$.result");
//
//        Assert.assertEquals(expectedResult, actualResult);

        System.out.println("@ApiAction(method = HTTP.PUT, path = \"client/put\", title = \"put test\", template = \"templates/Client.json\")");
    }
}
