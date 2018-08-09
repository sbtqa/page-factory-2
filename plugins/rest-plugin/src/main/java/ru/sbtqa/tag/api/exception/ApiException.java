package ru.sbtqa.tag.api.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message, Throwable e) {
        super(message, e);
    }

    public ApiException(String message) {
        super(message);
    }
}