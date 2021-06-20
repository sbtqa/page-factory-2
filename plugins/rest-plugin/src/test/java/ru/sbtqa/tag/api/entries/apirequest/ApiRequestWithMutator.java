package ru.sbtqa.tag.api.entries.apirequest;

import io.cucumber.datatable.DataTable;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Mutator;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.api.utils.DefaultMutators;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static ru.sbtqa.tag.api.utils.CastUtils.toMap;

@Endpoint(method = Rest.GET, path = "client/get-with-params", title = "api request with mutator test")
public class ApiRequestWithMutator extends EndpointEntry {

    private static final String NOT_NULL_STRING = "not null string";

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    @Mutator(clazz = DefaultMutators.class, method = "toUpperCase")
    private final String param = "lowercaseparam";

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    @Mutator(method = "doSmthWithNull")
    private final String header = null;

    public String doSmthWithNull(String value) {
        return value == null ? NOT_NULL_STRING : value;
    }

    @Validation(title = "result with mutated values")
    public void validate(DataTable parametersTable) {
        Map<String, String> parameters = toMap(parametersTable);
        getResponse().body("result", equalTo(parameters.get("query-parameter-name-1") + parameters.get("header-parameter-name-1")));
    }
}
