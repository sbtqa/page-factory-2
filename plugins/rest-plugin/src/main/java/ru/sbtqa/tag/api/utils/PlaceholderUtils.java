package ru.sbtqa.tag.api.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class PlaceholderUtils {

    private static final String QUOTE = "\"";

    private PlaceholderUtils() {
    }

    /**
     * Replace placeholders in string on parameters
     *
     * @param string     replace placeholders in this string
     * @param parameters replace these parameters
     * @return string with replaced placeholders
     */
    public static String replaceTemplatePlaceholders(String string, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            string = replacePlaceholder(string, parameter.getKey(), parameter.getValue());
        }

        return string;
    }

    /**
     * Replace Json template placeholders in string on parameters
     *
     * @param jsonString replace placeholders in this json string
     * @param parameters replace these parameters
     * @return json string with replaced placeholders
     */
    public static String replaceJsonTemplatePlaceholders(String jsonString, Map<String, Object> parameters) {

        Set<Map.Entry<String, Object>> mandatoryValues = parameters.entrySet().stream()
                .filter(stringObjectEntry -> stringObjectEntry.getValue() != null)
                .collect(Collectors.toSet());
        for (Map.Entry<String, Object> parameter : mandatoryValues) {
            jsonString = replacePlaceholder(jsonString, parameter.getKey(), parameter.getValue());
        }

        return removeOptionals(jsonString, parameters);
    }


    private static String removeOptionals(String jsonString, Map<String, Object> parameters) {
        Set<Map.Entry<String, Object>> optionals = parameters.entrySet().stream()
                .filter(stringObjectEntry -> stringObjectEntry.getValue() == null)
                .collect(Collectors.toSet());

        for (Map.Entry<String, Object> parameter : optionals) {
            String toRemoveRegex = "(\"[^\"]+\"\\s*:\\s*\"?\\$\\{" + parameter.getKey() + "\\}\"?\\s*,?)";
            jsonString = jsonString.replaceAll(toRemoveRegex, "");
        }

        String orphanCommaRegex = "(,)(\\s*})";
        return jsonString.replaceAll(orphanCommaRegex, "$2");
    }

    /**
     * Replace placeholder in string on value
     *
     * @param string   replace placeholders in this string
     * @param name     placeholder body (without start and finish marks)
     * @param newValue replace placeholder on this mark
     * @return string with replaced placeholder
     */
    public static String replacePlaceholder(String string, String name, Object newValue) {
        String value = String.valueOf(newValue);
        String[] nullables = new String[]{"null", QUOTE + "null" + QUOTE, QUOTE + QUOTE};

        String placeholder = createPlaceholderRegex(name);
        if (!(newValue instanceof String)) {
            placeholder = QUOTE + placeholder + QUOTE;
        } else if (Arrays.asList(nullables).contains(newValue)) {
            placeholder = QUOTE + "?" + placeholder + QUOTE + "?";
        }

        return string.replaceAll(placeholder, value);
    }

    public static String createPlaceholder(String placeholderName) {
        return format("${%s}", placeholderName);
    }

    private static String createPlaceholderRegex(String value) {
        return format("\\$\\{%s\\}", value);
    }
}
