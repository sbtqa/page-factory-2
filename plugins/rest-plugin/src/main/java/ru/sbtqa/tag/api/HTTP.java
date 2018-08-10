package ru.sbtqa.tag.api;

public enum HTTP {
    POST,
    GET,
    PUT,
    DELETE,
    PATCH,
    OPTIONS,
    HEAD;

    public static boolean isBodiless(HTTP http) {
        switch (http) {
            case GET:
            case HEAD:
            case DELETE:
                return true;
            default:
                return false;
        }
    }
}