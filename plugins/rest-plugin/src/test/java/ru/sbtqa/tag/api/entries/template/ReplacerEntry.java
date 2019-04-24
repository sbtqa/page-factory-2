package ru.sbtqa.tag.api.entries.template;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Validation;

@Endpoint(method = Rest.POST, path = "client/replace", title = "test replacer", template = "templates/Placeholders.json")
public class ReplacerEntry extends EndpointEntry {

    @Header(name = "Content-Type")
    private String header = "application/json";

    @Body(name = "day1")
    private String day1 = null;

    @Body(name = "day11")
    private String day11 = "true";

    @Body(name = "day12")
    private String day12;

    @Body(name = "day13")
    private String day13 = "true";

    @Body(name = "day14")
    private String day14 = "true";

    @Body(name = "day15")
    private String day15;

    @Validation(title = "works correctly")
    public void validate() {
        getResponse().body("result", equalTo("true"));
    }
}
