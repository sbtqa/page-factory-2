package ru.sbtqa.tag.api.properties;

import org.aeonbits.owner.Config;
import ru.sbtqa.tag.pagefactory.properties.Configuration;

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

    @Key("api.template.remove.optional")
    @DefaultValue("true")
    boolean isRemoveOptional();

    @Key("api.template.remove.empties")
    @DefaultValue("false")
    boolean shouldRemoveEmptyObjects();

    static ApiConfiguration create() {
        return Configuration.init(ApiConfiguration.class);
    }
}
