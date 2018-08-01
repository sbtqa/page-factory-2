package ru.sbtqa.tag.api;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;
import ru.sbtqa.tag.api.exception.ApiException;
import ru.sbtqa.tag.api.repositories.Bullet;
import ru.sbtqa.tag.api.repositories.Repository;
import ru.sbtqa.tag.api.repositories.RepositoryType;
import ru.sbtqa.tag.api.rest.Rest;
import ru.sbtqa.tag.api.rest.RestRawImpl;
import ru.sbtqa.tag.api.soap.Soap;
import ru.sbtqa.tag.api.soap.SoapImpl;
import ru.sbtqa.tag.parsers.core.ParserCallback;

/**
 * <p>
 * ApiFactory class.</p>
 *
 *
 */
public class ApiFactoryWrapper {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApiFactoryWrapper.class);

    private String currentEntryTitle;
    private String currentEntryPath;
    private ApiEntry currentEntry;

    private final String entriesPackage;
    private Class<? extends ParserCallback> parser;
    private Class<? extends Rest> rest = RestRawImpl.class;
    private Class<? extends Soap> soap = SoapImpl.class;
    private final Repository responseRepository;
    private final Repository requestRepository;

    /**
     * Constructor for ApiFactory.
     *
     * @param pagesPackage a {@link java.lang.String} object.
     */
    public ApiFactoryWrapper(String pagesPackage) {
        this.entriesPackage = pagesPackage;
        this.responseRepository = new Repository(RepositoryType.RESPONSE);
        this.requestRepository = new Repository(RepositoryType.REQUEST);
    }

    /**
     * Get api entry object by title
     *
     * @param title api entry title
     * @return api entry instance
     * @throws ru.sbtqa.tag.apifactory.exception.ApiException if api entry doesn't exist
     */
    public ApiEntry getApiEntry(String title) throws ApiException {
        if (null != currentEntry) {
            currentEntry = getApiEntry(currentEntry.getClass().getPackage().getName(), title);
        }
        if (null == currentEntry) {
            currentEntry = getApiEntry(entriesPackage, title);

        }
        if (null == currentEntry) {
            throw new ApiException("Api entry with title '" + title + "' is not registered");
        }
        return currentEntry;
    }

    /**
     * Get api entry by title and packageName
     *
     * @param packageName api entry package name
     * @param title api entry title
     * @return api entry instance
     * @throws ru.sbtqa.tag.apifactory.exception.ApiException if there is an error with parameters initialize
     */
    private ApiEntry getApiEntry(String packageName, String title) throws ApiException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Set<Class<?>> allClasses = new HashSet<>();
        try {
            for (ClassPath.ClassInfo info: ClassPath.from(loader).getAllClasses()) {
                if (info.getName().startsWith(packageName + ".")) {
                    allClasses.add(info.load());
                }
            }
        } catch (IOException ex) {
            LOG.warn("Failed to shape class info set", ex);
        }

        for (Class<?> entry : allClasses) {
            if (null != entry.getAnnotation(ApiAction.class)) {
                String entryTitle = entry.getAnnotation(ApiAction.class).title();
                String entryPath = entry.getAnnotation(ApiAction.class).path();
                if (entryTitle != null && entryTitle.equals(title)) {
                    try {
                        Constructor<ApiEntry> c = (Constructor<ApiEntry>) entry.getConstructor();
                        currentEntry = c.newInstance();
                        currentEntryTitle = title;
                        currentEntryPath = entryPath;
                        return currentEntry;
                    } catch (NoSuchMethodException | SecurityException | InstantiationException
                            | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        throw new ApiEntryInitializationException("Can't initialize current entry parameters", ex);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns currently initialized api entry
     *
     * @return api entry
     * @throws ru.sbtqa.tag.apifactory.exception.ApiException if there are an error with api entry initialize
     */
    public ApiEntry getCurrentApiEntry() throws ApiException {
        if (null == currentEntry) {
            throw new ApiEntryInitializationException("Current api entry not initialized");
        } else {
            return currentEntry;
        }
    }

    /**
     * @return the currentEntryTitle
     */
    public String getCurrentEntryTitle() {
        return currentEntryTitle;
    }

    /**
     * @return the currentEntryPath
     */
    public String getCurrentEntryPath() {
        return currentEntryPath;
    }

    /**
     * @return the parser
     */
    public Class<? extends ParserCallback> getParser() {
        return parser;
    }

    /**
     * @param parser the parser to set
     */
    public void setParser(Class<? extends ParserCallback> parser) {
        this.parser = parser;
    }

    /**
     * @return the responseRepository
     */
    public Repository getResponseRepository() {
        return responseRepository;
    }

    /**
     * Add response to repository
     *
     * @param clazz the api entry
     * @param bullet the {@link ru.sbtqa.tag.apifactory.repositories.Bullet} response
     */
    public void addResponseToRepository(Class<? extends ApiEntry> clazz, Bullet bullet) {
        this.responseRepository.addHeaders(clazz, bullet.getHeaders());
        this.responseRepository.addBody(clazz, bullet.getBody());
    }

    /**
     * Add response headers to repository
     *
     * @param clazz the api entry
     * @param headers the list of headers of response
     */
    public void addResponseHeadersToRepository(Class<? extends ApiEntry> clazz, Map<String, String> headers) {
        this.responseRepository.addHeaders(clazz, headers);
    }

    /**
     * Add response body to repository
     *
     * @param clazz the api entry
     * @param body the body of response
     */
    public void addResponseBodyToRepository(Class<? extends ApiEntry> clazz, String body) {
        this.responseRepository.addBody(clazz, body);
    }

    /**
     * @return the requestRepository
     */
    public Repository getRequestRepository() {
        return requestRepository;
    }

    /**
     * Add request to repository
     *
     * @param clazz the api entry
     * @param bullet the {@link ru.sbtqa.tag.apifactory.repositories.Bullet}
     * request
     */
    public void addRequestToRepository(Class<? extends ApiEntry> clazz, Bullet bullet) {
        this.requestRepository.addHeaders(clazz, bullet.getHeaders());
        this.requestRepository.addBody(clazz, bullet.getBody());
    }

    /**
     * Add request body to repository
     *
     * @param clazz the api entry
     * @param body the body of request
     */
    public void addRequestBodyToRepository(Class<? extends ApiEntry> clazz, String body) {
        this.requestRepository.addBody(clazz, body);
    }

    /**
     * Add request headers to repository
     *
     * @param clazz the api entry
     * @param headers the list of headers of request
     */
    public void addRequestHeadersToRepository(Class<? extends ApiEntry> clazz, Map<String, String> headers) {
        this.requestRepository.addHeaders(clazz, headers);
    }

    /**
     * @return the rest
     */
    public Class<? extends Rest> getRest() {
        return rest;
    }

    /**
     * @param rest the rest to set
     */
    public void setRest(Class<? extends Rest> rest) {
        this.rest = rest;
    }

    /**
     * @return the soap
     */
    public Class<? extends Soap> getSoap() {
        return soap;
    }

    /**
     * @param soap the soap to set
     */
    public void setSoap(Class<? extends Soap> soap) {
        this.soap = soap;
    }
}
