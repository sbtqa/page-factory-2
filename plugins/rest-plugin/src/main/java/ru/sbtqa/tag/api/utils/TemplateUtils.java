package ru.sbtqa.tag.api.utils;

import org.apache.commons.io.IOUtils;
import ru.sbtqa.tag.api.exception.RestPluginException;

import java.io.IOException;

public class TemplateUtils {

    private static final String BOM = "\uFEFF";

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
}
