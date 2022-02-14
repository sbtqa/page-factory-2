package ru.sbtqa.tag.api.entries.template;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.POST, path = "client/arrays-as-is", title = "empty arrays", template = "templates/EmptyArrays.json", shouldRemoveEmptyObjects = true)
public class EmptyArraysEntry extends EndpointEntry {

    @Body(name = "first")
    private String first;

    @Validation(title = "result")
    public void validate() {
        String expected = "[ \"1\" , \"2\" , { \"milk\" : true , \"first\" : \"parameter\"}]";
        getResponse().body("result", equalTo(expected));
    }
}