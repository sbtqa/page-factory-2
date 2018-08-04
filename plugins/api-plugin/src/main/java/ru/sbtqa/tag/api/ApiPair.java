package ru.sbtqa.tag.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiPair {

    private RequestSpecification request;
    private Response response;

    public ApiPair(RequestSpecification request, Response response) {
        this.request = request;
        this.response = response;
    }

    public RequestSpecification getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
