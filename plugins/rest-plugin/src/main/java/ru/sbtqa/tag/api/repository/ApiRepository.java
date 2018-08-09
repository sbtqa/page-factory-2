package ru.sbtqa.tag.api.repository;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import ru.sbtqa.tag.api.Entry;
import ru.sbtqa.tag.api.exception.ApiException;

public class ApiRepository implements Repository {

    private Map<Class<? extends Entry>, ApiPair> pairs = new LinkedHashMap<>();

    public void add(Class<? extends Entry> apiEntry, ApiPair pair) {
        pairs.put(apiEntry, pair);
    }

    public ApiPair get(Class<? extends Entry> apiEntry) {
        return pairs.get(apiEntry);
    }

    public ApiPair getLast() {
        Iterator<Map.Entry<Class<? extends Entry>, ApiPair>> iterator = pairs.entrySet().iterator();

        Map.Entry<Class<? extends Entry>, ApiPair> last = null;
        while (iterator.hasNext()) {
            last = iterator.next();
        }

        if (last == null) {
            throw new ApiException("Repository of request-response is empty");
        }

        return last.getValue();
    }
}
