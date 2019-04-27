package ru.sbtqa.tag.api.entries.apirequest;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.GET, path = "client/${client}", title = "placeholder api request")
public class PlaceholderRequestEndpointEntry extends EndpointEntry {

    @Query(name = "client")
    private String client;

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    private String param = Default.QUERY_PARAMETER_VALUE_1;

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    private String header;

    public PlaceholderRequestEndpointEntry(){
        super();
        header = Default.HEADER_PARAMETER_VALUE_1;
    }

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(param+header));
    }
}
