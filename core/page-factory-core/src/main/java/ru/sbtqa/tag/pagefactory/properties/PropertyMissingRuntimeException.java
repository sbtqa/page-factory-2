package ru.sbtqa.tag.pagefactory.properties;

public class PropertyMissingRuntimeException extends RuntimeException {

    public PropertyMissingRuntimeException(Throwable e) {
        super(e);
    }
    
    public PropertyMissingRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public PropertyMissingRuntimeException(String message) {
        super(message);
    }
}
