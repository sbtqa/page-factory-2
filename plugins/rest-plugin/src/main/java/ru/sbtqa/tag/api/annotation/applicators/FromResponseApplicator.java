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

    private Class fromEndpoint;
    private String path;
    private String header;
    private String mask;
    private boolean isNecessity;

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
