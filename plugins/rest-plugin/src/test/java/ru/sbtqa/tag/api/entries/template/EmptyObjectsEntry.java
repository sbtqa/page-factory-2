package ru.sbtqa.tag.api.entries.template;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.POST, path = "client/typed-arrays", title = "empty objects", template = "templates/EmptyObjects.json", isRemoveEmptyObjects = true)
public class EmptyObjectsEntry extends EndpointEntry {

    @Body(name = "first")
    private String first;

    @Validation(title = "result")
    public void validate() {
        String expected = "{ " +
                "\"first\" : \"parameter\" }";
        getResponse().body("result", equalTo(expected));
    }
}