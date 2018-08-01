package ru.sbtqa.tag.api.repositories;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.ApiEntry;

/**
 * Response or request repository. Contains as pair of ApiEntry.class and
 * {@link ru.sbtqa.tag.apifactory.repositories.Bullet} object.
 *
 *
 */
public class Repository {

    private static final Logger LOG = LoggerFactory.getLogger(Repository.class);
    
    private final String repositoryType;

    private final Map<Class<? extends ApiEntry>, Bullet> instance = new LinkedHashMap<>();
    
    /**
     * The initialization of a repository of the specified type
     *
     * @param repositoryType repository type of the directory
     * {@link ru.sbtqa.tag.apifactory.repositories.RepositoryType}
     */
    public Repository(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    /**
     * Get headers pairs name-value by ApiEntry.class
     *
     * @param apiEntry api object class of request
     * @return headers as Map
     */
    public Map<String, String> getHeaders(Class<? extends ApiEntry> apiEntry) {
        return instance.get(apiEntry).getHeaders();
    }

    /**
     * Get header value by ApiEntry.class and header name
     *
     * @param apiEntry api object class of request
     * @param headerName header name
     * @return {@link java.lang.String} header value
     */
    public String getHeader(Class<? extends ApiEntry> apiEntry, String headerName) {
        return instance.get(apiEntry).getHeader(headerName);
    }

    /**
     * Get body as String by ApiEntry.class
     *
     * @param apiEntry api object class of request
     * @return {@link java.lang.String} body
     */
    public String getBody(Class<? extends ApiEntry> apiEntry) {
        return instance.get(apiEntry).getBody();
    }

    /**
     * Add body by ApiEntry.class
     *
     * @param apiEntry api object class of request
     * @param body {@link java.lang.String} body
     */
    public void addBody(Class<? extends ApiEntry> apiEntry, String body) {
        Bullet bullet;
        if (instance.containsKey(apiEntry)) {
            bullet = instance.get(apiEntry);
        } else {
            bullet = new Bullet();
        }
        bullet.setBody(body);

        instance.put(apiEntry, bullet);
        LOG.info("Added to " + getType() + " repository key {} body {{}}", apiEntry.getName(), body);
    }

    /**
     * Add headers by ApiEntry.class
     *
     * @param apiEntry api object class of request
     * @param headers {@link java.util.Map} headers
     */
    public void addHeaders(Class<? extends ApiEntry> apiEntry, Map<String, String> headers) {
        Bullet bullet;
        if (instance.containsKey(apiEntry)) {
            bullet = instance.get(apiEntry);
        } else {
            bullet = new Bullet();
        }
        bullet.setHeaders(headers);

        instance.put(apiEntry, bullet);
        LOG.info("Added to " + getType() + " repository key {} headers {}", apiEntry.getName(), headers);
    }

    /**
     * Get last in repository
     *
     * @return {@link ru.sbtqa.tag.apifactory.repositories.Bullet} object
     */
    public Bullet getLastInRepository() {
        Bullet bullet = null;
        for (Map.Entry<Class<? extends ApiEntry>, Bullet> entry : instance.entrySet()) {
            bullet = entry.getValue();
        }
        return bullet;
    }

    /**
     * Get last api entry in repository.
     *
     * @return a {@link java.lang.Class} object.
     */
    public Class getLastEntryInRepository() {
        Class clazz = null;
        for (Map.Entry<Class<? extends ApiEntry>, Bullet> entry : instance.entrySet()) {
            clazz = entry.getKey();
        }
        return clazz;
    }
    
    /**
     * Get repository type as String
     *
     * @return {@link java.lang.String} type
     */
    public String getType() {
        return this.repositoryType;
    }
}
