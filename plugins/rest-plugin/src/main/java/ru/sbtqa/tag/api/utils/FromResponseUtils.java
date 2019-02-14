package ru.sbtqa.tag.api.utils;

import io.restassured.response.ValidatableResponse;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.exception.RestPluginException;

public class FromResponseUtils {

    private FromResponseUtils() {}

    public static Object getValueFromResponse(Class fromEndpoint, String header, String path, String mask, boolean isOptional) {
        ValidatableResponse response = getResponse(fromEndpoint);
        Object value = getValue(response, header, path, isOptional);
        value = applyMask(value, mask);

        return value;
    }

    private static ValidatableResponse getResponse(Class fromEndpoint) {
        if (fromEndpoint == void.class || fromEndpoint == null) {
            return ApiEnvironment.getRepository().getLast().getResponse();
        } else {
            return ApiEnvironment.getRepository().get(fromEndpoint).getResponse();
        }
    }

    private static Object getValue(ValidatableResponse response, String header, String path, boolean isOptional) {
        if (!header.isEmpty()) {
            return response.extract().header(header);
        } else {
            if (response.extract().body().path(path) != null || !isOptional) {
                return response.extract().body().path(path).toString();
            } else {
                return null;
            }
        }
    }

    private static Object applyMask(Object value, String mask) {
        if (!mask.isEmpty()) {
            if (value instanceof String) {
                return RegexUtils.getFirstMatcherGroup((String) value, mask);
            } else if (value == null) {
                throw new RestPluginException("Can't masking null value");
            } else {
                throw new RestPluginException("Masking was failed because value is not instance of String");
            }
        } else {
            return value;
        }
    }
}
