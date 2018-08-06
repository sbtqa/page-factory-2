package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Vallidation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@Endpoint(method = HTTP.GET, path = "client/get", title = "get with json")
public class GetEntry extends ApiEntry {

    @Vallidation(title = "default client")
    public void validate() {
        getResponse().body("id", equalTo(Default.ID));
        getResponse().body("name", equalTo(Default.NAME));
        getResponse().body("email", equalTo(Default.EMAIL));
    }
}
