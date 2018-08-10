package ru.sbtqa.tag.api;

import cucumber.api.DataTable;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.api.repository.ApiPair;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.api.utils.ReflectionHelper;

public abstract class EndpointEntry {

    private static final ApiConfiguration properties = ConfigFactory.create(ApiConfiguration.class);

    private ReflectionHelper reflectionHelper;
    private HTTP requestMethod;
    private String requestPath;
    private String requestBodyTemplate;
    private String title;

    public EndpointEntry() {
        reflectionHelper = new ReflectionHelper(this);
        requestMethod = this.getClass().getAnnotation(Endpoint.class).method();
        requestPath = this.getClass().getAnnotation(Endpoint.class).path();
        requestBodyTemplate = this.getClass().getAnnotation(Endpoint.class).template();
        title = this.getClass().getAnnotation(Endpoint.class).title();
    }

    public void send(DataTable dataTable) {
        reflectionHelper.applyDatatable(dataTable);
        send();
    }

    /**
     * Perform api request. Consist of prepare step, fill parameters step, buildRequest
     * url and send request step
     *
     * @return response
     * @throws RestPluginException if there is an error in setters, prepare or send
     * methods
     */
    public void send() {
        String url = properties.getBaseURI() + "/" + requestPath;
        send(url);
    }

    /**
     * Perform action with request method to url. Override it if you need use
     * another rest or soap implementation
     *
     * @param url action target
     * @return response
     * @throws RestPluginException if response is not
     * an instance of bullet type
     */
    private void send(String url) {
        reflectionHelper.applyAnnotations();

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

        System.out.println("=============================RESPONSE");
        ApiEnvironment.getRepository().add(this.getClass(), new ApiPair(request, response.then().log().all()));
    }

    private RequestSpecification buildRequest() {
        System.out.println("=============================REQUEST");
        RequestSpecification request = given().log().all();

        request.queryParams(reflectionHelper.getParameters(ParameterType.QUERY));

        request.headers(reflectionHelper.getParameters(ParameterType.HEADER));

        if (!requestBodyTemplate.isEmpty()) {
            request.body(reflectionHelper.getBody(requestBodyTemplate));
        }

        return request;
    }

    public void validate(String title, Object... params) {
        reflectionHelper.validate(title, params);
    }

    public ValidatableResponse getResponse() {
        return ApiEnvironment.getRepository().get(this.getClass()).getResponse();
    }

    public String getTitle() {
        return title;
    }
}
