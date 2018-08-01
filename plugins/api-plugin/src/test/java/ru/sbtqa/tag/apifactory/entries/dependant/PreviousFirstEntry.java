package ru.sbtqa.tag.apifactory.entries.dependant;

import ru.sbtqa.tag.apifactory.ApiEntry;
import ru.sbtqa.tag.apifactory.annotation.ApiAction;
import ru.sbtqa.tag.apifactory.rest.HTTP;

@ApiAction(method = HTTP.GET, path = "client/get", title = "dependent first")
public class PreviousFirstEntry extends ApiEntry {

}
