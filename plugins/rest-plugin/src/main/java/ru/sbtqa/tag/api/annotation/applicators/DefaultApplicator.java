package ru.sbtqa.tag.api.annotation.applicators;

import java.lang.reflect.Field;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.utils.ReflectionUtils;

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
