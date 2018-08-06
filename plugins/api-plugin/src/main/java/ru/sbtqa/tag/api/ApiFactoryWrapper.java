package ru.sbtqa.tag.api;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;
import ru.sbtqa.tag.api.exception.ApiException;
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

    /**
     * Constructor for ApiFactory.
     *
     * @param pagesPackage a {@link java.lang.String} object.
     */
    public ApiFactoryWrapper(String pagesPackage) {
        this.entriesPackage = pagesPackage;
    }

    /**
     * Get api entry object by name
     *
     * @param title api entry name
     * @return api entry instance
     * @throws ApiException if api entry doesn't exist
     */
    public ApiEntry getApiEntry(String title) {
        if (null != currentEntry) {
            currentEntry = getApiEntry(currentEntry.getClass().getPackage().getName(), title);
        }
        if (null == currentEntry) {
            currentEntry = getApiEntry(entriesPackage, title);

        }
        if (null == currentEntry) {
            throw new ApiException("Api entry with name '" + title + "' is not registered");
        }
        return currentEntry;
    }

    /**
     * Get api entry by name and packageName
     *
     * @param packageName api entry package name
     * @param title api entry name
     * @return api entry instance
     * @throws ApiException if there is an error with parameters initialize
     */
    private ApiEntry getApiEntry(String packageName, String title) {
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
            if (null != entry.getAnnotation(Endpoint.class)) {
                String entryTitle = entry.getAnnotation(Endpoint.class).title();
                String entryPath = entry.getAnnotation(Endpoint.class).path();
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
     * @throws ApiException if there are an error with api entry initialize
     */
    public ApiEntry getCurrentApiEntry() {
        if (null == currentEntry) {
            throw new ApiEntryInitializationException("Current api entry not initialized");
        } else {
            return currentEntry;
        }
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
}
