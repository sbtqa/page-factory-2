package ru.sbtqa.tag.api.entries.parameters;

import static org.hamcrest.Matchers.equalTo;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Cookie;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.utils.Default;

@Endpoint(method = Rest.GET, path = "client/cookie", title = "cookie")
public class CookieEntry extends EndpointEntry {

    @Cookie(name = Default.COOKIE_NAME)
    private String cookie = Default.COOKIE_VALUE;

    @Validation(title = "result")
    public void validate() {
        getResponse().body("result", equalTo(cookie));
    }
}
