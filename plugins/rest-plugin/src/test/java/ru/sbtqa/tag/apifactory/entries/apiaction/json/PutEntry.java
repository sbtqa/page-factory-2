package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.Entry;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@ru.sbtqa.tag.api.annotation.Endpoint(method = HTTP.PUT, path = "client/put", title = "put test", template = "templates/Client.json")
public class PutEntry extends Entry {

    @Header(name = "Content-Type")
    private String header = "application/json";

    @Body(name = "id")
    private String id = String.valueOf(Default.ID);

    @Body(name = "name")
    private String name = Default.NAME;

    @Body(name = "email")
    private String email = Default.EMAIL;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(id + name + email));
    }
}
