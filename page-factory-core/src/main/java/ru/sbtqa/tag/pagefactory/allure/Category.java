package ru.sbtqa.tag.pagefactory.allure;

import java.util.List;


/**
 * Allure category
 */
public class Category {

    private String name;
    private String messageRegex;
    private String traceRegex;
    private List<String> matchedStatuses;

    /**
     * Creates Allure report category
     *
     * @param name (mandatory) category name
     * @param messageRegex (optional) regex pattern to check test error message. Default ".*"
     * @param traceRegex (optional) regex pattern to check stack trace. Default ".*"
     * @param matchedStatuses (optional) list of suitable test statuses. Default ["failed", "broken", "passed", "skipped", "unknown"]
     */
    public Category(String name, String messageRegex, String traceRegex, List<String> matchedStatuses) {
        this.name = name;
        this.messageRegex = messageRegex;
        this.traceRegex = traceRegex;
        this.matchedStatuses = matchedStatuses;
    }

    @Override
    public int hashCode() {
        return (Category.class.hashCode() + getName()).hashCode();
    }

    @Override
    public boolean equals(Object category) {
        if (!(category instanceof Category)) {
            return false;
        } else {
            return ((Category) category).getName().equals(getName());
        }
    }

    public String getName() {
        return name;
    }

    public String getMessageRegex() {
        return messageRegex;
    }

    public String getTraceRegex() {
        return traceRegex;
    }

    public List<String> getMatchedStatuses() {
        return matchedStatuses;
    }
}
