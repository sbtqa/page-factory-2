package ru.sbtqa.tag.api.annotation.applicators;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Mutator;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.api.utils.EmptyClass;
import ru.sbtqa.tag.api.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;

/**
 * Applicator for {@link Mutator} annotation
 */
@Order(value = 200)
public class MutatorApplicator extends DefaultApplicator implements Applicator {

    public MutatorApplicator(EndpointEntry entry, Field field) {
        super(entry, field);
    }

    @Override
    public void apply() {
        Object fieldValue = get(field);
        Method mutator = getMutator(endpoint, field);
        set(field, ReflectionUtils.invoke(mutator, endpoint, fieldValue));
    }

    private Method getMutator(EndpointEntry endpoint, Field field) {
        Mutator mutator = field.getAnnotation(Mutator.class);
        Class container = mutator.clazz() == EmptyClass.class ? endpoint.getClass() : mutator.clazz();
        Method setter = Arrays.stream(container.getMethods())
            .filter(method -> Objects.equals(method.getName(), mutator.method()))
            .findFirst()
            .orElseThrow(() -> new RestPluginException(format(
                "Mutator method \"%s\" is not found",
                mutator.method())));
        setter.setAccessible(true);

        return setter;
    }
}


