package ru.sbtqa.tag.api.entries.apiaction.json;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.GET, path = "client/get", title = "get with json")
public class GetEndpointEntry extends EndpointEntry {

    @Validation(title = "default client")
    public void validate() {
        getResponse().body("id", equalTo(Default.ID));
        getResponse().body("name", equalTo(Default.NAME));
        getResponse().body("email", equalTo(Default.EMAIL));
    }
}
