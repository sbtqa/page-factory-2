package ru.sbtqa.tag.pagefactory.optional;

import cucumber.runtime.Argument;

public class ArgumentCustom extends Argument {

    private Boolean isModified = false;

    public ArgumentCustom(Integer offset, String val, Boolean isModified) {
        super(offset, val);
        this.isModified = isModified;
    }

    public boolean isModified() {
        return isModified;
    }
}
