package ru.sbtqa.tag.pagefactory.transformer;

import java.util.Arrays;
import java.util.HashSet;

public class ClickVariation extends Condition {

    public ClickVariation(String name) {
        this.name = name;
        this.conditions = new HashSet<>(Arrays.asList("double-click", "двойным кликом"));
    }

    public boolean isDoubleClick() {
        return !super.isConditionMatch();
    }
}
