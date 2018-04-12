package ru.sbtqa.tag.pagefactory.web.properties;

import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/application.properties")
public interface Configuration extends ru.sbtqa.tag.pagefactory.properties.Configuration {

    @Key("aspects.highlight.enabled")
    @DefaultValue("false")
    boolean isHighlightEnabled();
}
