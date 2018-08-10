package ru.sbtqa.tag.apifactory.entries.apirequest;

import cucumber.api.DataTable;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.rest.HTTP;

@ru.sbtqa.tag.api.annotation.Endpoint(method = HTTP.GET, path = "client/get-with-params", title = "api request with params test")
public class Endpoint extends EndpointEntry {

    @Query(name = "parameter-name-1")
    private String param;

    @Header(name = "header-name")
    private String header;

    @Validation(title = "result")
    public void validate(DataTable dataTable) {
        Map<String, String> expectedResultsTable =  dataTable.asMap(String.class, String.class);
        String expectedResult = expectedResultsTable.get("header-name") + expectedResultsTable.get("parameter-name-1");

        getResponse().body("result", equalTo(expectedResult));
    }
}
