package ru.sbtqa.tag.api.junit;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbtqa.tag.api.entries.methods.GetEndpointEntry;
import ru.sbtqa.tag.api.utils.JettyServiceUtils;
import ru.sbtqa.tag.stepdefs.ApiSteps;

public class JunitTests {

    private static Server server;

    @BeforeClass
    public static void setupClass() {
        server = JettyServiceUtils.startJetty();
    }

    @AfterClass
    public static void teardown() throws Exception {
        server.stop();
    }

    @Test
    public void endpointsGetTest() {
        ApiSteps api = ApiSteps.getInstance();

        api.send("get with json").validate("default client");
        api.send(GetEndpointEntry.class).validate();
    }
}
