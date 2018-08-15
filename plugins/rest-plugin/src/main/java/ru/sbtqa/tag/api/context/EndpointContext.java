package ru.sbtqa.tag.api.context;

import ru.sbtqa.tag.api.EndpointEntry;

public class EndpointContext {

    private static String currentEndpointTitle;
    private static EndpointEntry currentEndpoint;

    private EndpointContext() {}

    public static String getCurrentEndpointTitle() {
        return currentEndpointTitle;
    }

    private static void setCurrentEndpointTitle(String currentEndpointTitle) {
        EndpointContext.currentEndpointTitle = currentEndpointTitle;
    }

    public static EndpointEntry getCurrentEndpoint() {
        return currentEndpoint;
    }

    public static void setCurrentEndpoint(EndpointEntry currentEndpoint) {
        EndpointContext.currentEndpoint = currentEndpoint;
        EndpointContext.setCurrentEndpointTitle(currentEndpoint.getTitle());
    }
}
