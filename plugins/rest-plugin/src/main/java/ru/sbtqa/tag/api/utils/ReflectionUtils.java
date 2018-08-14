package ru.sbtqa.tag.api.utils;

import java.lang.reflect.Field;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.exception.RestPluginException;

public class ReflectionUtils {

    private ReflectionUtils() {}

    public static Object get(EndpointEntry endpoint, Field field) {
        try {
            field.setAccessible(true);
            return field.get(endpoint);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RestPluginException("Body with name '" + field.getName() + "' is not available", ex);
        }

    }

    public static void set(EndpointEntry endpoint, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(endpoint, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RestPluginException("Body with name '" + field.getName() + "' is not available", ex);
        }
    }
}
