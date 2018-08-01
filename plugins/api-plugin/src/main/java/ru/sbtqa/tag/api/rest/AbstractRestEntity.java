package ru.sbtqa.tag.api.rest;

import java.util.Map;
import ru.sbtqa.tag.api.exception.ApiRestException;

public abstract class AbstractRestEntity {

    public Object put(String url, Map<String, String> headers, Object body) throws ApiRestException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object patch(String url, Map<String, String> headers, Object body) throws ApiRestException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object delete(String url, Map<String, String> headers) throws ApiRestException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
