package ru.sbtqa.tag.api.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import ru.sbtqa.tag.api.dto.Client;
import ru.sbtqa.tag.api.dto.SimpleResult;
import ru.sbtqa.tag.api.utils.Default;
import ru.sbtqa.tag.api.utils.TestDataUtils;

@Path("/client")
public class ClientJsonEndpoint {

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        Client client = TestDataUtils.createDefaultClient();
        return Response.ok(client)
                .header(Default.HEADER_PARAMETER_NAME_1, Default.HEADER_PARAMETER_VALUE_1)
                .build();
    }

    @GET
    @Path("get-with-params")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWithParams(
            @HeaderParam(Default.HEADER_PARAMETER_NAME_1) String header,
            @QueryParam(Default.QUERY_PARAMETER_NAME_1) String query) {
        SimpleResult result = new SimpleResult();
        result.setResult(query + header);

        return Response.ok(result)
                .build();
    }

    @GET
    @Path("get-with-params-placeholder")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWithParams(
            @HeaderParam(Default.HEADER_PARAMETER_NAME_1) String header1,
            @HeaderParam("header2") String header2,
            @QueryParam(Default.QUERY_PARAMETER_NAME_1) String query) {
        SimpleResult result = new SimpleResult();
        result.setResult(query + header1 + header2);

        return Response.ok(result)
                .build();
    }

    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postWithParams(Client client) {
        SimpleResult result = new SimpleResult();
        result.setResult(client.getId() + client.getName() + client.getEmail());
        return Response.ok(result)
                .build();
    }

    @PUT
    @Path("put")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Client client) {
        SimpleResult result = new SimpleResult();
        result.setResult(client.getId() + client.getName() + client.getEmail());
        return Response.ok(result)
                .build();
    }

    @PATCH
    @Path("patch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(Client client) {
        SimpleResult result = new SimpleResult();
        result.setResult(client.getId() + client.getName() + client.getEmail());
        return Response.ok(result)
                .build();
    }

    @DELETE
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @QueryParam(Default.QUERY_PARAMETER_NAME_1) String param) {

        SimpleResult result = new SimpleResult();
        result.setResult(param);

        return Response.ok(result)
                .build();
    }

    @POST
    @Path("form")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response form(
            @FormParam("id") int id,
            @FormParam("name") String name,
            @FormParam("email") String email
    ) {
        SimpleResult result = new SimpleResult();
        result.setResult(id + name + email);
        return Response.ok(result)
                .build();
    }

    @GET
    @Path("cookie")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cookie(@CookieParam(Default.COOKIE_NAME) String cookie) {
        SimpleResult result = new SimpleResult();
        result.setResult(cookie);

        return Response.ok(result)
                .build();
    }

    @POST
    @Path("request-from-feature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestFromFeature(
            @QueryParam(Default.QUERY_PARAMETER_NAME_1) String query1,
            @QueryParam(Default.QUERY_PARAMETER_NAME_2) String query2,
            @HeaderParam(Default.HEADER_PARAMETER_NAME_1) String header1,
            @HeaderParam(Default.HEADER_PARAMETER_NAME_2) String header2,
            Client client) {

        SimpleResult result = new SimpleResult();
        result.setResult(String.format("q1=%s|\nq2=%s|\nh1=%s|\nh2=%s|\nid=%s|\nname=%s|\nemail=%s|\n",
                query1, query2, header1, header2, client.getId(), client.getName(), client.getEmail()));

        return Response.ok(result)
                .header(Default.HEADER_PARAMETER_NAME_1, header1)
                .header(Default.HEADER_PARAMETER_NAME_2, header2)
                .build();
    }

    @POST
    @Path("request-from-feature-2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestFromFeature2(
            @QueryParam("q2") String query1,
            @QueryParam("query-parameter-value-1") String query2,
            @HeaderParam("h") String header1,
            @HeaderParam("Content-Type") String header2,
            Client client) {

        SimpleResult result = new SimpleResult();
        result.setResult(String.format("q1=%s|q2=%s|h1=%s|h2=%s|id=%s|name=%s|email=%s|",
                query1, query2, header1, header2, client.getId(), client.getName(), client.getEmail()));

        return Response.ok(result)
                .header(Default.HEADER_PARAMETER_NAME_1, header1)
                .header(Default.HEADER_PARAMETER_NAME_2, header2)
                .build();
    }

    @POST
    @Path("replace")
    @Produces(MediaType.APPLICATION_JSON)
    public Response replace(String days) {

        BasicDBObject expected = (BasicDBObject) JSON.parse("{ \"day1\" :  null  , \"day11\" : \"null\" , " +
                "\"day13\" : \"true\" , \"day14\" : \"true\" , \"day15\" : \"\"}");

        BasicDBObject actual = (BasicDBObject) JSON.parse(days);

        SimpleResult result = new SimpleResult();
        result.setResult(String.valueOf(expected.equals(actual)));

        return Response.ok(result)
                .build();
    }
}