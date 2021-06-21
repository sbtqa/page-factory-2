package ru.sbtqa.tag.api.entries.apirequest;

import io.cucumber.datatable.DataTable;
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

@Endpoint(method = Rest.GET, path = "client/get-with-params-placeholder", title = "api request with params test placeholders")
public class WithParamsPlaceholdersEndpointEntry extends EndpointEntry {

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    private final String query = "new-${parameter-1}";

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    private final String header = "[{\"value\":\"${parameter-2}\", \"visible\":true, \"name\":\"${parameter-3}\"}]";

    @Header(name = "header2")
    private String header2;

    @Validation(title = "result with datatable placeholders")
    public void validate(DataTable dataTable) {
        validate(toMap(dataTable));
    }

    @Validation(title = "result with map placeholders")
    public void validate(Map<String, String> data) {
        String expectedResult = data.get(Default.QUERY_PARAMETER_NAME_1) + data.get(Default.HEADER_PARAMETER_NAME_1) + data.get("header2");
        getResponse().body("result", equalTo(expectedResult));
    }
}
