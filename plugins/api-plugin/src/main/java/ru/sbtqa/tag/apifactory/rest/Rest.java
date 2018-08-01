package ru.sbtqa.tag.apifactory.rest;

import java.util.Map;
import ru.sbtqa.tag.apifactory.exception.ApiRestException;

/**
 *
 *
 */
public interface Rest {

    Object get(String url, Map<String, String> headers) throws ApiRestException;

    Object post(String url, Map<String, String> headers, Object body) throws ApiRestException;

    Object put(String url, Map<String, String> headers, Object body) throws ApiRestException;

    Object patch(String url, Map<String, String> headers, Object body) throws ApiRestException;

    Object delete(String url, Map<String, String> headers) throws ApiRestException;
}
