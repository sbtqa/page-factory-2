package ru.sbtqa.tag.apifactory.utils;

import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import ru.sbtqa.tag.apifactory.endpoints.ClientJsonEndpoint;

public class JettyServiceUtils {

    private static final int PORT = 9998;
    private static final String HOST = "http://localhost/";

    public static Server startJetty() {
        URI uri = UriBuilder.fromUri(HOST).port(PORT).build();
        ResourceConfig config = new ResourceConfig();
        config.register(ClientJsonEndpoint.class);

        return JettyHttpContainerFactory.createServer(uri, config);
    }
}
