package ru.sbtqa.tag.api.annotation.applicators;

import io.restassured.response.ValidatableResponse;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.exception.RestPluginException;

@Order(0)
public class FromResponseApplicator extends DefaultApplicator implements Applicator {

    private FromResponse fromResponseAnnotation;

    public FromResponseApplicator(EndpointEntry entry, Field field) {
        super(entry, field);
        this.fromResponseAnnotation = field.getAnnotation(FromResponse.class);
    }

    @Override
    public void apply() {
        ValidatableResponse response = getResponse();
        Object value = getValue(response);
        value = applyMask(value);

        set(field, value);
    }

    private ValidatableResponse getResponse() {
        Class fromEndpoint = fromResponseAnnotation.endpointEntry();

        if ((fromEndpoint == void.class || fromEndpoint == null) && fromResponseAnnotation.usePrevious()) {
            return ApiEnvironment.getRepository().getLast().getResponse();
        } else {
            return ApiEnvironment.getRepository().get(fromEndpoint).getResponse();
        }
    }

    private Object getValue(ValidatableResponse response) {
        if (!"".equals(fromResponseAnnotation.header())) {
            return response.extract().header(fromResponseAnnotation.header());
        } else {
            if (response.extract().body().path(fromResponseAnnotation.path()) != null
                    || field.getAnnotation(FromResponse.class).necessity()) {
                return response.extract().body().path(fromResponseAnnotation.path()).toString();
            } else {
                return null;
            }
        }
    }

    private Object applyMask(Object value) {
        if (!fromResponseAnnotation.mask().isEmpty()) {
            if (value instanceof String) {
                Matcher matcher = Pattern.compile(fromResponseAnnotation.mask()).matcher((String) value);
                value = "";
                if (matcher.find()) {
                    value = matcher.group(1);
                }
                return value;
            } else {
                throw new RestPluginException("Masking was failed because " + field.getName() + " is not instance of String");
            }
        } else {
            return value;
        }
    }
}
