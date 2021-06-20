package ru.sbtqa.tag.pagefactory.properties;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.qautils.properties.Props;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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


    @Key("selenoid.version")
    @DefaultValue("")
    String getSelenoidVersion();

    @Key("selenoid.enableVNC")
    @DefaultValue("false")
    boolean getSelenoidEnableVNC();

    @Key("selenoid.screenResolution")
    @DefaultValue("")
    String getSelenoidScreenResolution();

    @Key("selenoid.enableVideo")
    @DefaultValue("false")
    boolean getSelenoidEnableVideo();

    @Key("selenoid.enableLog")
    @DefaultValue("false")
    boolean getSelenoidEnableLog();

    @Key("selenoid.logName")
    @DefaultValue("")
    String getSelenoidLogName();

    @Key("selenoid.video.name")
    @DefaultValue("")
    String getSelenoidVideoName();

    @Key("selenoid.video.screenSize")
    @DefaultValue("")
    String getSelenoidVideoScreenSize();

    @Key("selenoid.video.frameRate")
    @DefaultValue("")
    String getSelenoidVideoFrameRate();

    @Key("selenoid.nameOfTests")
    @DefaultValue("")
    String getSelenoidNameOfTests();

    @Key("selenoid.timeZone")
    @DefaultValue("")
    String getSelenoidTimeZone();

    @Key("selenoid.hostEntries")
    @DefaultValue("")
    String getSelenoidHostEntries();

    @Key("selenoid.applicationContainers")
    @DefaultValue("")
    String getSelenoidApplicationContainers();

    @Key("selenoid.containerLabels")
    @DefaultValue("")
    String getSelenoidContainerLabels();

    @Key("selenoid.sessionTimeout")
    @DefaultValue("")
    String getSelenoidSessionTimeout();


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

    @Key("aspects.report.error.source.enabled")
    @DefaultValue("false")
    boolean isReportXmlAttachEnabled();


    @Key("fragments.enabled")
    @DefaultValue("true")
    boolean isFragmentsEnabled();

    @Key("fragments.path")
    @DefaultValue("")
    String getFragmentsPath();


    @Key("data.initial.collection")
    @DefaultValue("")
    String getDataInitialCollection();

    @Key("data.type")
    @DefaultValue("stash")
    String getDataType();

    @Key("data.folder")
    @DefaultValue("")
    String getDataFolder();

    @Key("data.extension")
    @DefaultValue("")
    String getDataExtension();

    @Key("data.uri")
    @DefaultValue("")
    String getDataUri();

    @Key("data.db")
    @DefaultValue("")
    String getDataDb();

    @Key("data.arrayDelimiter")
    @DefaultValue(",")
    String getDataArrayDelimiter();


    @Key("junit.lang")
    @DefaultValue("ru")
    String getJunitLang();


    @Key("db.${name}.url")
    String getDbUrl();

    @Key("generators.class")
    @DefaultValue("")
    String getGeneratorsClass();

    static Configuration create() {
        return create(new HashMap());
    }

    static Configuration create(Map properties) {
        return Configuration.init(Configuration.class, properties);
    }

    static <T extends Config> T init(Class<T> configuration) {
        return init(configuration, new HashMap());
    }

    static <T extends Config> T init(Class<T> configuration, Map properties) {
        java.util.Properties allProps = Props.getProps();
        allProps.putAll(properties);

        Arrays.asList(configuration.getMethods())
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
}
