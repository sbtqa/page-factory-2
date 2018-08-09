package ru.sbtqa.tag.api.annotation.applicators;

import io.restassured.response.ValidatableResponse;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.ApiEnvironment;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.exception.ApiException;

@Order(0)
public class FromResponseApplicator extends DefaultApplicator implements Applicator {

    FromResponse fromResponse;

    public FromResponseApplicator(ApiEntry entry, Field field) {
        super(entry, field);
        this.fromResponse = field.getAnnotation(FromResponse.class);
    }

    @Override
    public void apply() {
        ValidatableResponse response = getResponse();
        Object value = getValue(response);
        value = applyMask(value);

        set(field, value);
    }

    private ValidatableResponse getResponse() {
        Class fromApiEntry = fromResponse.responseApiEntry();

        if ((fromApiEntry == void.class || fromApiEntry == null) && fromResponse.usePreviousResponse()) {
            return ApiEnvironment.getRepository().getLast().getResponse();
        } else {
            return ApiEnvironment.getRepository().get(fromApiEntry).getResponse();
        }
    }

    private Object getValue(ValidatableResponse response) {
        if (!"".equals(fromResponse.header())) {
            return response.extract().header(fromResponse.header());
        } else {
            if (response.extract().body().path(fromResponse.path()) != null
                    || field.getAnnotation(FromResponse.class).necessity()) {
                return response.extract().body().path(fromResponse.path()).toString();
            } else {
                return null;
            }
        }
    }

    private Object applyMask(Object value) {
        if (!fromResponse.mask().isEmpty()) {
            if (value instanceof String) {
                Matcher matcher = Pattern.compile(fromResponse.mask()).matcher((String) value);
                value = "";
                if (matcher.find()) {
                    value = matcher.group(1);
                }
                return value;
            } else {
                throw new ApiException("Masking was failed because " + field.getName() + " is not instance of String");
            }
        } else {
            return value;
        }
    }
}
