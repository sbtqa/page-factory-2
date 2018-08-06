package ru.sbtqa.tag.api;

import java.util.LinkedHashMap;
import java.util.Map;
import ru.sbtqa.tag.api.exception.ApiException;

public class ApiRepository implements Repository {

    private Map<Class<? extends ApiEntry>, ApiPair> pairs = new LinkedHashMap<>();

    public void add(Class<? extends ApiEntry> apiEntry, ApiPair pair) {
        pairs.put(apiEntry, pair);
    }

    public ApiPair get(Class<? extends ApiEntry> apiEntry) {
        return pairs.get(apiEntry);
    }

    public ApiPair getLast() {
        return pairs.entrySet().stream()
                .distinct()
                .findFirst()
                .orElseThrow(() -> new ApiException("Repository of request-response is empty"))
                .getValue();
    }
}
