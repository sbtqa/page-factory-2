package ru.sbtqa.tag.api.endpoints;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    @Path("delete-client")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
            @QueryParam("client") String param) {

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

        BasicDBObject expected = (BasicDBObject) JSON.parse("" +
                "{\n" +
                "  \"day1\": false,\n" +
                "  \"day11\": false,\n" +
                "  \"day12\": false,\n" +
                "  \"day13\": true,\n" +
                "  \"day14\": true,\n" +
                "  \"day15\": false\n" +
                "}");

        BasicDBObject actual = (BasicDBObject) JSON.parse(days);

        BasicDBObject result = new BasicDBObject();
        result.put("result", expected.equals(actual));

        return Response.ok(result.toJson()).build();
    }

    @POST
    @Path("typed-arrays")
    @Produces(MediaType.APPLICATION_JSON)
    public Response typedArrays(String request) {
        SimpleResult result = new SimpleResult();
        BasicDBObject response = (BasicDBObject) JSON.parse(request);
        result.setResult(response.toJson());
        return Response.ok(result).build();
    }
}