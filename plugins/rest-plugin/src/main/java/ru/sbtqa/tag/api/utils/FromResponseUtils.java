package ru.sbtqa.tag.api.utils;

import io.restassured.response.ValidatableResponse;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.exception.RestPluginException;

public class FromResponseUtils {

    public static Object getFromResponseValue(Class fromEndpoint, boolean isUsePrevious, String header, String path, String mask, boolean isNecessity) {
        ValidatableResponse response = getResponse(fromEndpoint, isUsePrevious);
        Object value = getValue(response, header, path, isNecessity);
        value = applyMask(value, mask);
        
        return value;
    }
    
    private static ValidatableResponse getResponse(Class fromEndpoint, boolean isUsePrevious) {
        if ((fromEndpoint == void.class || fromEndpoint == null) && isUsePrevious) {
            return ApiEnvironment.getRepository().getLast().getResponse();
        } else {
            return ApiEnvironment.getRepository().get(fromEndpoint).getResponse();
        }
    }

    private static Object getValue(ValidatableResponse response, String header, String path, boolean isNecessity) {
        if (!header.isEmpty()) {
            return response.extract().header(header);
        } else {
            if (response.extract().body().path(path) != null || isNecessity) {
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
            } else {
                throw new RestPluginException("Masking was failed because value is not instance of String");
            }
        } else {
            return value;
        }
    }
}
