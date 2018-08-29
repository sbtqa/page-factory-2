package ru.sbtqa.tag.api.utils;

import java.util.Map;

public class PlaceholderUtils {

    private static final String PLACEHOLDER_START = "\\$\\{";
    private static final String PLACEHOLDER_FINISH = "\\}";
    private static final String QUOTE = "\"";

    private PlaceholderUtils() {}

    /**
     * Replace placeholders in string on parameters
     *
     * @param string replace placeholders in this string
     * @param parameters replace these parameters
     * @return string with replaced placeholders
     */
    public static String replacePlaceholders(String string, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            replacePlaceholder(string, parameter.getKey(), parameter.getValue());
        }

        return string;
    }

    /**
     * Replace placeholder in string on value
     *
     * @param string replace placeholders in this string
     * @param name placeholder body (without start and finish marks)
     * @param newValue replace placeholder on this mark
     * @return string with replaced placeholder
     */
    public static String replacePlaceholder(String string, String name, Object newValue) {
        String value = String.valueOf(newValue);

        String placeholder;
        if (newValue instanceof String) {
            placeholder = PLACEHOLDER_START + name + PLACEHOLDER_FINISH;
        } else {
            placeholder = QUOTE + PLACEHOLDER_START + name + PLACEHOLDER_FINISH + QUOTE;
        }

        string = string.replaceAll(placeholder, value);

        return string;
    }
}
