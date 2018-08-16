package ru.sbtqa.tag.api.entries.fromfeature;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.POST, path = "client/request-from-feature", title = "request from feature", template = "templates/Client.json")
public class RequestFromFeatureEntry extends EndpointEntry {

    @Body(name = "id")
    private String id = "0";

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(Default.QUERY_PARAMETER_VALUE_1 + Default.QUERY_PARAMETER_VALUE_2
                + Default.HEADER_PARAMETER_VALUE_1 + Default.HEADER_PARAMETER_VALUE_2
                + String.valueOf(Default.ID) + Default.NAME + Default.EMAIL));
    }
}
