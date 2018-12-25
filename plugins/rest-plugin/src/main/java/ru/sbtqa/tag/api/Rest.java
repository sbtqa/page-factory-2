package ru.sbtqa.tag.api;

public enum Rest {
    POST,
    GET,
    PUT,
    DELETE,
    PATCH,
    OPTIONS,
    HEAD;

    /**
     * Whether the rest method has body or not
     * @param rest rest method
     * @return true if the method doesn't have the body
     */
    public static boolean isBodiless(Rest rest) {
        switch (rest) {
            case GET:
            case HEAD:
                return true;
            default:
                return false;
        }
    }
}