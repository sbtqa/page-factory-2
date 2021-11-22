package ru.sbtqa.tag.api.entries.template;

import ru.sbtqa.tag.api.BodyArray;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.POST, path = "client/typed-arrays", title = "typed arrays", template = "templates/TypedArrays.json")
public class TypedArraysEntry extends EndpointEntry {
    @Body(name = "valuesString")
    private BodyArray<String> valuesString;

    @Body(name = "valuesInt")
    private BodyArray<Integer> valuesInt;

    @Body(name = "valuesBoolean")
    private BodyArray<Boolean> valuesBoolean;


    @Validation(title = "result")
    public void validate() {
        String expected = "{ " +
                "\"channel\" : \"test-channel\", " +
                "\"requestId\" : \"1\", " +
                "\"requisites\" : [" +
                "{ \"path\" : \"home\", " +
                "\"valuesString\" : [" +
                "\"\\\"should be quoted\\\"\", \",one\", \"two\", \"three\", \"four\"], " +
                "\"valuesInt\" : [1, 2, 3, 4, 5], " +
                "\"valuesBoolean\" : [true, false, true, true, true] " +
                "}] }";
        getResponse().body("result", equalTo(expected));
    }
}
