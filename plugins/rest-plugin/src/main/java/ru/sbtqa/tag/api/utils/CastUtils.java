package ru.sbtqa.tag.api.utils;

import cucumber.api.DataTable;
import java.util.Map;

public class CastUtils {

    private CastUtils() {}

    public static Map<String, String> toMap(DataTable dataTable) {
        return dataTable.asMap(String.class, String.class);
    }
}
