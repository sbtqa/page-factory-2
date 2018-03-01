package ru.sbtqa.tag.pagefactory.support.properties;

import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Properties {

    INSTANCE;
    private final Logger LOG = LoggerFactory.getLogger(Properties.class);
    private final Configuration config;
    
    Properties() {
        config = ConfigFactory.create(Configuration.class);
    }

    public Configuration getProperties() {
        return config;
    }
}
