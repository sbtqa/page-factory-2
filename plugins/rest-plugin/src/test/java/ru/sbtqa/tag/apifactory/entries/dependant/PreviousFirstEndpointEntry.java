package ru.sbtqa.tag.apifactory.entries.dependant;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.rest.HTTP;

@ru.sbtqa.tag.api.annotation.Endpoint(method = HTTP.GET, path = "client/get", title = "dependent first")
public class PreviousFirstEndpointEntry extends EndpointEntry {

}
