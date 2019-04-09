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

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    private String param = Default.QUERY_PARAMETER_VALUE_1;

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    private String header;

    public RequestEndpointEntry(){
        super();
        header = Default.HEADER_PARAMETER_VALUE_1;
    }

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(header + param));
    }
}
