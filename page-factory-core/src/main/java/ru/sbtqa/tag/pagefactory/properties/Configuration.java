package ru.sbtqa.tag.pagefactory.properties;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"file:${TagConfigFile}", "classpath:config/application.properties"})
public interface Configuration extends Config {

    @Key("page.package")
    String getPagesPackage();

    @Key("timeout")
    @DefaultValue("20")
    int getTimeout();

    @Key("driver.shared")
    @DefaultValue("false")
    boolean getShared();


    @Key("video.enabled")
    @DefaultValue("false")
    boolean isVideoEnabled();

    @Key("video.path")
    @DefaultValue("")
    String getVideoPath();

    @Key("tasks.to.kill")
    @DefaultValue("")
    String getTasksToKill();

    @Key("screenshot.strategy")
    @DefaultValue("raw")
    String getScreenshotStrategy();


    @Key("aspects.report.fill.enabled")
    @DefaultValue("true")
    boolean isFillReportEnabled();

    @Key("aspects.report.click.enabled")
    @DefaultValue("true")
    boolean isClickReportEnabled();

    @Key("aspects.report.press.key.enabled")
    @DefaultValue("true")
    boolean isPressKeyReportEnabled();

    @Key("aspects.report.set.checkbox.enabled")
    @DefaultValue("true")
    boolean isSetCheckboxReportEnabled();

    @Key("aspects.report.select.enabled")
    @DefaultValue("true")
    boolean isSelectReportEnabled();


    @Key("fragments.enabled")
    @DefaultValue("true")
    boolean isFragmentsEnabled();

    @Key("fragments.path")
    @DefaultValue("")
    String getFragmentsPath();
}
