package ru.sbtqa.tag.api;

import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbtqa.tag.api.environment.ApiEnvironment;
import ru.sbtqa.tag.api.manager.EndpointManager;
import ru.sbtqa.tag.api.utils.JettyServiceUtils;
import ru.sbtqa.tag.stepdefs.ApiSteps;
import ru.sbtqa.tag.stepdefs.CoreSetupSteps;

public class JunitTest {
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
    public void endpointsGetTest(){
        ApiSteps.api.userSendRequestNoParams("get with json");
        ApiSteps.api.userValidate("default client");
    }
}
