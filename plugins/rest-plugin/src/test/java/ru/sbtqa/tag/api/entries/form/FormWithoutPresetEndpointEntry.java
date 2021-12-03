package ru.sbtqa.tag.api.entries.form;

import io.cucumber.datatable.DataTable;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.CastUtils;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import static org.hamcrest.Matchers.equalTo;

@Endpoint(method = Rest.POST, path = "client/form", title = "form without preset")
public class FormWithoutPresetEndpointEntry extends EndpointEntry {

    @Body(name = "id")
    private int id;

    @Body(name = "name")
    private String name;

    @Body(name = "email")
    private String email;

    @Validation(title = "result with data")
    public void validateWithData(DataTable dataTable) {
        getResponse().body("result", equalTo(CastUtils.toMap(dataTable).get("result")));
    }
}
