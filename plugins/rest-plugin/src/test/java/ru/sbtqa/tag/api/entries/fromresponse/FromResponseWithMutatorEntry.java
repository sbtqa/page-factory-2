package ru.sbtqa.tag.api.entries.fromresponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Mutator;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

@Endpoint(method = Rest.GET, path = "client/get", title = "from response with mutator")
public class FromResponseWithMutatorEntry extends EndpointEntry {

    private static final String HEADER_PREFIX = "headerPrefix";

    @FromResponse(endpoint = FromResponseFirstEndpointEntry.class, path = "email")
    @Mutator(method = "toUpperCase")
    private String emailFromSpecifiedRequest;

    @FromResponse(endpoint = FromResponseFirstEndpointEntry.class, header = Default.HEADER_PARAMETER_NAME_1)
    @Mutator(method = "addPrefix")
    private String firstHeaderValue;

    public String toUpperCase(String value) {
        return StringUtils.upperCase(value);
    }

    public String addPrefix(String value) {
        return HEADER_PREFIX + value;
    }

    @Validation(title = "result")
    public void validate() {
        Assert.assertEquals(Default.EMAIL.toUpperCase(), emailFromSpecifiedRequest);
        Assert.assertEquals(HEADER_PREFIX + Default.HEADER_PARAMETER_VALUE_1, firstHeaderValue);
    }
}
