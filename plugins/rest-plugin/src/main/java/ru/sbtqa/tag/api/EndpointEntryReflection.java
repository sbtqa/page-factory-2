package ru.sbtqa.tag.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbtqa.tag.api.annotation.Body;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Stashed;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.annotation.applicators.Applicator;
import ru.sbtqa.tag.api.annotation.applicators.ApplicatorHandler;
import ru.sbtqa.tag.api.annotation.applicators.FromResponseApplicator;
import ru.sbtqa.tag.api.annotation.applicators.StashedApplicator;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

import static java.lang.String.format;
import static ru.sbtqa.tag.api.utils.ReflectionUtils.*;

/**
 * The assistant class for {@link EndpointEntry}.
 * <p>
 * It helps to apply all of the fields annotations and consists getters for this fields
 */
public class EndpointEntryReflection {

    private EndpointEntry endpoint;
    private String entryTitle;
    private List<Field> fields;
    private Map<String, Method> validations = new HashMap<>();

    public EndpointEntryReflection(EndpointEntry endpoint) {
        this.endpoint = endpoint;
        this.entryTitle = endpoint.getClass().getAnnotation(Endpoint.class).title();
        this.fields = FieldUtilsExt.getDeclaredFieldsWithInheritance(endpoint.getClass());
        cacheValidations();
    }

    private void cacheValidations() {
        Method[] methods = endpoint.getClass().getMethods();
        for (Method method : methods) {
            Validation validation = method.getAnnotation(Validation.class);
            if (validation != null) {
                validations.put(validation.title(), method);
            }
        }
    }

    /**
     * Apply all belongs annotations to fields in endpoint entry
     */
    public void applyAnnotations() {
        for (Field field : fields) {
            field.setAccessible(true);

            ApplicatorHandler<Applicator> applicators = new ApplicatorHandler<>();
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof FromResponse) {
                    applicators.add(new FromResponseApplicator(endpoint, field));
                } else if (annotation instanceof Stashed) {
                    applicators.add(new StashedApplicator(endpoint, field));
                }
            }

            applicators.apply();
        }
    }

    /**
     * Get field by parameter annotation name (it can be one of {@link ru.sbtqa.tag.api.annotation.ParameterType}) and
     * set value to this field
     *
     * @param name parameter annotation name
     * @param value value to set
     */
    public void setParameterValueByTitle(String name, String value) {
        for (Field field : fields) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Body && ((Body) annotation).name().equals(name)
                        || annotation instanceof Query && ((Query) annotation).name().equals(name)
                        || annotation instanceof Header && ((Header) annotation).name().equals(name)
                        && value != null && !value.isEmpty()) {
                    set(endpoint, field, value);
                    return;
                }
            }
        }

        throw new RestPluginException(format("There is no \"%s\" parameter in \"%s\" endpoint", name, entryTitle));
    }

    /**
     * Invoke method annotated with {@link Validation} by title
     *
     * @param title title of validation rule {@link Validation#title()}
     * @param params params to pass to validation rule method
     */
    public void validate(String title, Object... params) {
        Method validation = validations.get(title);

        if (validation != null) {
            invoke(validation, endpoint, params);
        } else {
            throw new RestPluginException(format("There is no \"%s\" validation rule in \"%s\" endpoint", title, entryTitle));
        }
    }

    /**
     * Invoke method annotated with {@link Validation}.
     * Works if endpoint contains only one validation rule
     *
     * @param params params to pass to validation rule method
     */
    public void validate(Object... params) {
        if (validations.size() < 2) {
            Method validation = validations.values().stream().findFirst().orElseThrow(() -> new RestPluginException(format("There is no validation rules in \"%s\" endpoint", entryTitle)));
            invoke(validation, endpoint, params);
        } else {
            throw new RestPluginException(format("There is more then 1 validation rule in \"%s\" endpoint. Please specify validation rule with it title", entryTitle));
        }
    }

    /**
     * Get name-field map with fields annotated with one of {@link ParameterType} annotation
     *
     * @param type type of parameter
     * @return name-field map
     */

    public Map<String, Object> getParameters(ParameterType type) {
        Map<String, Object> parameters = new HashMap<>();

        for (Field field : fields) {
            for (Annotation annotation : field.getAnnotations()) {

                switch (type) {
                    case QUERY:
                        if (annotation instanceof Query) {
                            parameters.put(((Query) annotation).name(), get(endpoint, field));
                        }
                        break;
                    case HEADER:
                        if (annotation instanceof Header) {
                            parameters.put(((Header) annotation).name(), get(endpoint, field));
                        }
                        break;
                    case BODY:
                        if (annotation instanceof Body) {
                            parameters.put(((Body) annotation).name(), get(endpoint, field));
                        }
                        break;
                    default:
                        throw new RestPluginException(format("Parameter type \"%s\" is not supported", type));
                }
            }
        }

        return parameters;
    }
}
