package ru.sbtqa.tag.api.properties;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/application.properties")
public interface ApiConfiguration extends Config {

    @Key("api.baseURI")
    String getBaseURI();

    @Key("api.endpoint.package")
    String getEndpointsPackage();

    @Key("api.template.encoding")
    @DefaultValue("UTF-8")
    String getTemplateEncoding();

    @Key("api.ssl.relaxed")
    @DefaultValue("true")
    boolean isSslRelaxed();
}
