package ru.sbtqa.tag.apifactory.entries.apiaction.json;

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

@ApiAction(method = HTTP.PATCH, path = "client/patch", title = "patch test", template = "templates/Client.json")
public class PatchEntry extends ApiEntry {

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
        JsonParser parser = new JsonParser();
        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());

        String expectedResult = id + name + email;
        String actualResult = parser.read(response, "$.result");

        Assert.assertEquals(expectedResult, actualResult);
    }
}
