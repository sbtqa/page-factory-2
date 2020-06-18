package ru.sbtqa.tag.api.entries.apirequest;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.FinalSetter;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.GET, path = "client/get-with-params", title = "api request with final setter test")
public class ApiRequestWithFinalSetter extends EndpointEntry {

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    @FinalSetter(method = "makeUpperCase")
    private String param;

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    @FinalSetter(method = "addPrefixForHeader")
    private String header;

    public void addPrefixForHeader(String newValue) {
        this.header = HEADER_PREFIX + newValue;
    }

    public void makeUpperCase(String newValue) {
        this.param = newValue.toUpperCase();
    }

    public static final String HEADER_PREFIX = "Basic: ";


    @Validation(title = "result with final set values")
    public void validate(Map<String, String> data) {
        String expectedResult = data.get(Default.QUERY_PARAMETER_NAME_1) + data.get(Default.HEADER_PARAMETER_NAME_1);
        getResponse().body("result", equalTo(expectedResult));
    }
}
