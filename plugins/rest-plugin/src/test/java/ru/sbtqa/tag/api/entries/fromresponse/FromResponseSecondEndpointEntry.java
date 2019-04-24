package ru.sbtqa.tag.api.entries.fromresponse;

import org.junit.Assert;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.api.utils.RegexUtils;

@Endpoint(method = Rest.GET, path = "client/get", title = "from response second")
public class FromResponseSecondEndpointEntry extends EndpointEntry {

    @FromResponse(path = "email")
    private String emailFromPreviousRequest;

    @FromResponse(endpoint = FromResponseFirstEndpointEntry.class, path = "email")
    private String emailFromSpecifiedRequest;

    @FromResponse(endpoint = FromResponseFirstEndpointEntry.class, header = Default.HEADER_PARAMETER_NAME_1)
    private String firstHeaderValue;

    @FromResponse(path = "nonexistent", optional = true)
    private String nonexistent;

    @FromResponse(endpoint = FromResponseFirstEndpointEntry.class, path = "email", mask = Default.MASK)
    private String maskedValue;

    @Validation(title = "result")
    public void validate() {
        Assert.assertEquals(Default.EMAIL, emailFromPreviousRequest);
        Assert.assertEquals(Default.EMAIL, emailFromSpecifiedRequest);
        Assert.assertEquals(Default.HEADER_PARAMETER_VALUE_1, firstHeaderValue);
        Assert.assertNull(nonexistent);

        String expectedMaskedValue = RegexUtils.getFirstMatcherGroup(Default.EMAIL, Default.MASK);
        Assert.assertEquals(expectedMaskedValue, maskedValue);
    }
}
