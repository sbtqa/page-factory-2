package ru.sbtqa.tag.api.repository;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import ru.sbtqa.tag.api.EndpointEntry;
import ru.sbtqa.tag.api.exception.RestPluginException;

public class ApiRepository implements Repository {

    private Map<Class<? extends EndpointEntry>, ApiPair> pairs = new LinkedHashMap<>();

    public void add(Class<? extends EndpointEntry> endpoint, ApiPair pair) {
        pairs.put(endpoint, pair);
    }

    public ApiPair get(Class<? extends EndpointEntry> endpoint) {
        return pairs.get(endpoint);
    }

    public ApiPair getLast() {
        Iterator<Map.Entry<Class<? extends EndpointEntry>, ApiPair>> iterator = pairs.entrySet().iterator();

        Map.Entry<Class<? extends EndpointEntry>, ApiPair> last = null;
        while (iterator.hasNext()) {
            last = iterator.next();
        }

        if (last == null) {
            throw new RestPluginException("Repository of request-response is empty");
        }

        return last.getValue();
    }
}
