package ru.sbtqa.tag.api.entries.methods;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.DELETE, path = "client/delete", title = "delete test")
public class DeleteEndpointEntry extends EndpointEntry {

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    private String queryParameter = Default.QUERY_PARAMETER_VALUE_1;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(queryParameter));
    }
}
