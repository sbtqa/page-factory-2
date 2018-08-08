package ru.sbtqa.tag.api.utils;

import java.lang.reflect.Field;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;

public class ReflectionUtils {

    public static Object get(ApiEntry apiEntry, Field field) {
        try {
            field.setAccessible(true);
            return field.get(apiEntry);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Body with name '" + field.getName() + "' is not available", ex);
        }

    }

    public static void set(ApiEntry apiEntry, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(apiEntry, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Body with name '" + field.getName() + "' is not available", ex);
        }
    }
}
