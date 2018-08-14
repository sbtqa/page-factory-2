package ru.sbtqa.tag.api.entries.fromresponse;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.Rest;
import ru.sbtqa.tag.api.annotation.Endpoint;

@Endpoint(method = Rest.GET, path = "client/get", title = "dependent first")
public class PreviousFirstEndpointEntry extends EndpointEntry {

}
