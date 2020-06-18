package ru.sbtqa.tag.api.annotation.applicators;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.FinalSetter;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.api.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.String.format;

/**
 * Applicator for {@link FinalSetter} annotation
 */

@Order(value = 200)
public class FinalSetterApplicator  extends DefaultApplicator implements Applicator {

    public FinalSetterApplicator(EndpointEntry entry, Field field) {
        super(entry, field);
    }

    @Override
    public void apply() {
        Object fieldValue = get(field);
        set(field, fieldValue);
    }

    @Override
    protected void set(Field field, Object value) {
        Method finalSetter = getFinalSetterOrNull(endpoint, field, value);
        ReflectionUtils.invoke(finalSetter, endpoint, value);

    }

    private Method getFinalSetterOrNull(EndpointEntry endpoint, Field field, Object value) {
        FinalSetter finalSetter = field.getAnnotation(FinalSetter.class);
            try {
                Method setter = endpoint.getClass().getMethod(finalSetter.method(), value.getClass());
                setter.setAccessible(true);
                return setter;
            } catch (NoSuchMethodException ex) {
                throw new RestPluginException(format(
                    "Final setter method \"%s\" with \"%s\" type parameter is not found",
                    finalSetter.method(), value.getClass()), ex);
            }
        }
    }


