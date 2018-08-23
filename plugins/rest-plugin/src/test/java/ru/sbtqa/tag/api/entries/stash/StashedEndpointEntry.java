package ru.sbtqa.tag.api.entries.stash;

import org.junit.Assert;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Stashed;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.annotation.strategies.By;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.datajack.Stash;

@Endpoint(method = Rest.GET, path = "client/get", title = "put in stash")
public class StashedEndpointEntry extends EndpointEntry {

    @Stashed(by = By.NAME)
    private String stashByName = Default.STASHED_PARAMETER_VALUE_1;

    @Stashed(by = By.TITLE, title = Default.STASHED_PARAMETER_NAME_2)
    private String stashByTitle = Default.STASHED_PARAMETER_VALUE_2;

    @Validation(title = "stash")
    public void validate() {
        String valueByName = Stash.getValue("stashByName");
        Assert.assertEquals(Default.STASHED_PARAMETER_VALUE_1, valueByName);

        String valueByTitle = Stash.getValue(Default.STASHED_PARAMETER_NAME_2);
        Assert.assertEquals(Default.STASHED_PARAMETER_VALUE_2, valueByTitle);
    }
}
