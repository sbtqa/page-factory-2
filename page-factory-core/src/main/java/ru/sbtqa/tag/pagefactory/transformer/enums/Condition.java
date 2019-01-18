package ru.sbtqa.tag.pagefactory.transformer.enums;

import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import cucumber.runtime.CucumberException;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;

@XStreamConverter(ConditionTransformer.class)
public enum Condition {

    NEGATIVE("не"),
    NEGATIVE_EN("not"),
    POSITIVE(null);

    private final String name;

    Condition(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    public static Condition fromString(String name) {
        if (name == null) {
            return Condition.POSITIVE;
        } else {
            for (Condition value : Condition.values()) {
                if (value.name != null && value.name.equalsIgnoreCase(name.trim())) {
                    return value;
                }
            }
            throw new CucumberException("Incorrect enum-value in steps: " + name);
        }
    }
}
