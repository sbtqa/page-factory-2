package ru.sbtqa.tag.api;

import cucumber.api.DataTable;
import io.restassured.response.ValidatableResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.annotation.Bracketed;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.annotation.FromResponse;
import ru.sbtqa.tag.api.annotation.Header;
import ru.sbtqa.tag.api.annotation.Parameter;
import ru.sbtqa.tag.api.annotation.Query;
import ru.sbtqa.tag.api.annotation.Stashed;
import ru.sbtqa.tag.api.annotation.Validation;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;
import ru.sbtqa.tag.api.exception.ApiException;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.qautils.properties.Props;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

public class ReflectionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionHelper.class);

    private ApiEntry apiEntry;

    public ReflectionHelper(ApiEntry apiEntry) {
        this.apiEntry = apiEntry;
    }

    /**
     * Set request parameter by name
     *
     * @param name a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    public void setParamValueByTitle(String name, String value) {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Parameter && ((Parameter) annotation).name().equals(name)
                        || annotation instanceof Query && ((Query) annotation).name().equals(name)
                        || annotation instanceof Header && ((Header) annotation).name().equals(name)
                        && value != null && !value.isEmpty()) {
                    set(field, value);
                    return;
                }
            }
        }

        throw new ApiEntryInitializationException("There is no '" + name + "' parameter in '" + apiEntry.getClass().getAnnotation(Endpoint.class).title() + "' api entry.");
    }

    /**
     * Set request parameter by name
     *
     * @param name a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * name doesn't exists or not available
     */
    protected void setParamValueByName(String name, Object value) {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {
            if (name.equals(field.getName())) {
                set(field, value);
                return;
            }
        }
        throw new ApiEntryInitializationException("There is no parameter with name '" + name + "' in '" + apiEntry.getClass().getAnnotation(Endpoint.class).title() + "' api entry.");
    }

    protected void setDependentResponseParameters() {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {
            field.setAccessible(true);

            if (null != field.getAnnotation(FromResponse.class)) {
                FromResponse dependantParamAnnotation = field.getAnnotation(FromResponse.class);
                Object fieldValue = null;
                ValidatableResponse response;
                Class fromApiEntry = dependantParamAnnotation.responseApiEntry();

                if ((fromApiEntry == void.class || fromApiEntry == null) && dependantParamAnnotation.usePreviousResponse()) {
                    response = ApiEnvironment.getRepository().getLast().getResponse();
                } else {
                    response = ApiEnvironment.getRepository().get(fromApiEntry).getResponse();
                }

                if (!"".equals(dependantParamAnnotation.header())) {
                     fieldValue = response.extract().header(dependantParamAnnotation.header());
                } else {

                    if (response.extract().body().path(dependantParamAnnotation.path()) == null
                            && !field.getAnnotation(FromResponse.class).necessity()) {

                    } else {
                        fieldValue = response.extract().body().path(dependantParamAnnotation.path()).toString();
                    }
                }

                if (!"".equals(dependantParamAnnotation.mask())) {
                    if (fieldValue instanceof String) {
                        Matcher matcher = Pattern.compile(dependantParamAnnotation.mask()).matcher((String) fieldValue);
                        fieldValue = "";
                        if (matcher.find()) {
                            fieldValue = matcher.group(1);
                        }
                    } else {
                        throw new ApiException("Masking was failed because " + field.getName() + " is not instance of String");
                    }
                }

                setParamValueByName(field.getName(), fieldValue);
            }
        }
    }

    /**
     * Get parameters annotated by Parameter. Fill it by another response
     * if annotated by FromResponse, put value in stash and add
     * brackets by the way.
     *
     * @throws ApiException if one of parameters is not available
     */
    public void applyParametersAnnotation() {
        //for each field in api request object search for annotations
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {

            String name = null;
            Object value = null;

            //@Parameter. Get field name and value
            if (null != field.getAnnotation(Parameter.class)) {
                name = field.getName();
                value = get(field);
            }

            //@Stashed. Put name (or name) and value to stash
            if (null != field.getAnnotation(Stashed.class)) {
                Stashed stashedAnnotation = field.getAnnotation(Stashed.class);
                switch (stashedAnnotation.by()) {
                    case NAME:
                        Stash.asMap().put(field.getName(), get(field));
                        break;
                    case TITLE:
                        if (null != field.getAnnotation(Parameter.class)) {
                            Stash.asMap().put(field.getAnnotation(Parameter.class).name(), value);
                        } else {
                            LOG.error("The field annotated by @Stashed does not annotated by @Parameter");
                        }
                        break;
                }
            }

            //set parameter value by name only if it has one of parameter's annotation
            if (null != name) {
                setParamValueByName(name, value);

                //@Bracketed. Get name from value. Use it if value need to contains brackets.
                if (null != field.getAnnotation(Bracketed.class)) {
                    name = field.getAnnotation(Bracketed.class).value();
                }
//                parameters.put(name, value);
            }
        }
    }

    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();

        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Header) {
                    headers.put(((Header) annotation).name(), (String) get(field));
                }
            }
        }

        return headers;
    }


    /**
     * Get request body. Get request body template from resources
     *
     * @throws ApiException if template file
     * doesn't exist or not available
     */
    public String getBody(String template) {
        String body;

        //get body template from resources
        String templatePath = Props.get("api.template.path", "");
        String encoding = Props.get("api.encoding");
        String templateFullPath = templatePath + template;
        try {
            body = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(templateFullPath), encoding).replace("\uFEFF", "");
        } catch (NullPointerException ex) {
            throw new ApiEntryInitializationException("Can't find template file by path " + templateFullPath, ex);
        } catch (IOException ex) {
            throw new ApiEntryInitializationException("Template file '" + templateFullPath + "' is not available", ex);
        }

        //replace %parameter on parameter value
        for (Map.Entry<String, Object> parameter : getParameters().entrySet()) {
            if (parameter.getValue() instanceof String) {
                String value = (null != parameter.getValue()) ? (String) parameter.getValue() : "";
                body = body.replaceAll("%" + parameter.getKey(), value);
            } else if (parameter.getValue() instanceof List) {

                // TODO wtf is this?
                String value = "";
                String separator = Props.get("api.dependent.array.separator");
                List<String> params = ((List) parameter.getValue());
                for (int i = 0; i < params.size(); i++) {
                    value += params.get(i);
                    if (separator != null && i != params.size() - 1) {
                        value += separator;
                    }
                }

                body = body.replaceAll("%" + parameter.getKey(), value);
            } else {
                LOG.debug("Failed to substitute not String field to body template");
            }
        }

        return body;
    }

    private Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();

        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Parameter) {
                    parameters.put(((Parameter) annotation).name(), get(field));
                }
            }
        }


        return parameters;
    }

    private Object get(Field field) {
        try {
            field.setAccessible(true);
            return field.get(apiEntry);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with name '" + field.getName() + "' is not available", ex);
        }

    }

    private void set(Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(apiEntry, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with name '" + field.getName() + "' is not available", ex);
        }
    }


    /**
     * Perform action validation rule
     *
     * @param title a {@link java.lang.String} object.
     * @param params a {@link java.lang.Object} object.
     * @throws ApiException if can't invoke
     * method
     */
    public void validate(String title, Object... params) {
        Method[] methods = apiEntry.getClass().getMethods();
        for (Method method : methods) {
            if (null != method.getAnnotation(Validation.class)
                    && method.getAnnotation(Validation.class).title().equals(title)) {
                try {
                    method.invoke(apiEntry, params);
                } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
                    throw new ApiException("Failed to invoke method", e);
                }
                return;
            }
        }
        throw new ApiEntryInitializationException("There is no '" + title + "' validation rule in '" + apiEntry.getClass().getAnnotation(Endpoint.class).title() + "' api entry.");
    }

    public Map<String, ?> getQueryParams() {
        Map<String, Object> queryParams = new HashMap<>();

        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Query) {
                    queryParams.put(((Query) annotation).name(), get(field));
                }
            }
        }

        return queryParams;
    }

    public void applyDatatable(DataTable dataTable) {
        for (Map.Entry<String, String> dataTableRow : dataTable.asMap(String.class, String.class).entrySet()) {
            setParamValueByTitle(dataTableRow.getKey(), dataTableRow.getValue());
        }
    }
}
