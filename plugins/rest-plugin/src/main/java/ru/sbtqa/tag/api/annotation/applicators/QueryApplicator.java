package ru.sbtqa.tag.api.annotation.applicators;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.utils.PlaceholderUtils;

import java.lang.reflect.Field;

/**
 * Applicator for {@link Query} annotation
 */
public class QueryApplicator extends DefaultApplicator implements Applicator {

    public QueryApplicator(EndpointEntry entry, Field field) {
        super(entry, field);
    }

    @Override
    public void apply() {
        String path = endpoint.getPath();
        String placeholder = field.getAnnotation(Query.class).name();

        if (path.contains(placeholder)) {
            String replacedPath = PlaceholderUtils.replacePlaceholder(path, placeholder, get(field));
            endpoint.setPath(replacedPath);
            set(field, null);
        }
    }
}
