package ru.sbtqa.tag.api.exception;

import ru.sbtqa.tag.qautils.errors.AutotestError;

public class RestPluginException extends AutotestError {

    public RestPluginException(String message, Throwable e) {
        super(message, e);
    }

    public RestPluginException(String message) {
        super(message);
    }
}