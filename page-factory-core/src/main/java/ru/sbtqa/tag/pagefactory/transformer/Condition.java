package ru.sbtqa.tag.pagefactory.transformer;

import java.util.Set;

public abstract class Condition {

    protected String name;
    protected Set<String> conditions;

    public boolean isConditionMatch() {
        if (name == null) {
            return true;
        }
        return conditions.stream().anyMatch(negation -> negation.equals(name));
    }
}
