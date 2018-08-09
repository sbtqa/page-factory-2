package ru.sbtqa.tag.apifactory.entries.apiaction.json;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.Entry;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.apifactory.utils.Default;

@ru.sbtqa.tag.api.annotation.Endpoint(method = HTTP.DELETE, path = "client/delete", title = "delete test")
public class DeleteEntry extends Entry {

    @Query(name = Default.PARAMETER_NAME1)
    private String queryParameter = Default.PARAMETER_VALUE1;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(queryParameter));
    }
}
