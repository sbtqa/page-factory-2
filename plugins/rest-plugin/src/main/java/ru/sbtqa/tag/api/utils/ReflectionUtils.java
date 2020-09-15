package ru.sbtqa.tag.api.utils;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.exception.RestPluginException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.String.format;

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

    public static Object invoke(Method method, EndpointEntry endpoint, Object... params) {
        try {
            return method.invoke(endpoint, params);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            if (method.getAnnotation(Validation.class) != null) {
                String ruleTitle = method.getAnnotation(Validation.class).title();
                Throwable directThrowable = e;
                if (directThrowable.getCause() != null) {
                    directThrowable = e.getCause();
                    if (directThrowable.getCause() != null) {
                        directThrowable = directThrowable.getCause();
                    }
                }
                throw new RestPluginException(format("Failed to execute validation rule \"%s\" in \"%s\" endpoint entry", ruleTitle, endpoint.getTitle()), directThrowable);
            } else throw new RestPluginException(format(
                "Problem with invoking method \"%s\" of class \"%s\" with \"%s\" type parameter",
                method.getName(), endpoint.getClass(), Arrays.toString(method.getParameterTypes())), e);
        }
    }
}
