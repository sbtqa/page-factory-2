package ru.sbtqa.tag.pagefactory.transformer;

import java.util.Arrays;
import java.util.HashSet;

public class Presence extends Condition {

    Presence(String name) {
        this.name = name;
        this.conditions = new HashSet<>(Arrays.asList("not", "от", "не отображается", "не отображаются"));
    }

    public boolean isPresent() {
        return !super.isConditionMatch();
    }
}
