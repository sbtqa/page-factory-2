package ru.sbtqa.tag.api.utils;

import static java.lang.String.format;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.exception.RestPluginException;

public class ReflectionUtils {

    private ReflectionUtils() {}

    public static Object get(EndpointEntry endpoint, Field field) {
        try {
            field.setAccessible(true);
            return field.get(endpoint);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RestPluginException(format("Body with name \"%s\" is not available", field.getName()), ex);
        }

    }

    public static void set(EndpointEntry endpoint, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(endpoint, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RestPluginException(format("Body with name \"%s\" is not available", field.getName()), ex);
        }
    }

    public static void invoke(Method method, EndpointEntry endpoint, Object... params) {
        try {
            method.invoke(endpoint, params);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            throw new RestPluginException(format("Failed to execute validation rule in \"%s\" endpoint entry", endpoint), e);
        }
    }
}
