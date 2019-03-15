package ru.sbtqa.tag.pagefactory.exceptions;

public class AllureNonCriticalError extends AssertionError {

    /**
     * @param e a {@link Throwable} object.
     */
    public AllureNonCriticalError(Throwable e) {
        super(e);
    }

    /**
     * @param message a {@link String} object.
     * @param e       a {@link Throwable} object.
     */
    public AllureNonCriticalError(String message, Throwable e) {
        super(message, e);
    }

    /**
     * @param message a {@link String} object.
     */
    public AllureNonCriticalError(String message) {
        super(message);
    }
}
