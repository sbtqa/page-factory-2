package ru.sbtqa.tag.pagefactory.support.properties;

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
