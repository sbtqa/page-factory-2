package ru.sbtqa.tag.pagefactory.exceptions;

public class FragmentException extends Exception {

    public FragmentException(Throwable e) {
        super(e);
    }

    public FragmentException(String message, Throwable e) {
        super(message, e);
    }

    public FragmentException(String message) {
        super(message);
    }
}
