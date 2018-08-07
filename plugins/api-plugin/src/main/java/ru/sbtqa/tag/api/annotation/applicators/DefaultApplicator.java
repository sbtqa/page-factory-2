package ru.sbtqa.tag.api.annotation.applicators;

import java.lang.reflect.Field;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;

public abstract class DefaultApplicator {

    ApiEntry entry;
    Field field;

    public DefaultApplicator(ApiEntry apiEntry, Field field) {
        this.entry = apiEntry;
        this.field = field;
    }

    protected Object get(Field field) {
        try {
            return field.get(entry);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with name '" + field.getName() + "' is not available", ex);
        }

    }

    protected void set(Field field, Object value) {
        try {
            field.set(entry, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with name '" + field.getName() + "' is not available", ex);
        }
    }
}
