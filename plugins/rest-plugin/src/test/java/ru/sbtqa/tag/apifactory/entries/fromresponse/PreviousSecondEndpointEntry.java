package ru.sbtqa.tag.apifactory.entries.fromresponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.HTTP;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.apifactory.utils.Default;

@Endpoint(method = HTTP.GET, path = "client/get", title = "dependent second")
public class PreviousSecondEndpointEntry extends EndpointEntry {

    @FromResponse(path = "email")
    private String emailFromPreviousRequest;

    @FromResponse(path = "email", endpointEntry = PreviousFirstEndpointEntry.class)
    private String emailFromSpecifiedRequest;

    @FromResponse(endpointEntry = PreviousFirstEndpointEntry.class, header = Default.HEADER_NAME)
    private String firstHeaderValue;

    @FromResponse(endpointEntry = PreviousFirstEndpointEntry.class, path = "nonexistent", necessity = false)
    private String nonexistent;

    @FromResponse(endpointEntry = PreviousFirstEndpointEntry.class, path = "email", mask = Default.MASK)
    private String maskedValue;

    @Validation(title = "result")
    public void validate() {
        Assert.assertEquals(Default.EMAIL, emailFromPreviousRequest);
        Assert.assertEquals(Default.EMAIL, emailFromSpecifiedRequest);
        Assert.assertEquals(Default.HEADER_VALUE, firstHeaderValue);
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
