package ru.sbtqa.tag.api.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import org.apache.commons.lang3.reflect.FieldUtils;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.qautils.errors.AutotestError;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class PlaceholderUtils {

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
    public static String replaceTemplatePlaceholders(EndpointEntry entry, String string, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            String parameterName = parameter.getKey();
            Object parameterValue = parameter.getValue();

            if (isFieldExists(entry, parameterName)) {
                Field declaredField = FieldUtils.getAllFieldsList(entry.getClass())
                        .stream().filter(field -> field.getName().equals(parameterName))
                        .findFirst().orElseThrow(() -> new AutotestError("This error should never appear"));
                string = replacePlaceholder(string, declaredField, parameterName, parameterValue);
            } else {
                string = replacePlaceholder(string, null, parameterName, parameterValue);
            }
        }

        return string;
    }

    /**
     * Replace placeholders in string
     *
     * @param string replace placeholders in this string
     * @return string with replaced placeholders
     */
    public static String replaceTemplatePlaceholders(String string) {
        if (string.startsWith("${") && string.endsWith("}")) {
            String unquoteString = string.replaceAll("^\\$\\{", "").replaceAll("\\}$", "");
            String property = System.getProperty(unquoteString);
            return property != null ? property : string;
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
            String parameterName = parameter.getKey();
            Object parameterValue = parameter.getValue();

            if (isFieldExists(entry, parameterName)) {
                Field declaredField = FieldUtils.getAllFieldsList(entry.getClass())
                        .stream().filter(field -> field.getName().equals(parameterName))
                        .findFirst().orElseThrow(() -> new AutotestError("This error should never appear"));
                jsonString = replacePlaceholder(jsonString, declaredField, parameterName, parameterValue);
            } else {
                jsonString = replacePlaceholder(jsonString, null, parameterName, parameterValue);
            }

        }

        return jsonString;
    }

    private static boolean isFieldExists(EndpointEntry entry, String fieldName) {
        return FieldUtils.getAllFieldsList(entry.getClass()).stream()
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    public static String removeOptionals(String jsonString, Map<String, Object> parameters) {
        Set<Map.Entry<String, Object>> optionals = parameters.entrySet().stream()
                .filter(stringObjectEntry -> stringObjectEntry.getValue() == null)
                .collect(Collectors.toSet());

        for (Map.Entry<String, Object> parameter : optionals) {
            String toRemoveRegex = "(\"[^\"]+\"\\s*:\\s*\"?\\$\\{" + parameter.getKey() + "}\"?\\s*,?)";
            jsonString = jsonString.replaceAll(toRemoveRegex, "");
        }

        String orphanCommaRegex = "(,)(\\s*})";
        return jsonString.replaceAll(orphanCommaRegex, "$2");
    }

    public static String removeEmptyObjects(String jsonString) {
        Type objectType = new TypeToken<Map<String, Object>>() {
        }.getType();
        Type arrayType = new TypeToken<ArrayList<?>>() {
        }.getType();
        boolean isJsonArray = jsonString.trim().startsWith("[");

        Gson gson = new GsonBuilder()
                .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
                .setPrettyPrinting()
                .create();

        if (isJsonArray) {
            ArrayList<?> array = gson.fromJson(jsonString, arrayType);
            return gson.toJson(jsonArrayCleaner(array));
        } else {
            Map<String, Object> data = gson.fromJson(jsonString, objectType);
            return gson.toJson(jsonObjectCleaner(data));
        }

    }

    private static ArrayList<?> jsonArrayCleaner(ArrayList<?> jsonArray) {
        for (Iterator<?> it = jsonArray.iterator(); it.hasNext(); ) {
            Object item = it.next();
            if (item == null
                    || (item instanceof String && ((String) item).isEmpty())
                    || (item instanceof Map && ((Map<?, ?>) item).isEmpty())
                    || (item instanceof ArrayList && ((ArrayList<?>) item).isEmpty())) {
                it.remove();
            } else if (item instanceof ArrayList) {
                item = jsonArrayCleaner((ArrayList<?>) item);
                if (((ArrayList<?>) item).isEmpty()) {
                    it.remove();
                }
            } else if (item instanceof Map) {
                item = jsonObjectCleaner((Map<String, Object>) item);
                if (((Map<?, ?>) item).isEmpty()) {
                    it.remove();
                }
            }
        }
        return jsonArray;
    }

    private static Map<String, Object> jsonObjectCleaner(Map<String, Object> jsonData) {
        for (Iterator<Map.Entry<String, Object>> it = jsonData.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            Object value = entry.getValue();
            if (value == null
                    || (value instanceof String && ((String) value).isEmpty())
                    || (value instanceof Map && ((Map<?, ?>) value).isEmpty())
                    || (value instanceof ArrayList && ((ArrayList<?>) value).isEmpty())) {
                it.remove();
            } else if (value instanceof Map) {
                value = jsonObjectCleaner((Map<String, Object>) value);
                if (((Map<?, ?>) value).isEmpty()) {
                    it.remove();
                }
            } else if (value instanceof ArrayList) {
                value = jsonArrayCleaner((ArrayList<?>) value);
                if (((ArrayList<?>) value).isEmpty()) {
                    it.remove();
                }
            }
            entry.setValue(value);
        }
        return jsonData;
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
