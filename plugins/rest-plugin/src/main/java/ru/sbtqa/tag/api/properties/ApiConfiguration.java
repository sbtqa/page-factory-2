package ru.sbtqa.tag.api.properties;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/application.properties")
public interface ApiConfiguration extends Config {

    @Key("api.baseURI")
    String getBaseURI();

    @Key("endpoint.package")
    String getEndpointsPackage();
}
