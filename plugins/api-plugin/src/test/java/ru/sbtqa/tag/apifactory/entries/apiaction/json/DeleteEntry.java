package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import org.junit.Assert;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.ApiFactory;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiRequestParam;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.parsers.JsonParser;
import ru.sbtqa.tag.parsers.core.exceptions.ParserException;

@ApiAction(method = HTTP.DELETE, path = "client/delete?" + Default.PARAMETER_NAME1 + "=%param", title = "delete test")
public class DeleteEntry extends ApiEntry {

    @ApiRequestParam(title = "some title")
    private String param = Default.PARAMETER_VALUE1;

    @ApiValidationRule(title = "result")
    public void validate() throws ParserException {
        JsonParser parser = new JsonParser();
        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());

        String expectedResult = param;
        String actualResult = parser.read(response, "$.result");

        Assert.assertEquals(expectedResult, actualResult);
    }


}
