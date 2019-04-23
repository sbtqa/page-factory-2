package ru.sbtqa.tag.api.entries.methods;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.MediaType.JSON_UTF_8;
import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.POST, path = "client/post", title = "post with json", template = "templates/Client.json")
public class PostEndpointEntry extends EndpointEntry {

    @Header(name = CONTENT_TYPE)
    private String header;

    @Body(name = "id")
    private int id = Default.ID;

    @Body(name = "name")
    private String name = "";

    @Body(name = "email")
    private String email;

    public PostEndpointEntry() {
        super();
        email = Default.EMAIL;
        header = JSON_UTF_8.toString();
    }

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(id + name + email));
    }
}
