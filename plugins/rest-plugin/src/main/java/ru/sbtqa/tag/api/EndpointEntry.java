package ru.sbtqa.tag.api;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.api.repository.ApiPair;
import ru.sbtqa.tag.api.storage.BlankStorage;
import ru.sbtqa.tag.api.utils.PathUtils;
import ru.sbtqa.tag.api.utils.PlaceholderUtils;
import ru.sbtqa.tag.api.utils.TemplateUtils;

import static io.restassured.RestAssured.given;
import static ru.sbtqa.tag.api.annotation.ParameterType.BODY;
import static ru.sbtqa.tag.api.annotation.ParameterType.COOKIE;
import static ru.sbtqa.tag.api.annotation.ParameterType.HEADER;
import static ru.sbtqa.tag.api.annotation.ParameterType.QUERY;

/**
 * An endpoint request (ala Page Object).
 * <p>
 * It symbolizes the request for an api endpoint. You need to extends your entries from this class
 * and annotate with {@link Endpoint}
 */
public abstract class EndpointEntry {

    private static final ApiConfiguration PROPERTIES = ApiConfiguration.create();

    private EndpointEntryReflection reflection;
    private BlankStorage blankStorage;
    private Rest method;
    private String path;
    private String template;
    private String title;

    public EndpointEntry() {
        Endpoint endpoint = this.getClass().getAnnotation(Endpoint.class);
        method = endpoint.method();
        path = endpoint.path();
        template = endpoint.template();
        title = endpoint.title();

        reflection = new EndpointEntryReflection(this);
        blankStorage = ApiEnvironment.getBlankStorage();
    }

    public void send(Map<String, String> data) {
        for (Map.Entry<String, String> dataTableRow : data.entrySet()) {
            String key = dataTableRow.getKey();
            String value = dataTableRow.getValue();

            reflection.setParameterValueByTitle(key, value);
            reflection.replacePlaceholdersInParameterValue(key, value);
        }

        send();
    }

    public void send() {
        reflection.applyAnnotations();
        String url = PathUtils.unite(PROPERTIES.getBaseURI(), path);

        RequestSpecification request = buildRequest();
        Response response;
        switch (method) {
            case GET:
                response = request.get(url);
                break;
            case POST:
                response = request.post(url);
                break;
            case PUT:
                response = request.put(url);
                break;
            case PATCH:
                response = request.patch(url);
                break;
            case DELETE:
                response = request.delete(url);
                break;
            case OPTIONS:
                response = request.options(url);
                break;
            case HEAD:
                response = request.head(url);
                break;
            default:
                throw new UnsupportedOperationException("Request method " + method + " is not supported");
        }

        ApiEnvironment.getRepository().add(this.getClass(), new ApiPair(request, response.then().log().all(true)));
    }

    private RequestSpecification buildRequest() {
        RequestSpecification request = given().log().all(true);

        request.queryParams(getQueryParameters());
        request.headers(getHeaders());
        request.cookies(getCookies());

        if (!Rest.isBodiless(method) && !template.isEmpty()) {
            request.body(getBody());
        } else {
            request.formParams(getForm());
        }

        return request;
    }

    private Map<String, ?> getQueryParameters() {
        Map<String, Object> queries = new HashMap<>();

        queries.putAll(reflection.getParameters(QUERY));
        queries.putAll(blankStorage.get(title).getQueries());

        return queries;
    }

    private Map<String, ?> getHeaders() {
        Map<String, Object> headers = new HashMap<>();

        headers.putAll(reflection.getParameters(HEADER));
        headers.putAll(blankStorage.get(title).getHeaders());

        return headers;
    }

    private Map<String, ?> getCookies() {
        Map<String, Object> headers = new HashMap<>();

        headers.putAll(reflection.getParameters(COOKIE));
        headers.putAll(blankStorage.get(title).getCookies());

        return headers;
    }

    public String getBody() {
        String body = TemplateUtils.loadFromResources(this.getClass(), template, PROPERTIES.getTemplateEncoding());
        return PlaceholderUtils.replacePlaceholders(body, getParameters());
    }

    public Map<String, Object> getForm() {
        return getParameters();
    }

    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.putAll(reflection.getParameters(BODY));
        parameters.putAll(blankStorage.get(title).getBodies());

        return parameters;
    }

    public void validate(String title, Object... params) {
        reflection.validate(title, params);
    }

    public void validate(Object... params) {
        reflection.validate(params);
    }

    public ValidatableResponse getResponse() {
        return ApiEnvironment.getRepository().get(this.getClass()).getResponse();
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
