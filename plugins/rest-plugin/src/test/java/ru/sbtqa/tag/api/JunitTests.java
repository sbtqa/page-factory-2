package ru.sbtqa.tag.api;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.sbtqa.tag.api.annotation.ParameterType;
import ru.sbtqa.tag.api.entries.apirequest.ApiRequestWithMutator;
import ru.sbtqa.tag.api.entries.apirequest.WithParamsEndpointEntry;
import ru.sbtqa.tag.api.entries.apirequest.WithParamsPlaceholdersEndpointEntry;
import ru.sbtqa.tag.api.entries.fromfeature.FirstRequestFromFeatureEntry;
import ru.sbtqa.tag.api.entries.methods.GetEndpointEntry;
import ru.sbtqa.tag.api.entries.template.TypedArraysEntry;
import ru.sbtqa.tag.api.junit.ApiSteps;
import ru.sbtqa.tag.api.utils.JettyServiceUtils;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.MediaType.JSON_UTF_8;
import static ru.sbtqa.tag.api.utils.CastUtils.toDataTable;

@RunWith(JUnit4.class)
public class JunitTests {

    private static Server server;

    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        server = JettyServiceUtils.startJetty();
    }

    @Test
    public void simpleGetTest() {
        ApiSteps.getInstance().send("get with json").validate("default client");
        ApiSteps.getInstance().send(GetEndpointEntry.class).validate();
    }

    @Test
    public void getWithParamsTest() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query-parameter-name-1", "query-parameter-value-1");
        parameters.put("header-parameter-name-1", "header-parameter-value-1");

        ApiSteps.getInstance().send(WithParamsEndpointEntry.class, parameters)
                .validate("result with map", parameters);
    }

    @Test
    public void getWithParamsPlaceholdersTest() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("parameter-1", "parameter-value-1");
        parameters.put("parameter-2", "parameter-value-2");
        parameters.put("parameter-3", "Alex");
        parameters.put("header2", "header-value-2");

        Map<String, String> resultParameters = new HashMap<>();
        resultParameters.put("query-parameter-name-1", "new-parameter-value-1");
        resultParameters.put("header-parameter-name-1", "[{\"value\":\"parameter-value-2\", \"visible\":true, \"name\":\"Alex\"}]");
        resultParameters.put("header2", "header-value-2");

        ApiSteps.getInstance().send(WithParamsPlaceholdersEndpointEntry.class, parameters)
                .validate("result with map placeholders", resultParameters);
    }

    @Test
    public void fillOnTheFlyTest() {
        Map<String, String> headers = new HashMap<>();
        headers.put("header-parameter-name-1", "header-parameter-value-1");
        headers.put("header-parameter-name-2", "header-parameter-value-2");

        Map<String, String> queries = new HashMap<>();
        queries.put("query-parameter-name-1", "query-parameter-value-1");
        queries.put("query-parameter-name-2", "query-parameter-value-2");

        Map<String, String> bodies = new HashMap<>();
        bodies.put("name", "Default_person");
        bodies.put("email", "default_person@google.com");

        ApiSteps.getInstance().fill(FirstRequestFromFeatureEntry.class)
                .add(ParameterType.HEADER, CONTENT_TYPE, JSON_UTF_8.toString())
                .add(ParameterType.HEADER, headers)
                .add(ParameterType.QUERY, "query-parameter-name-1", "query-parameter-value-1")
                .add(ParameterType.QUERY, queries)
                .add(ParameterType.BODY, "id", 11223344)
                .add(ParameterType.BODY, bodies)
                .send().validate();
    }

    @Test
    public void mutatorTest() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query-parameter-name-1", "query-parameter-value-1");
        parameters.put("header-parameter-name-1", null);

        ApiSteps.getInstance().send(ApiRequestWithMutator.class, parameters);

        parameters.replace("query-parameter-name-1", "query-parameter-value-1".toUpperCase());
        parameters.replace("header-parameter-name-1", "not null string");

        ApiSteps.getInstance().validate("result with mutated values", toDataTable(parameters));
    }

    @Test @Ignore
    public void typedArrayTest() {
        ApiSteps.getInstance().fill(TypedArraysEntry.class)
                .add(ParameterType.HEADER, CONTENT_TYPE, JSON_UTF_8.toString())
                .add(ParameterType.BODY, "valuesString", new BodyArray<>("\"should be quoted\", \\,one, two, three, four", String.class))
                .add(ParameterType.BODY, "valuesInt", new BodyArray<>("1, 2, 3, 4, 5", Integer.class))
                .add(ParameterType.BODY, "valuesBoolean", new BodyArray<>("true, false, true, true, true", Boolean.class))
                .send().validate("result");
    }

    @AfterClass
    public static void teardown() throws Exception {
        server.stop();
    }
}

