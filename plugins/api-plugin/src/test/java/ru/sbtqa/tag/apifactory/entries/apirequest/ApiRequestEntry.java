package ru.sbtqa.tag.apifactory.entries.apirequest;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@Endpoint(method = HTTP.GET, path = "client/get-with-params", title = "api request test")
public class ApiRequestEntry extends ApiEntry {

    @Query(name = Default.PARAMETER_NAME1)
    private String param = Default.PARAMETER_VALUE1;

    @Header(name = Default.HEADER_NAME)
    private String header = Default.HEADER_VALUE;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(header + param));
    }
}
