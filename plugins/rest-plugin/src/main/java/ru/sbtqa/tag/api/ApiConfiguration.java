package ru.sbtqa.tag.api;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/application.properties")
public interface ApiConfiguration extends Config {

    @Key("api.baseURI")
    String getBaseURI();
}
