package ru.sbtqa.tag.pagefactory.exceptions;

public class FactoryRuntimeException extends RuntimeException {

    /**
     * 
     * @param e  TODO
     */
    public FactoryRuntimeException(Throwable e) {
        super(e);
    }
    
    /**
     *
     * @param message a {@link String} object.
     * @param e a {@link Throwable} object.
     */
    public FactoryRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    /**
     *
     * @param message a {@link String} object.
     */
    public FactoryRuntimeException(String message) {
        super(message);
    }

}
