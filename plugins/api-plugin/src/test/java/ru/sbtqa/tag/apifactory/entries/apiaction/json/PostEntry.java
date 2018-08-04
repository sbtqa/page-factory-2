package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.Repository;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiRequestHeader;
import ru.sbtqa.tag.api.annotation.ApiRequestParam;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@ApiAction(method = HTTP.POST, path = "client/post", title = "post with json", template = "templates/Client.json")
public class PostEntry extends ApiEntry {

    @ApiRequestHeader(header = "Content-Type")
    private String header = "application/json";

    @ApiRequestParam(title = "id")
    private String id = String.valueOf(Default.ID);

    @ApiRequestParam(title = "name")
    private String name = Default.NAME;

    @ApiRequestParam(title = "email")
    private String email = Default.EMAIL;

    @ApiValidationRule(title = "result")
    public void validate() {
        System.out.println(
                Repository.get(this.getClass()).getResponse().then().extract().headers().asList()
        );
        System.out.println(
                Repository.get(this.getClass()).getResponse().then().extract().response().asString()
        );
    }
}
