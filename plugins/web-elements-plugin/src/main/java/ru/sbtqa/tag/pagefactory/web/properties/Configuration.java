package ru.sbtqa.tag.pagefactory.web.properties;

import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/application.properties")
public interface Configuration extends ru.sbtqa.tag.pagefactory.properties.Configuration {

    @Key("aspects.highlight.enabled")
    @DefaultValue("false")
    boolean isHighlightEnabled();

    @Key("aspects.click.actions.enabled")
    @DefaultValue("false")
    boolean isClickViaSeleniumActionsEnabled();

    @Key("aspects.scroll.to.element.enabled")
    @DefaultValue("false")
    boolean isScrollToElementEnabled();
}
