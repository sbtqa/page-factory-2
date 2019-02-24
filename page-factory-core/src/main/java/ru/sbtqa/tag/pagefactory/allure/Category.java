package ru.sbtqa.tag.pagefactory.allure;

import java.util.List;

public class Category {

    private String name;
    private String messageRegex;
    private List<String> matchedStatuses;

    public Category(String name, String messageRegex, List<String> matchedStatuses) {
        this.name = name;
        this.messageRegex = messageRegex;
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

    public List<String> getMatchedStatuses() {
        return matchedStatuses;
    }
}
