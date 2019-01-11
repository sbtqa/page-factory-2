package ru.sbtqa.tag.api.manager;

import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.annotation.Endpoint;
import ru.sbtqa.tag.api.context.EndpointContext;
import ru.sbtqa.tag.api.exception.RestPluginException;
import ru.sbtqa.tag.api.properties.ApiConfiguration;
import ru.sbtqa.tag.qautils.errors.AutotestError;

public class EndpointManager {

    private static final Logger LOG = LoggerFactory.getLogger(EndpointManager.class);
    private static final ApiConfiguration PROPERTIES = ApiConfiguration.create();
    private static final ThreadLocal<Set<Class<?>>> ENDPOINTS_CACHE = ThreadLocal.withInitial(HashSet::new);

    private EndpointManager() {
    }

    public static EndpointEntry getEndpoint(String title) {
        if (null == EndpointContext.getCurrentEndpoint() || !EndpointContext.getCurrentEndpointTitle().equals(title)) {
            return getEndpointFromCache(title);
        }

        return EndpointContext.getCurrentEndpoint();
    }

    private static EndpointEntry getEndpointFromCache(String title) {
        for (Class<?> entry : ENDPOINTS_CACHE.get()) {
            if (null != entry.getAnnotation(Endpoint.class)
                    && entry.getAnnotation(Endpoint.class).title().equals(title)) {
                return bootstrapEndpoint(entry);
            }
        }

        throw new AutotestError("Endpoint entry with title '" + title + "' is not registered");
    }

    public static EndpointEntry getEndpoint(Class endpointClass) {
        if (null == EndpointContext.getCurrentEndpoint() || !EndpointContext.getCurrentEndpoint().getClass().equals(endpointClass)) {
            return getEndpointFromCache(endpointClass);
        }

        return EndpointContext.getCurrentEndpoint();
    }

    private static EndpointEntry getEndpointFromCache(Class endpointClass) {
        for (Class<?> entry : ENDPOINTS_CACHE.get()) {
            if (null != entry.getAnnotation(Endpoint.class)
                    && entry.equals(endpointClass)) {
                return bootstrapEndpoint(entry);
            }
        }
        throw new AutotestError("Endpoint entry with class '" + endpointClass.getName() + "' is not registered");
    }

    private static EndpointEntry bootstrapEndpoint(Class<?> entry) {
        try {
            Constructor<EndpointEntry> constructor = (Constructor<EndpointEntry>) entry.getConstructor();
            constructor.setAccessible(true);
            EndpointEntry endpoint = constructor.newInstance();
            EndpointContext.setCurrentEndpoint(endpoint);
            return endpoint;
        } catch (NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RestPluginException("Can't initialize current entry parameters", ex);
        }
    }

    public static void cacheEndpoints() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Set<Class<?>> cache = new HashSet<>();
            for (ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClassesRecursive(PROPERTIES.getEndpointsPackage())) {
                cache.add(info.load());
            }
            ENDPOINTS_CACHE.set(cache);
        } catch (IOException ex) {
            LOG.warn("Failed to shape class info set", ex);
        }
    }
}
