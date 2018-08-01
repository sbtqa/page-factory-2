package ru.sbtqa.tag.api.entries.apiaction.json;

import org.junit.Assert;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.ApiFactory;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.parsers.JsonParser;
import ru.sbtqa.tag.parsers.core.exceptions.ParserException;

@ApiAction(method = HTTP.GET, path = "client/get", title = "get with json")
public class GetEntry extends ApiEntry {

    @ApiValidationRule(title = "default client")
    public void validate() throws ParserException {
        JsonParser parser = new JsonParser();

        String response = ApiFactory.getApiFactory().getResponseRepository().getBody(this.getClass());
        int id = parser.read(response, "$.id");
        String name = parser.read(response, "$.name");
        String email = parser.read(response, "$.email");

        Assert.assertEquals(Default.ID, id);
        Assert.assertEquals(Default.NAME, name);
        Assert.assertEquals(Default.EMAIL, email);
    }
}
