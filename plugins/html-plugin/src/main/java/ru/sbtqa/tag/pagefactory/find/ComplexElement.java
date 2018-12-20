package ru.sbtqa.tag.pagefactory.find;

import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebElement;

class ComplexElement<T extends WebElement> {

    public static final String ELEMENT_SEPARATOR = "->";
    private T element;
    private int currentPosition = 0;
    private final List<String> elementPath;
    private final String fullElementPath;
    private final boolean waitAppear;
    private boolean isPresent = true;

    public ComplexElement(T element, String fullElementPath, boolean waitAppear) {
        this.element = element;
        this.fullElementPath = fullElementPath;
        this.elementPath = Arrays.asList(fullElementPath.split(ELEMENT_SEPARATOR));
        this.waitAppear = waitAppear;
    }

    public boolean isWaitAppear() {
        return waitAppear;
    }
        
    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.isPresent = element != null;
        this.element = element;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<String> getElementPath() {
        return elementPath;
    }
    
    public String getCurrentName() {
        return elementPath.get(currentPosition);
    }

    public String getFullElementPath() {
        return fullElementPath;
    }
    
    public boolean isPresent(){
        return isPresent;
    }
}
