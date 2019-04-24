package ru.sbtqa.tag.api.entries.apirequest;

import cucumber.api.DataTable;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import static ru.sbtqa.tag.api.utils.CastUtils.toMap;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.GET, path = "client/get-with-params", title = "api request with params test")
public class WithParamsEndpointEntry extends EndpointEntry {

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    private String param;

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    private String header;

    @Validation(title = "result with datatable")
    public void validate(DataTable dataTable) {
        validate(toMap(dataTable));
    }

    @Validation(title = "result with map")
    public void validate(Map<String, String> data) {
        String expectedResult = data.get(Default.QUERY_PARAMETER_NAME_1) + data.get(Default.HEADER_PARAMETER_NAME_1);
        getResponse().body("result", equalTo(expectedResult));
    }
}
