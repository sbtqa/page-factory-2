package ru.sbtqa.tag.mq.factory.enumeration;

public enum MessagePropertyType {
    TOPIC,
    PARTITION,
    OFFSET,
    KEY;

    public static MessagePropertyType get(String typeStr) {
        for (MessagePropertyType value : values()) {
            if (value.name().equalsIgnoreCase(typeStr)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Property type doesn't exists");
    }
}
