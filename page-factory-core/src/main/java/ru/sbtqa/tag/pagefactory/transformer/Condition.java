package ru.sbtqa.tag.pagefactory.transformer;

import java.util.Set;

public abstract class Condition {

    protected String name;
    Set<String> conditions;

    boolean isConditionMatch() {
        if (name == null) {
            return false;
        }
        return conditions.stream().noneMatch(negation -> negation.equals(name));
    }
}
