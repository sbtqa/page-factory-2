package ru.sbtqa.tag.pagefactory.html.properties;

import org.aeonbits.owner.Config.Sources;
import ru.sbtqa.tag.pagefactory.properties.Configuration;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

@Sources("classpath:config/application.properties")
public interface HtmlConfiguration extends WebConfiguration {

    @Key("use.test.id")
    @DefaultValue("false")
    boolean getUseTestId();

    @Key("verify.page.before.action")
    @DefaultValue("false")
    boolean getVerifyPageBeforeAction();

    @Key("ui.types")
    @DefaultValue("src/test/resources/type/types.json")
    String geUiTypes();

    @Key("verify.page")
    @DefaultValue("false")
    boolean getVerifyPage();

    @Key("plugins.html.decorator.fqcn")
    @DefaultValue("ru.sbtqa.tag.pagefactory.html.loader.decorators.CustomHtmlElementDecorator")
    String getDecoratorFullyQualifiedClassName();

    static HtmlConfiguration create() {
        return Configuration.init(HtmlConfiguration.class);
    }
}
