package ru.sbtqa.tag.api.entries.apirequest;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Mutator;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.api.utils.DefaultMutators;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.core.IsEqual.equalTo;

@Endpoint(method = Rest.GET, path = "client/get-with-params", title = "api request with mutator test")
public class ApiRequestWithMutator extends EndpointEntry {

    private static final String NOT_NULL_STRING = "not null string";

    @Query(name = Default.QUERY_PARAMETER_NAME_1)
    @Mutator(clazz = DefaultMutators.class, method = "toUpperCase")
    private String param = "lowercaseparam";

    @Header(name = Default.HEADER_PARAMETER_NAME_1)
    @Mutator(method = "doSmthWithNull")
    private String header = null;

    public String doSmthWithNull(String value) {
        return value == null ? NOT_NULL_STRING : value;
    }

    @Validation(title = "result with mutated values")
    public void validate() {
        getResponse().body("result", equalTo("LOWERCASEPARAM" + NOT_NULL_STRING));
    }
}
