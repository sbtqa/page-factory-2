package ru.sbtqa.tag.api.annotation.applicators;

import java.lang.reflect.Field;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.utils.FromResponseUtils;

/**
 * Applicator for {@link FromResponse} annotation
 */
@Order(0)
public class FromResponseApplicator extends DefaultApplicator implements Applicator {

    private final Class fromEndpoint;
    private final String path;
    private final String header;
    private final String mask;
    private final boolean isNecessity;

    public FromResponseApplicator(EndpointEntry entry, Field field) {
        super(entry, field);

        FromResponse fromResponseAnnotation = field.getAnnotation(FromResponse.class);
        fromEndpoint = fromResponseAnnotation.endpoint();
        path = fromResponseAnnotation.path();
        header = fromResponseAnnotation.header();
        mask = fromResponseAnnotation.mask();
        isNecessity = fromResponseAnnotation.optional();
    }

    @Override
    public void apply() {
        Object value = FromResponseUtils.getValueFromResponse(fromEndpoint, header, path, mask, isNecessity);
        set(field, value);
    }
}
