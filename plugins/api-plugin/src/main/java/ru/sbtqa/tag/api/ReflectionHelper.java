package ru.sbtqa.tag.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.annotation.AddBracket;
import ru.sbtqa.tag.api.annotation.ApiRequestHeader;
import ru.sbtqa.tag.api.annotation.ApiRequestParam;
import ru.sbtqa.tag.api.annotation.ApiUrlParam;
import ru.sbtqa.tag.api.annotation.DependentResponseParam;
import ru.sbtqa.tag.api.annotation.PutInStash;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;
import ru.sbtqa.tag.api.exception.ApiException;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.parsers.core.ParserItem;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

public class ReflectionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionHelper.class);

    private ApiEntry apiEntry;

    public ReflectionHelper(ApiEntry apiEntry) {
        this.apiEntry = apiEntry;
    }

    /**
     * Set request parameter by title
     *
     * @param title a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    public void setParamValueByTitle(String title, String value) {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(apiEntry.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (((annotation instanceof ApiRequestParam && ((ApiRequestParam) annotation).title().equals(title))
                        || (annotation instanceof ApiUrlParam && ((ApiUrlParam) annotation).title().equals(title)))
                        && value != null && !value.isEmpty()) {
                    set(field, title, value);
                    return;
                }
            }
        }
        throw new ApiEntryInitializationException("There is no '" + title + "' parameter in '" + apiEntry.getActionTitle() + "' api entry.");
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
                set(field, name, value);
                return;
            }
        }
        throw new ApiEntryInitializationException("There is no parameter with name '" + name + "' in '" + apiEntry.getActionTitle() + "' api entry.");
    }

    @SuppressWarnings("ThrowableResultIgnored")
    protected void setDependentResponseParameters() {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        for (Field field : fieldList) {
            field.setAccessible(true);

            //@DependentResponseParam. Go to response in responseEntry and get some value by path
            if (null != field.getAnnotation(DependentResponseParam.class)) {
                DependentResponseParam dependantParamAnnotation = field.getAnnotation(DependentResponseParam.class);
                Object fieldValue = null;
                Class responseEntry = dependantParamAnnotation.responseEntry();

                if ((responseEntry == void.class || responseEntry == null)
                        && dependantParamAnnotation.usePreviousResponse()) {

                    // TODO REPOSITORY
                    responseEntry = null;
//                    responseEntry = ApiFactory.getApiFactory().getResponseRepository().getLastEntryInRepository();
                }

                if (!"".equals(dependantParamAnnotation.header())) {
                    // TODO REPOSITORY
                    Map<String, String> dependantResponseHeaders = null;
//                    Map<String, String> dependantResponseHeaders = ApiFactory.getApiFactory().getResponseRepository().getHeaders(responseEntry);
                    for (Map.Entry<String, String> header : dependantResponseHeaders.entrySet()) {
                        if (null != header.getKey() && header.getKey().equals(dependantParamAnnotation.header())) {
                            fieldValue = header.getValue();
                        }
                    }
                } else {
                    // TODO REPOSITORY
                    Object dependantResponseBody = null;
//                    Object dependantResponseBody = ApiFactory.getApiFactory().getResponseRepository().getBody(responseEntry);

                    //Use applied parser to get value by path throw the callback
                    if (ApiFactory.getApiFactory().getParser() != null) {
                        Object callbackResult = null;
                        try {
                            ParserItem item = new ParserItem((String) dependantResponseBody, dependantParamAnnotation.path());
                            callbackResult = ApiFactory.getApiFactory().getParser().getConstructor().newInstance().call(item);
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                            throw new ApiEntryInitializationException("Could not initialize parser callback", ex);
                        } catch (Exception e) {
                            LOG.debug("No such element in callback", e);
                            if (field.getAnnotation(DependentResponseParam.class).necessity()) {
                                throw new NoSuchElementException(e.getMessage());
                            }
                        }
                        if (callbackResult instanceof Exception) {
                            throw (ApiException) callbackResult;
                        } else {
                            fieldValue = callbackResult;
                        }
                    } else {
                        throw new ApiEntryInitializationException("Could not initialize parser callback");
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

//                parameters.put(field.getName(), fieldValue);
                setParamValueByName(field.getName(), fieldValue);
            }
        }
    }

    /**
     * Get parameters annotated by ApiRequestParam. Fill it by another response
     * if annotated by DependentResponseParam, put value in stash and add
     * brackets by the way.
     *
     * @throws ApiException if one of parameters is not available
     */
    public void applyParametersAnnotation() {
        //for each field in api request object search for annotations
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        for (Field field : fieldList) {

            String name = null;
            Object value = null;

            //@ApiRequestParam. Get field name and value
            if (null != field.getAnnotation(ApiRequestParam.class)) {
                name = field.getName();
                value = get(field, name);
            }

            //@PutInStash. Put name (or title) and value to stash
            if (null != field.getAnnotation(PutInStash.class)) {
                PutInStash putInStashAnnotation = field.getAnnotation(PutInStash.class);
                switch (putInStashAnnotation.by()) {
                    case NAME:
                        Stash.asMap().put(field.getName(), get(field, name));
                        break;
                    case TITLE:
                        if (null != field.getAnnotation(ApiRequestParam.class)) {
                            Stash.asMap().put(field.getAnnotation(ApiRequestParam.class).title(), value);
                        } else {
                            LOG.error("The field annotated by @PutInStash does not annotated by @ApiRequestParam");
                        }
                        break;
                }
            }

            //set parameter value by name only if it has one of parameter's annotation
            if (null != name) {
                setParamValueByName(name, value);

                //@AddBracket. Get name from value. Use it if value need to contains brackets.
                if (null != field.getAnnotation(AddBracket.class)) {
                    name = field.getAnnotation(AddBracket.class).value();
                }
//                parameters.put(name, value);
            }
        }
    }

    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();

        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof ApiRequestHeader) {
                    headers.put(((ApiRequestHeader) annotation).header(), (String) get(field, ((ApiRequestHeader) annotation).header()));
                }
            }
        }

        return headers;
    }


