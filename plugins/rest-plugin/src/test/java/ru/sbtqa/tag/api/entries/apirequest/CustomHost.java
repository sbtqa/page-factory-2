package ru.sbtqa.tag.api.entries.apirequest;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.GET, host = "${env.customHost}", path = "/random/postcodes", title = "custom host")
public class CustomHost extends EndpointEntry {

    @Validation(title = "result")
    public void validate() {
        getResponse().body("status", equalTo(200));
    }
}