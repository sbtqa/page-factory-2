package ru.sbtqa.tag.pagefactory.properties;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.qautils.properties.Props;

public interface Configuration extends Config {

    @Key("page.package")
    String getPagesPackage();

    @Key("timeout")
    @DefaultValue("20")
    int getTimeout();

    @Key("driver.shared")
    @DefaultValue("false")
    boolean getShared();

    @Key("stash.shared")
    @DefaultValue("false")
    boolean getStashShared();

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

    @Key("data.initial.collection")
    @DefaultValue("")
    String getDataInitialCollection();

    static <T extends Config> T init(Class<T> configuration) {
        java.util.Properties allProps = Props.getProps();

        Arrays.asList(configuration.getMethods()).stream()
                .forEach((Method method) -> {
                    if (method.isAnnotationPresent(Key.class)) {
                        String annotationValue = method.getAnnotation(Key.class).value();
                        if (null != System.getenv(annotationValue)) {
                            allProps.put(annotationValue, System.getenv(annotationValue));
                        }
                    }
                });

        allProps.putAll(System.getProperties());

        return ConfigFactory.create(configuration, allProps);
    }

    static Configuration create() {
        return ConfigFactory.create(Configuration.class, Props.getProps());
    }
}
