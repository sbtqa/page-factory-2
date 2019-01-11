package ru.sbtqa.tag.api.utils;

public class PathUtils {

    private static final String SLASH = "/";
    private static final String SLASHES_ON_START_REGEX = "^/+";
    private static final String SLASHES_ON_END_REGEX = "/+$";
    private static final String EMPTY_LINE = "";

    private PathUtils() {}

    public static String unite(String left, String right) {
        if (left.isEmpty()) {
            return right;
        } else {
            left = left.replaceAll(SLASHES_ON_END_REGEX, EMPTY_LINE);
            right = right.replaceAll(SLASHES_ON_START_REGEX, EMPTY_LINE);

            return left + SLASH + right;
        }
    }
}
