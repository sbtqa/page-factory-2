package ru.sbtqa.tag.api.entries.apirequest;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.GET, path = "client/get-with-params", title = "api request test")
public class RequestEndpointEntry extends EndpointEntry {

    @Query(name = Default.PARAMETER_NAME1)
    private String param = Default.PARAMETER_VALUE1;

    @Header(name = Default.HEADER_NAME)
    private String header = Default.HEADER_VALUE;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(header + param));
    }
}
