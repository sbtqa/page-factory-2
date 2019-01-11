package ru.sbtqa.tag.api.annotation.applicators;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.utils.ReflectionUtils;

import java.lang.reflect.Field;

public abstract class DefaultApplicator implements Applicator {

    EndpointEntry endpoint;
    Field field;

    public DefaultApplicator(EndpointEntry endpoint, Field field) {
        this.endpoint = endpoint;
        this.field = field;
    }

    protected Object get(Field field) {
        return ReflectionUtils.get(endpoint, field);
    }

    protected void set(Field field, Object value) {
        ReflectionUtils.set(endpoint, field, value);
    }
}
