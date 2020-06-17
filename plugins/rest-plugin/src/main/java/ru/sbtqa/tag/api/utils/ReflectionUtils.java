package ru.sbtqa.tag.api.utils;

import org.apache.commons.lang3.StringUtils;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.exception.RestPluginException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            Method setter = getSetter(endpoint, field);
            if (setter != null) {
                setter.setAccessible(true);
                setter.invoke(endpoint, value);
            } else {
                field.setAccessible(true);
                field.set(endpoint, value);
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            throw new RestPluginException(format("Body with name \"%s\" is not available", field.getName()), ex);
        }
    }

    private static Method getSetter(EndpointEntry endpoint, Field field) {
        try {
            return endpoint.getClass().getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static void invoke(Method method, EndpointEntry endpoint, Object... params) {
        try {
            method.invoke(endpoint, params);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            String ruleTitle = method.getAnnotation(Validation.class).title();
            Throwable directThrowable = e;
            if (directThrowable.getCause() != null) {
                directThrowable = e.getCause();
                if (directThrowable.getCause() != null) {
                    directThrowable = directThrowable.getCause();
                }
            }
            throw new RestPluginException(format("Failed to execute validation rule \"%s\" in \"%s\" endpoint entry", ruleTitle, endpoint.getTitle()), directThrowable);
        }
    }
}
