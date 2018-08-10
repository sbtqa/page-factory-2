package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@ru.sbtqa.tag.api.annotation.Endpoint(method = HTTP.GET, path = "client/get", title = "get with json")
public class GetEndpointEntry extends EndpointEntry {

    @Validation(title = "default client")
    public void validate() {
        getResponse().body("id", equalTo(Default.ID));
        getResponse().body("name", equalTo(Default.NAME));
        getResponse().body("email", equalTo(Default.EMAIL));
    }
}
