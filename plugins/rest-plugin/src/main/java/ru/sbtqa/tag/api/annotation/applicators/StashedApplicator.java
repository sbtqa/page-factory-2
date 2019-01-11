package ru.sbtqa.tag.api.annotation.applicators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.EndpointEntryReflection;
import ru.sbtqa.tag.api.annotation.Stashed;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.datajack.Stash;

import java.lang.reflect.Field;

/**
 * Applicator for {@link Stashed} annotation
 */
public class StashedApplicator extends DefaultApplicator implements Applicator {

    private static final Logger LOG = LoggerFactory.getLogger(EndpointEntryReflection.class);

    public StashedApplicator(EndpointEntry entry, Field field) {
        super(entry, field);
    }

    @Override
    public void apply() {
        Stashed stashed = field.getAnnotation(Stashed.class);
        switch (stashed.by()) {
            case NAME:
                Stash.asMap().put(field.getName(), get(field));
                break;
            case TITLE:
                if (!stashed.title().isEmpty()) {
                    Stash.asMap().put(stashed.title(), get(field));
                } else {
                    LOG.error("The field annotated by @Stashed has the null as title");
                }
                break;
            default:
                throw new RestPluginException("Failed to apply @Stashed annotation");
        }

    }
}
