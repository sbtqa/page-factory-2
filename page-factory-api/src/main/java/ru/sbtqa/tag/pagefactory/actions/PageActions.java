package ru.sbtqa.tag.pagefactory.actions;

public interface PageActions {

    void fill(Object element, String text);
    void click(Object element);
    void press(Object element, String key);
    void select(Object element, String option);
    void setCheckbox(Object element, boolean state);
}
