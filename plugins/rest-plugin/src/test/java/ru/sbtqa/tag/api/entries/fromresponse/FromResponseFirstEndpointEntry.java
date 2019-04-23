package ru.sbtqa.tag.api.entries.fromresponse;

import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.pagefactory.Rest;
import ru.sbtqa.tag.pagefactory.annotations.rest.Endpoint;

@Endpoint(method = Rest.GET, path = "client/get", title = "from response first")
public class FromResponseFirstEndpointEntry extends EndpointEntry {

}
