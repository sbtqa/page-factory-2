package ru.sbtqa.tag.apifactory.entries.stash;

import org.junit.Assert;
import ru.sbtqa.tag.api.Entry;
import ru.sbtqa.tag.api.annotation.Stashed;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.annotation.strategies.By;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.datajack.Stash;

@ru.sbtqa.tag.api.annotation.Endpoint(method = HTTP.GET, path = "client/get", title = "put in stash")
public class PutInStashEntry extends Entry {

    @Stashed(by = By.NAME)
    private String stashByName = Default.PARAMETER_VALUE1;

    @Stashed(by = By.TITLE, title = Default.PARAMETER_TITLE2)
    private String stashByTitle = Default.PARAMETER_VALUE2;

    @Validation(title = "stash")
    public void validate() {
        String valueByName = Stash.getValue("stashByName");
        Assert.assertEquals(Default.PARAMETER_VALUE1, valueByName);

        String valueByTitle = Stash.getValue(Default.PARAMETER_TITLE2);
        Assert.assertEquals(Default.PARAMETER_VALUE2, valueByTitle);
    }
}
