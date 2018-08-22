package ru.sbtqa.tag.api;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
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
        // TODO send Endpoint.class
        // TODO validate with no params
        ApiSteps.getInstance().send("get with json").validate("default client");


//        ApiSteps.api.userSendRequestNoParams("get with json");
//        ApiSteps.api.userValidate("default client");
    }
}
