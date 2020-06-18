package ru.sbtqa.tag.api.entries.fromresponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.FinalSetter;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

@Endpoint(method = Rest.GET, path = "client/get", title = "from response with setter")
public class FromResponseWithSetterEntry extends EndpointEntry {


    @FromResponse(endpoint = FromResponseFirstEndpointEntry.class, path = "email")
    @FinalSetter(method = "makeEmailUpperCase")
    private String emailFromSpecifiedRequest;


    @FromResponse(endpoint = FromResponseFirstEndpointEntry.class, header = Default.HEADER_PARAMETER_NAME_1)
    @FinalSetter(method = "addPrefixForHeader")
    private String firstHeaderValue;

    final String headerPrefix = "headerPrefix";

    public void makeEmailUpperCase(String emailFromSpecifiedRequest) {
        this.emailFromSpecifiedRequest = StringUtils.upperCase(emailFromSpecifiedRequest);
    }

    public void addPrefixForHeader(String firstHeaderValue) {
        this.firstHeaderValue = headerPrefix + firstHeaderValue;
    }

    @Validation(title = "result")
    public void validate() {
        Assert.assertEquals(Default.EMAIL.toUpperCase(), emailFromSpecifiedRequest);
        Assert.assertEquals(headerPrefix + Default.HEADER_PARAMETER_VALUE_1, firstHeaderValue);
    }
}
