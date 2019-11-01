package ru.sbtqa.tag.pagefactory.transformer;

import java.util.Arrays;
import java.util.HashSet;

public class ContainCondition extends Condition {

    ContainCondition(String name) {
        this.name = name.trim().toLowerCase();
        this.conditions = new HashSet<>(Arrays.asList("not contain", "не должно", "не должен"));
    }

    public boolean isPositive() {
        return !super.isConditionMatch();
    }
}