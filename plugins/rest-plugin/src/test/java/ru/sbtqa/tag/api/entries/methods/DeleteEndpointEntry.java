package ru.sbtqa.tag.api.entries.methods;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.DELETE, path = "client/delete-client", title = "delete test")
public class DeleteEndpointEntry extends EndpointEntry {

    @Query(name = "client")
    private String queryParameter;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(queryParameter));
    }
}
