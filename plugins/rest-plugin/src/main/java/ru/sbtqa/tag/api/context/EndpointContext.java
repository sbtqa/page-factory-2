package ru.sbtqa.tag.api.context;

import ru.sbtqa.tag.api.Entry;

public class EndpointContext {

    private static String currentEndpointTitle;
    private static Entry currentEndpoint;

    private EndpointContext() {}

    public static String getCurrentEndpointTitle() {
        return currentEndpointTitle;
    }

    private static void setCurrentEndpointTitle(String currentEndpointTitle) {
        EndpointContext.currentEndpointTitle = currentEndpointTitle;
    }

    public static Entry getCurrentEndpoint() {
        return currentEndpoint;
    }

    public static void setCurrentEndpoint(Entry currentEndpoint) {
        EndpointContext.currentEndpoint = currentEndpoint;
        EndpointContext.setCurrentEndpointTitle(currentEndpoint.getTitle());
    }
}
