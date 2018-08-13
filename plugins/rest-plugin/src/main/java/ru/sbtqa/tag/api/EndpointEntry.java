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

public abstract class EndpointEntry {

    private static final ApiConfiguration PROPERTIES = ConfigFactory.create(ApiConfiguration.class);

    private EndpointEntryReflection reflection;
    private HTTP requestMethod;
    private String requestPath;
    private String requestBodyTemplate;
    private String title;

    public EndpointEntry() {
        reflection = new EndpointEntryReflection(this);
        requestMethod = this.getClass().getAnnotation(Endpoint.class).method();
        requestPath = this.getClass().getAnnotation(Endpoint.class).path();
        requestBodyTemplate = this.getClass().getAnnotation(Endpoint.class).template();
        title = this.getClass().getAnnotation(Endpoint.class).title();
    }

    public void send(DataTable dataTable) {
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            reflection.setParameterValueByTitle(dataTableRow.getKey(), dataTableRow.getValue());
        }

        send();
    }

    public void send() {
        String url = PROPERTIES.getBaseURI() + "/" + requestPath;
        send(url);
    }

    private void send(String url) {
        reflection.applyAnnotations();

        RequestSpecification request = buildRequest();
        Response response;
        switch (requestMethod) {
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
                throw new UnsupportedOperationException("Request method " + requestMethod + " is not support");
        }

        ApiEnvironment.getRepository().add(this.getClass(), new ApiPair(request, response.then().log().all()));

    }

    private RequestSpecification buildRequest() {
        RequestSpecification request = given().log().all();

        request.queryParams(reflection.getParameters(ParameterType.QUERY));

        request.headers(reflection.getParameters(ParameterType.HEADER));

        if (!HTTP.isBodiless(requestMethod)) {
            request.body(reflection.getBody(requestBodyTemplate));
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
