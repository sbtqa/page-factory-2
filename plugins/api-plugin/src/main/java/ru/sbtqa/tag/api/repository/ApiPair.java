package ru.sbtqa.tag.api.repository;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ApiPair {

    private RequestSpecification request;
    private ValidatableResponse response;

    public ApiPair(RequestSpecification request, ValidatableResponse response) {
        this.request = request;
        this.response = response;
    }

    public RequestSpecification getRequest() {
        return request;
    }

    public ValidatableResponse getResponse() {
        return response;
    }
}
