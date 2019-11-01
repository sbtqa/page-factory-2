package ru.sbtqa.tag.pagefactory.transformer.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Condition {

    private String name;
    private Set<String> negations = new HashSet<>(Arrays.asList("not contain", "не должно", "не должен"));

    Condition(String name) {
        this.name = name.trim().toLowerCase();
    }

    public boolean isPositive() {
        if (name == null) {
            return true;
        }
        return negations.stream().noneMatch(negation -> negation.equals(name));
    }
}