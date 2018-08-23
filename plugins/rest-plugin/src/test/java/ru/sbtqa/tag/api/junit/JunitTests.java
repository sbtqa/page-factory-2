package ru.sbtqa.tag.api.junit;

import org.eclipse.jetty.server.Server;

public class JunitTests {

    private static Server server;

//    @BeforeClass
//    public static void setupClass() {
//        server = JettyServiceUtils.startJetty();
//    }
//
//    @AfterClass
//    public static void teardown() throws Exception {
//        server.stop();
//    }
//
//    @Test
//    public void simpleGetTest() {
//        ApiSteps api = ApiSteps.getInstance();
//
//        api.send("get with json").validate("default client");
//        api.send(GetEndpointEntry.class).validate();
//    }
//
//    @Test
//    public void getWithParamsTest() {
//        ApiSteps api = ApiSteps.getInstance();
//
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("query-parameter-name-1", "query-parameter-value-1");
//        parameters.put("header-parameter-name-1", "header-parameter-value-1");
//
//        api.send("api request with params test", parameters)
//                .validate("result with map", parameters);
//    }
//
//
//    @Test
//    public void FillOnTheFlyTest() {
//        ApiSteps api = ApiSteps.getInstance();
//
//        Map<String, String> headers = new HashMap<>();
//        headers.put("header-parameter-name-1", "header-parameter-value-1");
//        headers.put("header-parameter-name-2", "header-parameter-value-2");
//
//        Map<String, String> queries = new HashMap<>();
//        queries.put("query-parameter-name-1", "query-parameter-value-1");
//        queries.put("query-parameter-name-2", "query-parameter-value-2");
//
//        Map<String, String> bodies = new HashMap<>();
//        bodies.put("name", "Default_person");
//        bodies.put("email", "default_person@google.com");
//
//        api.fill("first request from feature")
//                .add(ParameterType.HEADER, "Content-Type", "application/json")
//                .add(ParameterType.HEADER, headers)
//                .add(ParameterType.QUERY, "query-parameter-name-1", "query-parameter-value-1")
//                .add(ParameterType.QUERY, queries)
//                .add(ParameterType.BODY, "id", "11223344")
//                .add(ParameterType.BODY, bodies)
//                .send().validate();
//    }
}
