package ru.sbtqa.tag.api;

import java.util.LinkedHashMap;
import java.util.Map;
import ru.sbtqa.tag.api.exception.ApiException;

public class Repository {

    private static Map<Class<? extends ApiEntry>, ApiPair> pairs = new LinkedHashMap<>();

    public static void add(Class<? extends ApiEntry> apiEntry, ApiPair pair) {
        pairs.put(apiEntry, pair);
    }

    public static ApiPair get(Class<? extends ApiEntry> apiEntry) {
        return pairs.get(apiEntry);
    }

    public static ApiPair getLast() {
        return pairs.entrySet().stream()
                .distinct()
                .findFirst()
                .orElseThrow(() -> new ApiException("Repository of request-response is empty"))
                .getValue();
    }
}
