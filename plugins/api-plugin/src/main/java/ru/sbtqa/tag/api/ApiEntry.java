package ru.sbtqa.tag.api;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.annotation.AddBracket;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiRequestHeader;
import ru.sbtqa.tag.api.annotation.ApiRequestParam;
import ru.sbtqa.tag.api.annotation.ApiUrlParam;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.annotation.DependentResponseParam;
import ru.sbtqa.tag.api.annotation.PutInStash;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;
import ru.sbtqa.tag.api.exception.ApiException;
import ru.sbtqa.tag.api.repositories.Bullet;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.api.rest.Rest;
import ru.sbtqa.tag.api.soap.Soap;
import ru.sbtqa.tag.datajack.Stash;
import ru.sbtqa.tag.parsers.core.ParserItem;
import ru.sbtqa.tag.qautils.properties.Props;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

/**
 * Api object (ala Page object). Request to definite url with a set of
 * parameters such as request method, parameters, response validation.
 *
 *
 */
public abstract class ApiEntry {

    protected static final Logger LOG = LoggerFactory.getLogger(ApiEntry.class);

    private String requestPath = this.getClass().getAnnotation(ApiAction.class).path();
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> parameters = new HashMap<>();
    private String body = null;
    private String template = this.getClass().getAnnotation(ApiAction.class).template();

