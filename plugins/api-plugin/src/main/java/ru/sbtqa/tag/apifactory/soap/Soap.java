package ru.sbtqa.tag.apifactory.soap;

import java.net.Proxy;
import java.util.Map;
import ru.sbtqa.tag.apifactory.exception.ApiSoapException;

/**
 *
 *
 */
public interface Soap {

    public Object send(String url, Map<String, String> headers, Object body, final Proxy p) throws ApiSoapException;
}
