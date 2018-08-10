package ru.sbtqa.tag.api.exception;

public class RestPluginException extends RuntimeException {

    public RestPluginException(String message, Throwable e) {
        super(message, e);
    }

    public RestPluginException(String message) {
        super(message);
    }
}