    /**
     * Set request parameter by title
     *
     * @param title a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @throws ApiException if
     */
    public void setParamValueByTitle(String title, String value) throws ApiException {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (((annotation instanceof ApiRequestParam && ((ApiRequestParam) annotation).title().equals(title))
                        || (annotation instanceof ApiUrlParam && ((ApiUrlParam) annotation).title().equals(title)))
                        && value != null && !value.isEmpty()) {
                    field.setAccessible(true);
                    try {
                        field.set(this, value);
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        throw new ApiEntryInitializationException("Parameter with title '" + title + "' is not available", ex);
                    }
                    return;
                }
            }
        }
        throw new ApiEntryInitializationException("There is no '" + title + "' parameter in '" + this.getActionTitle() + "' api entry.");
    }

    /**
     * Set request parameter by name
     *
     * @param name a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @throws ApiException if parameter with
     * name doesn't exists or not available
     */
    protected void setParamValueByName(String name, Object value) throws ApiException {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        for (Field field : fieldList) {
            if (name.equals(field.getName())) {
                field.setAccessible(true);
                try {
                    field.set(this, value);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new ApiEntryInitializationException("Failed to write value to field '" + name + "'", ex);
                }
                return;
            }
        }
        throw new ApiEntryInitializationException("There is no parameter with name '" + name + "' in '" + this.getActionTitle() + "' api entry.");
    }

    /**
     * Fill request parameters from data table
     *
     * @param params the list of parameters
     */
    public void fillParams(Map<String, String> params) {

        for (Map.Entry<String, String> p : params.entrySet()) {
            try {
                setParamValueByTitle(p.getKey(), p.getValue());
            } catch (ApiException e) {
                LOG.error("Failed to set params value by title", e);
            }
        }
    }

    /**
     * Fill api request parameters, api request headers, request body and
     * replace templates in requestPath
     *
     * @throws ApiException if there is an
     * error in setters method
     */
    public void fillParameters() throws ApiException {
        applyParametersAnnotation();
        setHeaders();
        setBody();

        //if api action path contains parameters like '%parameter' replace it with it value
        for (Map.Entry<String, Object> parameter : sortByKeyLength(parameters).entrySet()) {
            if (parameter.getValue() instanceof String) {
                requestPath = requestPath.replaceAll("%" + parameter.getKey(), (String) parameter.getValue());
            } else {
                LOG.debug("Failed to substitute not String field to path template");
            }
        }
    }

    /**
     * Override it if you want to do pre-fire actions such as database run-up,
     * test data prepare or something else
     *
     * @throws ApiException if there is an
     * error in user's prepare steps
     */
    public void prepare() throws ApiException {

    }

    /**
     * Perform action with request method to url. Override it if you need use
     * another rest or soap implementation
     *
     * @param url action target
     * @return response
     * @throws ApiException if response is not
     * an instance of bullet type
     */
    public Object fire(String url) throws ApiException {
        //Get request method of current api object
        HTTP requestMethod = this.getClass().getAnnotation(ApiAction.class).method();
        String templateName = this.getClass().getAnnotation(ApiAction.class).template();

        url = getFullUrl(url);

        Bullet response = null;
        Bullet request = null;
        Class restImpl = ApiFactory.getApiFactory().getRest();
        Class soapImpl = ApiFactory.getApiFactory().getSoap();
        try {
            Rest rest = (Rest) restImpl.newInstance();
            Soap soap = (Soap) soapImpl.newInstance();

            Object bd;
            if (!"".equals(templateName)) {
                bd = getBody();
            } else {
                bd = getParameters();
            }

            Map<String, String> hdrs = getHeaders();
            switch (requestMethod) {
                case GET:
                    response = (Bullet) rest.get(url, hdrs);
                    ApiFactory.getApiFactory().addRequestHeadersToRepository(this.getClass(), hdrs);
                    break;
                case POST:
                    response = (Bullet) rest.post(url, hdrs, bd);
                    request = new Bullet(hdrs, bd.toString());
                    ApiFactory.getApiFactory().addRequestToRepository(this.getClass(), request);
                    break;
                case PUT:
                    response = (Bullet) rest.put(url, hdrs, bd);
                    request = new Bullet(hdrs, bd.toString());
                    ApiFactory.getApiFactory().addRequestToRepository(this.getClass(), request);
                    break;
                case PATCH:
                    response = (Bullet) rest.patch(url, hdrs, bd);
                    request = new Bullet(hdrs, bd.toString());
                    ApiFactory.getApiFactory().addRequestToRepository(this.getClass(), request);
                    break;
                case DELETE:
                    response = (Bullet) rest.delete(url, hdrs);
                    ApiFactory.getApiFactory().addRequestHeadersToRepository(this.getClass(), hdrs);
                    break;
                case SOAP:
                    response = (Bullet) soap.send(url, hdrs, bd, Proxy.NO_PROXY);
                    request = new Bullet(hdrs, bd.toString());
                    ApiFactory.getApiFactory().addRequestToRepository(this.getClass(), request);
                    break;
                default:
                    throw new UnsupportedOperationException("Request method " + requestMethod + " is not support");
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            LOG.error("Error with fire method implementation generate", ex);
        }

        return response;
    }

    /**
     * Perform api request. Consist of prepare step, fill parameters step, build
     * url and fire request step
     *
     * @return response
     * @throws ApiException if there is an error in setters, prepare or fire
     * methods
     */
    public Object fireRequest() throws ApiException {
        setDependentResponseParameters();
        prepare();
        fillParameters();

        String requestUrl = ApiFactory.getApiRequestUrl();
        String url = requestUrl + "/" + requestPath;

        Object response = fire(url);
        //Put response to response repository
        ApiFactory.getApiFactory().addResponseToRepository(this.getClass(), (Bullet) response);
        return response;
    }

    /**
     * Perform api request. Consist of prepare step, fill parameters step, build
     * url and fire request step
     *
     * @param url target url to fire
     * @return response
     * @throws ApiException if there is an error in setters, prepare or fire
     * methods
     */
    public Object fireRequest(String url) throws ApiException {
        setDependentResponseParameters();
        prepare();
        fillParameters();

        Object response = fire(url);
        //Put response to response repository
        ApiFactory.getApiFactory().addResponseToRepository(this.getClass(), (Bullet) response);
        return response;
    }

    /**
     * Perform action validation rule
     *
     * @param title a {@link java.lang.String} object.
     * @param params a {@link java.lang.Object} object.
     * @throws ApiException if can't invoke
     * method
     */
    public void fireValidationRule(String title, Object... params) throws ApiException {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (null != method.getAnnotation(ApiValidationRule.class)
                    && method.getAnnotation(ApiValidationRule.class).title().equals(title)) {
                try {
                    method.invoke(this, params);
                } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
                    throw new ApiException("Failed to invoke method", e);
                }
                return;
            }
        }
        throw new ApiEntryInitializationException("There is no '" + title + "' validation rule in '" + this.getActionTitle() + "' api entry.");
    }

    /**
     * Get parameters annotated by ApiRequestParam. Fill it by another response
     * if annotated by DependentResponseParam, put value in stash and add
     * brackets by the way.
     *
     * @throws ApiException if one of parameters is not available
     */
    public void applyParametersAnnotation() throws ApiException {
        //for each field in api request object search for annotations
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        for (Field field : fieldList) {
            field.setAccessible(true);

            String name = null;
            Object value = null;

            //@ApiRequestParam. Get field name and value
            if (null != field.getAnnotation(ApiRequestParam.class)) {
                name = field.getName();
                try {
                    value = field.get(this);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new ApiEntryInitializationException("Parameter with name '" + name + "' is not available", ex);
                }
            }

            //@PutInStash. Put name (or title) and value to stash
            if (null != field.getAnnotation(PutInStash.class)) {
                PutInStash putInStashAnnotation = field.getAnnotation(PutInStash.class);
                switch (putInStashAnnotation.by()) {
                    case NAME:
                        try {
                            Stash.asMap().put(field.getName(), (String) field.get(this));
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            throw new ApiEntryInitializationException("Parameter with name '" + name + "' is not available", ex);
                        }
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
                parameters.put(name, value);
            }
        }
    }

    /**
     * Get and fill api entry headers
     *
     * @throws ApiException if one of headers
     * is not available
     */
    protected void setHeaders() throws ApiException {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        for (Field field : fieldList) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof ApiRequestHeader) {
                    field.setAccessible(true);
                    try {
                        headers.put(((ApiRequestHeader) annotation).header(), (String) field.get(this));
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        throw new ApiEntryInitializationException("Parameter with headers title '" + ((ApiRequestHeader) annotation).header() + "' is not available", ex);
                    }
                }
            }
        }
    }

    /**
     * Get url with param if param field exist
     *
     * @return partUrl withParams
     * @throws ApiException
     */
    protected String getFullUrl(String url) throws ApiException {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        String urlParamString = "";
        for (Field field : fieldList) {
            ApiUrlParam urlParam = field.getAnnotation(ApiUrlParam.class);
            if (urlParam != null && !"".equals(urlParam.title())) {
                field.setAccessible(true);
                try {
                    String param = (String) field.get(this);
                    if (param != null && !param.equals("")) {
                        if (!"".equals(urlParamString)) {
                            urlParamString += "&";
                        }
                        urlParamString += urlParam.title() + "=" + param;
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new ApiEntryInitializationException("Parameter with title '" + urlParam.title()+ "' is not available", ex);
                }
            }
        }

        if (!urlParamString.equals("")) {
            if (url.contains("?")) {
                url += "&" + urlParamString;
            } else {
                url += "?" + urlParamString;
            }
        }
        return url;
    }

    /**
     * Get request body. Get request body template from resources
     *
     * @throws ApiException if template file
     * doesn't exist or not available
     */
    public void setBody() throws ApiException {
        if (!"".equals(template)) {
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
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                if (parameter.getValue() instanceof String) {
                    String value = (null != parameter.getValue()) ? (String) parameter.getValue() : "";
                    body = body.replaceAll("%" + parameter.getKey(), value);
                } else if (parameter.getValue() instanceof List) {
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
        }
    }

    @SuppressWarnings("ThrowableResultIgnored")
    protected void setDependentResponseParameters() throws ApiException {
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
                    responseEntry = ApiFactory.getApiFactory().getResponseRepository().getLastEntryInRepository();
                }

                if (!"".equals(dependantParamAnnotation.header())) {
                    Map<String, String> dependantResponseHeaders = ApiFactory.getApiFactory().getResponseRepository().getHeaders(responseEntry);
                    for (Map.Entry<String, String> header : dependantResponseHeaders.entrySet()) {
                        if (null != header.getKey() && header.getKey().equals(dependantParamAnnotation.header())) {
                            fieldValue = header.getValue();
                        }
                    }
                } else {
                    Object dependantResponseBody = ApiFactory.getApiFactory().getResponseRepository().getBody(responseEntry);

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

                parameters.put(field.getName(), fieldValue);
                setParamValueByName(field.getName(), fieldValue);
            }
        }
    }

    protected Map<String, Object> sortByKeyLength(Map<String, Object> map) {
        Map<String, Object> treeMap = new TreeMap<>(
                new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                if (s1.length() > s2.length()) {
                    return -1;
                } else if (s1.length() < s2.length()) {
                    return 1;
                } else {
                    return s1.compareTo(s2);
                }
            }
        });

        treeMap.putAll(map);
        return treeMap;
    }

    /**
     * Get headers
     *
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Add header to headers map
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Get parameters
     *
     * @return the parameters
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Add parameter to parameters map
     * @param key
     * @param value
     */
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    /**
     * Get request body
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set request body
     *
     * @param body a {@link java.lang.String} object.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get template name
     *
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Set template name
     *
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Get api action path
     *
     * @return action path
     */
    public String getActionPath() {
        return requestPath;
    }

    /**
     * Set action path
     * @param path
     */
    public void setActionPath(String path) {
        requestPath = path;
    }

    /**
     * Get api action title
     *
     * @return title
     */
    public String getActionTitle() {
        return this.getClass().getAnnotation(ApiAction.class).title();
    }
}
