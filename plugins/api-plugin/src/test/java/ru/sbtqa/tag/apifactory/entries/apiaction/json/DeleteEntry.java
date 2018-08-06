package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Vallidation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@Endpoint(method = HTTP.DELETE, path = "client/delete", title = "delete test")
public class DeleteEntry extends ApiEntry {

    @Query(name = Default.PARAMETER_NAME1)
    private String queryParameter = Default.PARAMETER_VALUE1;

    @Vallidation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(queryParameter));
    }
}
