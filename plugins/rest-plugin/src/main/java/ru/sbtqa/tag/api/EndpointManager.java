package ru.sbtqa.tag.api;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.exception.ApiEntryInitializationException;
import ru.sbtqa.tag.api.exception.ApiException;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.qautils.errors.AutotestError;

public class EndpointManager {

    private static final Logger LOG = LoggerFactory.getLogger(EndpointManager.class);
    private static final ApiConfiguration PROPERTIES = ConfigFactory.create(ApiConfiguration.class);
    private static final Set<Class<?>> ENDPOINTS_CACHE = new HashSet<>();

    /**
     * Get endpoint object by name
     *
     * @param title endpoint name
     * @return endpoint instance
     * @throws ApiException if endpoint doesn't exist
     */
    public static Entry getEndpoint(String title) {
        if (null == EndpointContext.getCurrentEndpoint() || !EndpointContext.getCurrentEndpointTitle().equals(title)) {
            return getEndpointFromCache(title);
        }

        return EndpointContext.getCurrentEndpoint();
    }

    /**
     * Get endpoint by name and packageName
     *
     * @param title endpoint name
     * @return endpoint instance
     * @throws ApiException if there is an error with parameters initialize
     */
    private static Entry getEndpointFromCache(String title) {
        for (Class<?> entry : ENDPOINTS_CACHE) {
            if (null != entry.getAnnotation(ru.sbtqa.tag.api.annotation.Endpoint.class)) {
                String entryTitle = entry.getAnnotation(ru.sbtqa.tag.api.annotation.Endpoint.class).title();
                if (entryTitle != null && entryTitle.equals(title)) {
                    return bootstrapEndpoint(entry);
                }
            }
        }

        throw new AutotestError("Page object with title '" + title + "' is not registered");
    }

    private static Entry bootstrapEndpoint(Class<?> entry) {
        try {
            Constructor<Entry> constructor = (Constructor<Entry>) entry.getConstructor();
            constructor.setAccessible(true);
            Entry apiEntry = constructor.newInstance();
            EndpointContext.setCurrentEndpoint(apiEntry);
            return apiEntry;
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new ApiEntryInitializationException("Can't initialize current entry parameters", ex);
        }
    }

    public static void cacheEndpoints() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            for (ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClassesRecursive(PROPERTIES.getEndpointsPackage())) {
                ENDPOINTS_CACHE.add(info.load());
            }
        } catch (IOException ex) {
            LOG.warn("Failed to shape class info set", ex);
        }
    }
}
