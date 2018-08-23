package ru.sbtqa.tag.api.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    private RegexUtils() {}

    public static String getFirstMatcherGroup(String text, String mask) {
        Matcher matcher = Pattern.compile(mask).matcher(text);
        
        String firstGroup = "";
        if (matcher.find()) {
            firstGroup = matcher.group(1);
        }

        return firstGroup;
    }
}
