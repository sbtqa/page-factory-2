package ru.sbtqa.tag.apifactory.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import ru.sbtqa.tag.apifactory.dto.Client;
import ru.sbtqa.tag.apifactory.dto.SimpleResult;
import ru.sbtqa.tag.apifactory.utils.Default;
import ru.sbtqa.tag.apifactory.utils.TestDataUtils;

@Path("/client")
public class ClientJsonEndpoint {

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        Client client = TestDataUtils.createDefaultClient();
        return Response.ok(client)
                .header("first-name", "name-value-1")
                .build();
    }

    @GET
    @Path("get-with-params")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWithParams(
            @HeaderParam(Default.HEADER_NAME) String header,
            @QueryParam(Default.PARAMETER_NAME1) String param) {
        SimpleResult result = new SimpleResult();
        result.setResult(header + param);

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
            @QueryParam(Default.PARAMETER_NAME1) String param) {

        SimpleResult result = new SimpleResult();
        result.setResult(param);

        return Response.ok(result)
                .build();
    }
}