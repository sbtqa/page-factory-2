package ru.sbtqa.tag.pagefactory.checks;

public interface PageChecks {

    boolean checkEquality(Object element, String text);
    boolean checkEmptiness(Object element);
}
