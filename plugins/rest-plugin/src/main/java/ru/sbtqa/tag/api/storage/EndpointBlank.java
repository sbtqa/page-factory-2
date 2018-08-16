package ru.sbtqa.tag.api.storage;

import java.util.HashMap;
import java.util.Map;

public class EndpointBlank {

    private String title;
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> queries = new HashMap<>();
    private Map<String, String> bodies = new HashMap<>();

    public EndpointBlank(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void addHeader(String name, Object value) {
        headers.put(name, value);
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
