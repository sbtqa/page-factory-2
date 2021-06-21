package ru.sbtqa.tag.pagefactory.elements.hint;

import org.openqa.selenium.WebElement;

public interface Hint extends WebElement {

    void open();
    void close();
    boolean isOpen();
    <T extends WebElement> T getContent();
    default <T extends WebElement> T getOpenButton(){
        return (T) this;
    }
}
