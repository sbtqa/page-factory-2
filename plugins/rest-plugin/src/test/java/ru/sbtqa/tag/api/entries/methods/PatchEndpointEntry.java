package ru.sbtqa.tag.api.entries.methods;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.PATCH, path = "client/patch", title = "patch test", template = "templates/Client.json")
public class PatchEndpointEntry extends EndpointEntry {

    @Header(name = "Content-Type")
    private final String header = "application/json";

    @Body(name = "id")
    private final int id = Default.ID;

    @Body(name = "name")
    private final String name = Default.NAME;

    @Body(name = "email")
    private final String email = Default.EMAIL;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(id + name + email));
    }
}
