package ru.sbtqa.tag.api;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.entries.apirequest.WithParamsEndpointEntry;
import ru.sbtqa.tag.api.entries.fromfeature.FirstRequestFromFeatureEntry;
import ru.sbtqa.tag.api.entries.methods.GetEndpointEntry;
import ru.sbtqa.tag.api.utils.JettyServiceUtils;
import ru.sbtqa.tag.stepdefs.ApiSteps;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.MediaType.JSON_UTF_8;

public class JunitTests {

    private static Server server;
    private static ApiSteps api;

    @BeforeClass
    public static void setupClass() {
        server = JettyServiceUtils.startJetty();
        api = ApiSteps.getInstance();
    }

    @Test
    public void simpleGetTest() {
        api.send("get with json").validate("default client");
        api.send(GetEndpointEntry.class).validate();
    }

    @Test
    public void getWithParamsTest() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query-parameter-name-1", "query-parameter-value-1");
        parameters.put("header-parameter-name-1", "header-parameter-value-1");

        api.send(WithParamsEndpointEntry.class, parameters)
                .validate("result with map", parameters);
    }

    @Test
    public void FillOnTheFlyTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("header-parameter-name-1", "header-parameter-value-1");
        headers.put("header-parameter-name-2", "header-parameter-value-2");

        Map<String, String> queries = new HashMap<>();
        queries.put("query-parameter-name-1", "query-parameter-value-1");
        queries.put("query-parameter-name-2", "query-parameter-value-2");

        Map<String, String> bodies = new HashMap<>();
        bodies.put("name", "Default_person");
        bodies.put("email", "default_person@google.com");

        api.fill(FirstRequestFromFeatureEntry.class)
                .add(ParameterType.HEADER, CONTENT_TYPE, JSON_UTF_8.toString())
                .add(ParameterType.HEADER, headers)
                .add(ParameterType.QUERY, "query-parameter-name-1", "query-parameter-value-1")
                .add(ParameterType.QUERY, queries)
                .add(ParameterType.BODY, "id", "11223344")
                .add(ParameterType.BODY, bodies)
                .send().validate();
    }

    @AfterClass
    public static void teardown() throws Exception {
        server.stop();
    }

}
