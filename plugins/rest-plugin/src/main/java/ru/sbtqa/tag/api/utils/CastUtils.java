package ru.sbtqa.tag.api.utils;

import io.cucumber.datatable.DataTable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CastUtils {

    private CastUtils() {}

    public static Map<String, String> toMap(DataTable dataTable) {
        return dataTable.asMap(String.class, String.class);
    }

    public static DataTable toDataTable(Map<String, String> map) {
        List<List<String>> data = map.entrySet().stream()
                .map(row -> Arrays.asList(row.getKey(), row.getValue()))
                .collect(Collectors.toList());

        return DataTable.create(data);
    }
}
