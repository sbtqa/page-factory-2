package ru.sbtqa.tag.pagefactory.support.properties;

public class PropsRuntimeException extends RuntimeException {

    public PropsRuntimeException(Throwable e) {
        super(e);
    }
    
    public PropsRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public PropsRuntimeException(String message) {
        super(message);
    }
}
