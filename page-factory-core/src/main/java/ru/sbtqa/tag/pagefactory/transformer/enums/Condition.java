package ru.sbtqa.tag.pagefactory.transformer.enums;

import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import cucumber.runtime.CucumberException;
import ru.sbtqa.tag.pagefactory.transformer.ConditionTransformer;

@XStreamConverter(ConditionTransformer.class)
public enum Condition {
    
    NEGATIVE("не"),
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
        }
        if ("not".equalsIgnoreCase(name.trim()) || Condition.NEGATIVE.getValue().equalsIgnoreCase(name.trim())) {
            return Condition.NEGATIVE;
        }
        throw new CucumberException("Incorrect enum-value in steps: " + name);
    }
}
