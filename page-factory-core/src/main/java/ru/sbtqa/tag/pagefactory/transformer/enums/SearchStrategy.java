package ru.sbtqa.tag.pagefactory.transformer.enums;//package ru.sbt.qa.platform.ui.core.enums;

// FIXME
//import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import cucumber.runtime.CucumberException;
//import ru.sbtqa.tag.pagefactory.transformer.SearchTransformer;
import static java.lang.String.format;

//@XStreamConverter(SearchTransformer.class)
public enum SearchStrategy {
    EQUALS(null),
    CONTAINS("по частичному совпадению с");

    private final String name;

    SearchStrategy(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    public static SearchStrategy fromString(String name) {
        if (name == null) {
            return SearchStrategy.EQUALS;
        }
        for (SearchStrategy value : SearchStrategy.values()) {
            if (value.name != null && value.name.equalsIgnoreCase(name.trim())) {
                return value;
            }
        }
        throw new CucumberException("Incorrect enum-value in steps:" + name);
    }
}
