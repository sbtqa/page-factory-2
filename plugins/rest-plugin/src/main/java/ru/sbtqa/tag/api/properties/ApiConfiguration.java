package ru.sbtqa.tag.api.properties;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.qautils.properties.Props;

@Sources("classpath:config/application.properties")
public interface ApiConfiguration extends Config {

    @Key("api.baseURI")
    @DefaultValue("")
    String getBaseURI();

    @Key("api.endpoint.package")
    String getEndpointsPackage();

    @Key("api.template.encoding")
    @DefaultValue("UTF-8")
    String getTemplateEncoding();

    @Key("api.ssl.relaxed")
    @DefaultValue("false")
    boolean isSslRelaxed();

    static ApiConfiguration create() {
        return ConfigFactory.create(ApiConfiguration.class, Props.getProps());
    }
}
