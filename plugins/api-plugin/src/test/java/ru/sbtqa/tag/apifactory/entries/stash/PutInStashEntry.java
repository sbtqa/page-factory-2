package ru.sbtqa.tag.api.entries.stash;

import org.junit.Assert;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiRequestParam;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.annotation.PutInStash;
import ru.sbtqa.tag.api.annotation.strategies.By;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.datajack.Stash;

@ApiAction(method = HTTP.GET, path = "client/get", title = "put in stash")
public class PutInStashEntry extends ApiEntry {

    @ApiRequestParam(title = Default.PARAMETER_TITLE1)
    @PutInStash(by = By.NAME)
    private String stashByName = Default.PARAMETER_VALUE1;

    @ApiRequestParam(title = Default.PARAMETER_TITLE2)
    @PutInStash(by = By.TITLE)
    private String stashByTitle = Default.PARAMETER_VALUE2;

    @ApiValidationRule(title = "stash")
    public void validate() {
        String valueByName = Stash.getValue("stashByName");
        Assert.assertEquals(Default.PARAMETER_VALUE1, valueByName);

        String valueByTitle = Stash.getValue(Default.PARAMETER_TITLE2);
        Assert.assertEquals(Default.PARAMETER_VALUE2, valueByTitle);
    }
}
