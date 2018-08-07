package ru.sbtqa.tag.api.annotation.applicators;

import java.lang.reflect.Field;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.Bracketed;
import ru.sbtqa.tag.api.annotation.Parameter;

@Order(1)
public class ParameterApplicator extends DefaultApplicator implements Applicator {

    public ParameterApplicator(ApiEntry entry, Field field) {
        super(entry, field);
    }

    @Override
    public void apply() {
        String name = null;
        Object value = null;

        //@Parameter. Get field name and value
        if (null != field.getAnnotation(Parameter.class)) {
            name = field.getName();
            value = get(field);
        }

        //set parameter value by name only if it has one of parameter's annotation
        if (null != name) {
            set(field, value);

            // TODO сейчас не работает
            //@Bracketed. Get name from value. Use it if value need to contains brackets.
            if (null != field.getAnnotation(Bracketed.class)) {
                name = field.getAnnotation(Bracketed.class).value();
            }
        }
    }
}
