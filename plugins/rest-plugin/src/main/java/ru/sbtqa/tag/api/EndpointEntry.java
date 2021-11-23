package ru.sbtqa.tag.api;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Mutator;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Stashed;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.api.repository.ApiPair;
import ru.sbtqa.tag.api.storage.BlankStorage;
import ru.sbtqa.tag.api.utils.PlaceholderUtils;
import ru.sbtqa.tag.api.utils.TemplateUtils;
import ru.sbtqa.tag.pagefactory.ApiEndpoint;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;
import ru.sbtqa.tag.pagefactory.utils.PathUtils;

import static io.restassured.RestAssured.given;
import static ru.sbtqa.tag.api.annotation.ParameterType.*;

/**
 * An endpoint request (ala Page Object).
 * <p>
 * It symbolizes the request for an api endpoint. You need to extends your entries from this class
 * and annotate with {@link Endpoint}
 */
public class EndpointEntry implements ApiEndpoint {

    private static final ApiConfiguration PROPERTIES = ApiConfiguration.create();

    private final EndpointEntryReflection reflection;
    private final BlankStorage blankStorage;
    private final Rest method;
    private String host;
    private String path;
    private final String template;
    private final String title;

    public EndpointEntry() {
        Endpoint endpoint = this.getClass().getAnnotation(Endpoint.class);
        method = endpoint.method();
        path = endpoint.path();
        host = endpoint.host();
        template = endpoint.template();
        title = endpoint.title();

        reflection = new EndpointEntryReflection(this);
        blankStorage = ApiEnvironment.getBlankStorage();
        reflection.applyAnnotations(FromResponse.class);
        reflection.applyAnnotations(Query.class);
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
        reflection.applyAnnotations(FromResponse.class);
        reflection.applyAnnotations(Query.class);
        reflection.applyAnnotations(Stashed.class);
        reflection.applyAnnotations(Mutator.class);

        String url = PathUtils.unite(host.isEmpty() ? PROPERTIES.getBaseURI() : PlaceholderUtils.replaceTemplatePlaceholders(host), path);

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
        if(template.endsWith(".json")) {
            System.out.println("template.endsWith(\".json\")");
            return PlaceholderUtils.replaceJsonTemplatePlaceholders(this.reflection.getEndpoint(), body, getParameters());
        } else {
            System.out.println("no template.endsWith(\".json\")");
            return PlaceholderUtils.replaceTemplatePlaceholders(this.reflection.getEndpoint(), body, getParameters());
        }
    }

    public Map<String, Object> getForm() {
        return getParameters();
    }

    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.putAll(reflection.getParameters(BODY));
        parameters.putAll(blankStorage.get(title).getBodies());
        System.out.println("QQQQQQQQ ");
        System.out.println(blankStorage.get(title));
        System.out.println(blankStorage.get(title).getBodies());
        System.out.println(parameters);
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

    @Override
    public String toString() {
        return getTitle();
    }
}
