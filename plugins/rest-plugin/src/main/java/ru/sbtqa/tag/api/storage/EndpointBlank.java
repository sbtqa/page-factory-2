package ru.sbtqa.tag.api.storage;

import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * The blank of the request.
 * This blank filled from features with steps like "user add a header parameter with name "name" and value "value""
 * and stored in {@link BlankStorage}. EndpointBlank.title is matched with @Endpoint.title.
 * Before sending @Endpoint request this endppoint blank (header, queries, body params) applies to the request parameters
 */
public class EndpointBlank {

    private final String title;
    private final Map<String, Object> headers = new HashMap<>();
    private final Map<String, Object> cookies = new HashMap<>();
    private final Map<String, Object> queries = new HashMap<>();
    private final Map<String, String> bodies = new HashMap<>();

    public EndpointBlank(String title) {
        this.title = title;
    }

    public EndpointBlank(Class endpoint) {
        this.title = ((Endpoint) endpoint.getAnnotation(Endpoint.class)).title();
    }

    public String getTitle() {
        return title;
    }

    public void addParameter(ParameterType type, String name, Object valueObj) {
        String value = String.valueOf(valueObj);

        if (value != null && !value.equals(""))
            switch (type) {
                case QUERY:
                    addQuery(name, value);
                    break;
                case HEADER:
                    addHeader(name, value);
                    break;
                case BODY:
                    addBodyParameter(name, value);
                    break;
                case COOKIE:
                    addCookie(name, value);
                    break;
            }
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void addHeader(String name, Object value) {
        headers.put(name, value);
    }

    public Map<String, Object> getCookies() {
        return cookies;
    }

    private void addCookie(String name, String value) {
        cookies.put(name, value);
    }

    public Map<String, Object> getQueries() {
        return queries;
    }

    public void addQuery(String name, String value) {
        queries.put(name, value);
    }

    public Map<String, String> getBodies() {
        return bodies;
    }

    public void addBodyParameter(String name, String value) {
        bodies.put(name, value);
    }
}