//    public void getBody() {
//        if (!"".equals(template)) {
//            //get body template from resources
//            String templatePath = Props.get("api.template.path", "");
//            String encoding = Props.get("api.encoding");
//            String templateFullPath = templatePath + template;
//            try {
//                body = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(templateFullPath), encoding).replace("\uFEFF", "");
//            } catch (NullPointerException ex) {
//                throw new ApiEntryInitializationException("Can't find template file by path " + templateFullPath, ex);
//            } catch (IOException ex) {
//                throw new ApiEntryInitializationException("Template file '" + templateFullPath + "' is not available", ex);
//            }
//
//            //replace %parameter on parameter value
//            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
//                if (parameter.getValue() instanceof String) {
//                    String value = (null != parameter.getValue()) ? (String) parameter.getValue() : "";
//                    body = body.replaceAll("%" + parameter.getKey(), value);
//                } else if (parameter.getValue() instanceof List) {
//                    String value = "";
//                    String separator = Props.get("api.dependent.array.separator");
//                    List<String> params = ((List) parameter.getValue());
//                    for (int i = 0; i < params.size(); i++) {
//                        value += params.get(i);
//                        if (separator != null && i != params.size() - 1) {
//                            value += separator;
//                        }
//                    }
//
//                    body = body.replaceAll("%" + parameter.getKey(), value);
//                } else {
//                    LOG.debug("Failed to substitute not String field to body template");
//                }
//            }
//        }
//    }

    private Object get(Field field, String name) {
        try {
            field.setAccessible(true);
            return field.get(apiEntry);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with name '" + name + "' is not available", ex);
        }

    }

    private void set(Field field, String name, Object value) {
        try {
            field.setAccessible(true);
            field.set(apiEntry, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with title '" + name + "' is not available", ex);
        }
    }
}
