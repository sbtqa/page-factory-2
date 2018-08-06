package ru.sbtqa.tag.api;

import cucumber.api.DataTable;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.exception.ApiException;
import ru.sbtqa.tag.api.rest.HTTP;

/**
 * Api object (ala Page object). Request to definite url with a set of
 * parameters such as request method, parameters, response validation.
 */
public abstract class ApiEntry {

    private static final ApiConfiguration properties = ConfigFactory.create(ApiConfiguration.class);

    private ReflectionHelper reflectionHelper;
    private HTTP requestMethod;
    private String requestPath;
    private String requestBodyTemplate;

    public ApiEntry() {
        reflectionHelper = new ReflectionHelper(this);
        requestMethod = this.getClass().getAnnotation(Endpoint.class).method();
        requestPath = this.getClass().getAnnotation(Endpoint.class).path();
        requestBodyTemplate = this.getClass().getAnnotation(Endpoint.class).template();
    }

    /**
     * Perform api request. Consist of prepare step, fill parameters step, buildRequest
     * url and send request step
     *
     * @return response
     * @throws ApiException if there is an error in setters, prepare or send
     * methods
     */
    public void send() {
        reflectionHelper.setDependentResponseParameters();
        reflectionHelper.applyParametersAnnotation();

        String url = properties.getBaseURI() + "/" + requestPath;

        send(url);
    }

    public void send(DataTable dataTable) {
        reflectionHelper.applyDatatable(dataTable);
        send();
    }

    /**
     * Perform action with request method to url. Override it if you need use
     * another rest or soap implementation
     *
     * @param url action target
     * @return response
     * @throws ApiException if response is not
     * an instance of bullet type
     */
    private void send(String url) {
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

        request.queryParams(reflectionHelper.getQueryParams());

        request.headers(reflectionHelper.getHeaders());

        if (!requestBodyTemplate.isEmpty()) {
            request.body(reflectionHelper.getBody(requestBodyTemplate));
        }

        return request;
    }

    /**
     * Perform action validation rule
     *
     * @param title a {@link java.lang.String} object.
     * @param params a {@link java.lang.Object} object.
     * @throws ApiException if can't invoke
     * method
     */
    public void validate(String title, Object... params) {
        reflectionHelper.validate(title, params);
    }

    public ValidatableResponse getResponse() {
        return ApiEnvironment.getRepository().get(this.getClass()).getResponse();
    }
}
