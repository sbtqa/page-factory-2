package ru.sbtqa.tag.api;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.annotation.ApiUrlParam;
import ru.sbtqa.tag.api.annotation.ApiValidationRule;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;
import ru.sbtqa.tag.api.exception.ApiException;
import ru.sbtqa.tag.api.rest.HTTP;
import ru.sbtqa.tag.qautils.properties.Props;
import ru.sbtqa.tag.qautils.reflect.FieldUtilsExt;

/**
 * Api object (ala Page object). Request to definite url with a set of
 * parameters such as request method, parameters, response validation.
 */
public abstract class ApiEntry {

    protected static final Logger LOG = LoggerFactory.getLogger(ApiEntry.class);

    private ReflectionHelper reflectionHelper;

    private Map<String, Object> parameters = new HashMap<>();
    private HTTP requestMethod;
    private String requestPath;
    private String template;

    public ApiEntry() {
        reflectionHelper = new ReflectionHelper(this);
        requestMethod = this.getClass().getAnnotation(ApiAction.class).method();
        requestPath = this.getClass().getAnnotation(ApiAction.class).path();
        template = this.getClass().getAnnotation(ApiAction.class).template();
    }

    /**
     * Fill api request parameters, api request headers, request body and
     * replace templates in requestPath
     *
     * @throws ApiException if there is an
     * error in setters method
     */
    public void fillParameters() {
        reflectionHelper.applyParametersAnnotation();
//        getHeaders();
//        getBody();

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
     * Perform api request. Consist of prepare step, fill parameters step, buildRequest
     * url and send request step
     *
     * @return response
     * @throws ApiException if there is an error in setters, prepare or send
     * methods
     */
    public void fire() {
        reflectionHelper.setDependentResponseParameters();
        fillParameters();

        String requestUrl = ApiFactory.getApiRequestUrl();
        String url = requestUrl + "/" + requestPath;

        send(url);
    }

    /**
     * Perform api request. Consist of prepare step, fill parameters step, buildRequest
     * url and send request step
     *
     * @param url target url to send
     * @return response
     * @throws ApiException if there is an error in setters, prepare or send
     * methods
     */
    public void fire(String url) {
        reflectionHelper.setDependentResponseParameters();
        fillParameters();

        send(url);
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
    public void send(String url) {
        url = getFullUrl(url);

        RequestSpecification request = buildRequest();
        Response response;
        switch (requestMethod) {
            case GET:
                response = request.get(url);
                break;
            case POST:
                response = request.post(url);
                break;
            case PUT:
                response = request.put(url);
                break;
            case PATCH:
                response = request.patch(url);
                break;
            case DELETE:
                response = request.delete(url);
                break;
            case OPTIONS:
                response = request.options(url);
                break;
            case HEAD:
                response = request.head(url);
                break;
            default:
                throw new UnsupportedOperationException("Request method " + requestMethod + " is not support");
        }

        Repository.add(this.getClass(), new ApiPair(request, response));
    }

    private RequestSpecification buildRequest() {
        RequestSpecification request = given();

        request.headers(reflectionHelper.getHeaders());

        if (!template.isEmpty()) {
            request.body(reflectionHelper.getBody());
        }
        .
        return request;
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
     * Get url with param if param field exist
     *
     * @return partUrl withParams
     * @throws ApiException
     */
    protected String getFullUrl(String url) {
        List<Field> fieldList = FieldUtilsExt.getDeclaredFieldsWithInheritance(this.getClass());
        String urlParamString = "";
        for (Field field : fieldList) {
            ApiUrlParam urlParam = field.getAnnotation(ApiUrlParam.class);
            if (urlParam != null && !"".equals(urlParam.title())) {
                String param = (String) get(field, urlParam.title());
                if (param != null && !param.equals("")) {
                    if (!"".equals(urlParamString)) {
                        urlParamString += "&";
                    }
                    urlParamString += urlParam.title() + "=" + param;
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
    public String getBody() {
        String body;

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

        return body;
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
     * Get api action title
     *
     * @return title
     */
    public String getActionTitle() {
        return this.getClass().getAnnotation(ApiAction.class).title();
    }

    private Object get(Field field, String name) {
        try {
            field.setAccessible(true);
            return field.get(this);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with name '" + name + "' is not available", ex);
        }

    }

    private void set(Field field, String name, Object value) {
        try {
            field.setAccessible(true);
            field.set(this, value);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new ApiEntryInitializationException("Parameter with title '" + name + "' is not available", ex);
        }
    }
}
