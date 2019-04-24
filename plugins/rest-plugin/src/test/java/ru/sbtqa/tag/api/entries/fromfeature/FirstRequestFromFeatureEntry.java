package ru.sbtqa.tag.api.entries.fromfeature;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.POST, path = "client/request-from-feature", title = "first request from feature", template = "templates/Client.json")
public class FirstRequestFromFeatureEntry extends EndpointEntry {

    @Body(name = "id")
    private int id = 0;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo("q1=" + Default.QUERY_PARAMETER_VALUE_1 + "|\n" + "q2=" + Default.QUERY_PARAMETER_VALUE_2
                + "|\n" + "h1=" + Default.HEADER_PARAMETER_VALUE_1 + "|\n" + "h2=" + Default.HEADER_PARAMETER_VALUE_2 + "|\n"
                + "id=" + Default.ID + "|\n" + "name=" + Default.NAME + "|\n" + "email=" + Default.EMAIL + "|\n"));
    }
}
