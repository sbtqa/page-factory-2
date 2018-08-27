package ru.sbtqa.tag.api.utils;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import ru.sbtqa.tag.api.exception.RestPluginException;

public class TemplateUtils {

    private static final String BOM = "\uFEFF";
    private static final String PLACEHOLDER_START = "\\$\\{";
    private static final String PLACEHOLDER_FINISH = "\\}";
    private static final String QUOTE = "\"";

    private TemplateUtils() {}

    /**
     * Load template file as string from resources
     *
     * @param entry the entity for which the loading occurs
     * @param templatePath the path to template file like "templates/MyTemplate.json"
     * @param encoding file encoding
     * @return The template file as string
     */
    public static String loadFromResources(Class<?> entry, String templatePath, String encoding) {
        try {
            return IOUtils.toString(entry.getClassLoader().getResourceAsStream(templatePath), encoding)
                    .replace(BOM, "");
        } catch (NullPointerException ex) {
            throw new RestPluginException("Can't find file by path " + templatePath, ex);
        } catch (IOException ex) {
            throw new RestPluginException("File '" + templatePath + "' is not available", ex);
        }
    }

    /**
     * Replace placeholders in body on parameters
     *
     * @param body replace placeholders in this body
     * @param parameters replace these parameters
     * @return body as string
     */
    public static String replacePlaceholders(String body, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            String value = String.valueOf(parameter.getValue());

            String placeholder;
            if (parameter.getValue() instanceof String) {
                placeholder = PLACEHOLDER_START + parameter.getKey() + PLACEHOLDER_FINISH;
            } else {
                placeholder = QUOTE + PLACEHOLDER_START + parameter.getKey() + PLACEHOLDER_FINISH + QUOTE;
            }

            body = body.replaceAll(placeholder, value);
        }

        return body;
    }

}
