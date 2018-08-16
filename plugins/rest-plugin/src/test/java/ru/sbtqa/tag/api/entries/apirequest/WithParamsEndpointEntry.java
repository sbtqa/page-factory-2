package ru.sbtqa.tag.api.entries.apirequest;

import cucumber.api.DataTable;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.GET, path = "client/get-with-params", title = "api request with params test")
public class WithParamsEndpointEntry extends EndpointEntry {

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    private String param;

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    private String header;

    @Validation(title = "result")
    public void validate(DataTable dataTable) {
        Map<String, String> expectedResultsTable =  dataTable.asMap(String.class, String.class);
        String expectedResult = expectedResultsTable.get(Default.QUERY_PARAMETER_NAME_1) + expectedResultsTable.get(Default.HEADER_PARAMETER_NAME_1);

        getResponse().body("result", equalTo(expectedResult));
    }
}
