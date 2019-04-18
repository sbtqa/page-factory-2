package ru.sbtqa.tag.api.utils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlaceholderUtils {

    private static final String PLACEHOLDER_START = "\\$\\{";
    private static final String PLACEHOLDER_FINISH = "\\}";
    private static final String QUOTE = "\"";

    private PlaceholderUtils() {
    }

    /**
     * Replace placeholders in string on parameters
     *
     * @param string replace placeholders in this string
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

        Set<Map.Entry<String, Object>> optionals = parameters.entrySet().stream()
                .filter(stringObjectEntry -> stringObjectEntry.getValue() == null)
                .collect(Collectors.toSet());

        for (Map.Entry<String, Object> parameter : optionals) {
            String toRemoveRegex = "(\"[^\"]+\"\\s*:\\s*\"?\\$\\{" + parameter.getKey() + "\\}\"?\\s*,?)";
            jsonString = jsonString.replaceAll(toRemoveRegex, "");
        }

        String orphanCommaRegex = "(,)(\\s*})";
        jsonString = jsonString.replaceAll(orphanCommaRegex, "$2");

        return jsonString;
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

        String placeholder = PLACEHOLDER_START + name + PLACEHOLDER_FINISH;
        if (newValue instanceof String) {
            if (newValue.equals("null")) {
                placeholder = QUOTE + "?" + placeholder + QUOTE + "?";
                value = "null";
            } else if (newValue.equals(QUOTE + "null" + QUOTE)) {
                placeholder = QUOTE + "?" + placeholder + QUOTE + "?";
                value = QUOTE + "null" + QUOTE;
            } else if (newValue.equals(QUOTE + QUOTE)){
                placeholder = QUOTE + "?" + placeholder + QUOTE + "?";
                value = QUOTE + QUOTE;
            }
        } else {
            placeholder = QUOTE + placeholder + QUOTE;
        }

        return string.replaceAll(placeholder, value);
    }
}
