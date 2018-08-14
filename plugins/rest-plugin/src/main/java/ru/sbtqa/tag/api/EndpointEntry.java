package ru.sbtqa.tag.api;

import cucumber.api.DataTable;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.api.repository.ApiPair;

/**
 * An endpoint request (ala Page Object).
 * <p>
 * It symbolizes the request for an api endpoint. You need to extends your entries from this class
 * and annotate with {@link ru.sbtqa.tag.api.annotation.Endpoint}
 */
public abstract class EndpointEntry {

    private static final ApiConfiguration PROPERTIES = ConfigFactory.create(ApiConfiguration.class);

    private EndpointEntryReflection reflection;
    private Rest method;
    private String path;
    private String template;
    private String title;

    public EndpointEntry() {
        reflection = new EndpointEntryReflection(this);

        Endpoint endpoint = this.getClass().getAnnotation(Endpoint.class);
        method = endpoint.method();
        path = endpoint.path();
        template = endpoint.template();
        title = endpoint.title();
    }

    public void send(DataTable dataTable) {
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            reflection.setParameterValueByTitle(dataTableRow.getKey(), dataTableRow.getValue());
        }

        send();
    }

    public void send() {
        String url = PROPERTIES.getBaseURI() + "/" + path;
        send(url);
    }

    private void send(String url) {
        reflection.applyAnnotations();

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

        ApiEnvironment.getRepository().add(this.getClass(), new ApiPair(request, response.then().log().all()));

    }

    private RequestSpecification buildRequest() {
        RequestSpecification request = given().log().all();
        request.queryParams(reflection.getParameters(ParameterType.QUERY));
        request.headers(reflection.getParameters(ParameterType.HEADER));
        if (!Rest.isBodiless(method)) {
            request.body(reflection.getBody(template));
        }
        
        return request;
    }

    public void validate(String title, Object... params) {
        reflection.validate(title, params);
    }

    public ValidatableResponse getResponse() {
        return ApiEnvironment.getRepository().get(this.getClass()).getResponse();
    }

    public String getTitle() {
        return title;
    }
}
