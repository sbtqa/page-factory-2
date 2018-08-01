package ru.sbtqa.tag.apifactory.entries.dependant;

import ru.sbtqa.tag.api.ApiEntry;
import ru.sbtqa.tag.api.annotation.ApiAction;
import ru.sbtqa.tag.api.rest.HTTP;

@ApiAction(method = HTTP.GET, path = "client/get", title = "dependent first")
public class PreviousFirstEntry extends ApiEntry {

}
