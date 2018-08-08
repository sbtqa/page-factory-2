package ru.sbtqa.tag.api.annotation.applicators;

import java.lang.reflect.Field;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.utils.ReflectionUtils;

public abstract class DefaultApplicator {

    ApiEntry apiEntry;
    Field field;

    public DefaultApplicator(ApiEntry apiEntry, Field field) {
        this.apiEntry = apiEntry;
        this.field = field;
    }

    protected Object get(Field field) {
        return ReflectionUtils.get(apiEntry, field);

    }

    protected void set(Field field, Object value) {
        ReflectionUtils.set(apiEntry, field, value);
    }
}
