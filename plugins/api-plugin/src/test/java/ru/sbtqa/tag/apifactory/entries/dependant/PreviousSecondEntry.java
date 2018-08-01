package ru.sbtqa.tag.apifactory.entries.dependant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import ru.sbtqa.tag.apifactory.ApiEntry;
import ru.sbtqa.tag.apifactory.annotation.ApiAction;
import ru.sbtqa.tag.apifactory.annotation.DependentResponseParam;
import ru.sbtqa.tag.apifactory.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@ApiAction(method = HTTP.GET, path = "client/get", title = "dependent second")
public class PreviousSecondEntry extends ApiEntry {

    @DependentResponseParam(path = "$.email")
    private String emailFromPreviousRequest;

    @DependentResponseParam(path = "$.email", responseEntry = PreviousFirstEntry.class)
    private String emailFromSpecifiedRequest;

    @DependentResponseParam(responseEntry = PreviousFirstEntry.class, header = Default.HEADER_NAME)
    private String firstHeaderValue;

    @DependentResponseParam(responseEntry = PreviousFirstEntry.class, path = "$.nonexistent", necessity = false)
    private String nonexistent;

    @DependentResponseParam(responseEntry = PreviousFirstEntry.class, path = "$.email", mask = Default.MASK)
    private String maskedValue;

    @Override
    public void prepare() {
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
