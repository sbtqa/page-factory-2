package ru.sbtqa.tag.api;

import ru.sbtqa.tag.qautils.properties.Props;

/**
 *
 *
 */
public class ApiFactory {

    private static ApiFactoryWrapper instance;

    private ApiFactory() {
        throw new IllegalAccessError("Singleton class");
    }
    
    /**
     * Get api factory
     *
     * @return api factory
     */
    public static ApiFactoryWrapper getApiFactory() {
        if (null == instance) {
            instance = new ApiFactoryWrapper(Props.get("api.entries.package"));
        }
        return instance;
    }
}
