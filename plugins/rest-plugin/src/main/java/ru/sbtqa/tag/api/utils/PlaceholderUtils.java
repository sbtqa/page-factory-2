package ru.sbtqa.tag.api.utils;

import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class PlaceholderUtils {

    private static final String QUOTE = "\"";

    private PlaceholderUtils() {}

    /**
     * Replace placeholders in string on parameters
     *
     * @param string replace placeholders in this string
     * @param parameters replace these parameters
     * @return string with replaced placeholders
     */
    public static String replaceTemplatePlaceholders(EndpointEntry entry, String string, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            if (isFieldExists(entry, parameter.getKey())) {
                try {
                    string = replacePlaceholder(string, entry.getClass()
                            .getDeclaredField(parameter.getKey()), parameter.getKey(), parameter.getValue());
                } catch (NoSuchFieldException e) {
                    throw new AutotestError("This error should never appear", e);
                }
            } else {
                string = replacePlaceholder(string, null, parameter.getKey(), parameter.getValue());
            }
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
    public static String replaceJsonTemplatePlaceholders(EndpointEntry entry, String jsonString, Map<String, Object> parameters) {
        Set<Map.Entry<String, Object>> mandatoryValues = parameters.entrySet().stream()
                .filter(stringObjectEntry -> stringObjectEntry.getValue() != null)
                .collect(Collectors.toSet());
        for (Map.Entry<String, Object> parameter : mandatoryValues) {
            if (isFieldExists(entry, parameter.getKey())) {
                Field declaredField = null;
                try {
                    declaredField = entry.getClass().getDeclaredField(parameter.getKey());
                } catch (NoSuchFieldException e) {
                    throw new AutotestError("This error should never appear", e);
                }
                jsonString = replacePlaceholder(jsonString, declaredField, parameter.getKey(), parameter.getValue());
            } else {
                jsonString = replacePlaceholder(jsonString, null, parameter.getKey(), parameter.getValue());
            }

        }

        return removeOptionals(jsonString, parameters);
    }

    private static boolean isFieldExists(EndpointEntry entry, String fieldName) {
        return Arrays.stream(FieldUtils.getAllFields(entry.getClass()))
                .anyMatch(field -> field.getName().equals(fieldName));
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
     * @param string replace placeholders in this string
     * @param declaredField endpoint field to validate type
     * @param name placeholder body (without start and finish marks)
     * @param newValue replace placeholder on this mark
     * @return string with replaced placeholder
     */
    public static String replacePlaceholder(String string, Field declaredField, String name, Object newValue) {
        String value = String.valueOf(newValue);
        String[] nullables = new String[]{"null", QUOTE + "null" + QUOTE, QUOTE + QUOTE};

        String placeholder = Pattern.quote(createPlaceholder(name));
        if (!(newValue instanceof String) || (declaredField != null && declaredField.getType() != String.class)) {
            placeholder = QUOTE + placeholder + QUOTE;
        } else if (Arrays.asList(nullables).contains(newValue)) {
            placeholder = QUOTE + "?" + placeholder + QUOTE + "?";
        }

        return string.replaceAll(placeholder, value);
    }

    public static String createPlaceholder(String placeholderName) {
        return format("${%s}", placeholderName);
    }
}
