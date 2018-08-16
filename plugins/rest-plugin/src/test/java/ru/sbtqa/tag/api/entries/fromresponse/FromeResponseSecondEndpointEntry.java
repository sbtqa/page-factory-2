package ru.sbtqa.tag.api.entries.fromresponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.GET, path = "client/get", title = "from response second")
public class FromeResponseSecondEndpointEntry extends EndpointEntry {

    @FromResponse(path = "email")
    private String emailFromPreviousRequest;

    @FromResponse(path = "email", endpointEntry = FromResponseFirstEndpointEntry.class)
    private String emailFromSpecifiedRequest;

    @FromResponse(endpointEntry = FromResponseFirstEndpointEntry.class, header = Default.HEADER_PARAMETER_NAME_1)
    private String firstHeaderValue;

    @FromResponse(endpointEntry = FromResponseFirstEndpointEntry.class, path = "nonexistent", necessity = false)
    private String nonexistent;

    @FromResponse(endpointEntry = FromResponseFirstEndpointEntry.class, path = "email", mask = Default.MASK)
    private String maskedValue;

    @Validation(title = "result")
    public void validate() {
        Assert.assertEquals(Default.EMAIL, emailFromPreviousRequest);
        Assert.assertEquals(Default.EMAIL, emailFromSpecifiedRequest);
        Assert.assertEquals(Default.HEADER_PARAMETER_VALUE_1, firstHeaderValue);
        Assert.assertEquals(null, nonexistent);

        String expectedMaskedValue = getMaskedValue(Default.EMAIL, Default.MASK);
        Assert.assertEquals(expectedMaskedValue, maskedValue);
    }

    private String getMaskedValue(String original, String mask) {
        Matcher matcher = Pattern.compile(Default.MASK).matcher(Default.EMAIL);

        String expectedMaskedValue = "";
        if (matcher.find()) {
            expectedMaskedValue = matcher.group(1);
        }

        return expectedMaskedValue;
    }
}
