package ru.sbtqa.tag.apifactory.entries.fromresponse;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.HTTP;
import ru.sbtqa.tag.api.annotation.Endpoint;

@Endpoint(method = HTTP.GET, path = "client/get", title = "dependent first")
public class PreviousFirstEndpointEntry extends EndpointEntry {

}
