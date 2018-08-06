package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Parameter;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@Endpoint(method = HTTP.POST, path = "client/post", title = "post with json", template = "templates/Client.json")
public class PostEntry extends ApiEntry {

    @Header(name = "Content-Type")
    private String header = "application/json";

    @Parameter(name = "id")
    private String id = String.valueOf(Default.ID);

    @Parameter(name = "name")
    private String name = Default.NAME;

    @Parameter(name = "email")
    private String email = Default.EMAIL;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(id + name + email));
    }
}
