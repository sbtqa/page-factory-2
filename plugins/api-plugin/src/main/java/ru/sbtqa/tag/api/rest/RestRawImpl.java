package ru.sbtqa.tag.api.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.api.exception.ApiRestException;
import ru.sbtqa.tag.api.repositories.Bullet;
import ru.sbtqa.tag.qautils.properties.Props;

/**
 *
 *
 */
public class RestRawImpl implements Rest {

    private static final Logger LOG = LoggerFactory.getLogger(RestRawImpl.class);

    @Override
    public Bullet get(String url, Map<String, String> headers) throws ApiRestException {
        return fire("GET", url, headers, null);
    }

    @Override
    public Bullet post(String url, Map<String, String> headers, Object body) throws ApiRestException {
        return fire("POST", url, headers, body);
    }

    @Override
    public Bullet put(String url, Map<String, String> headers, Object body) throws ApiRestException {
        return fire("PUT", url, headers, body);
    }

    @Override
    public Bullet patch(String url, Map<String, String> headers, Object body) throws ApiRestException {
        return fire("PATCH", url, headers, body);
    }

    @Override
    public Bullet delete(String url, Map<String, String> headers) throws ApiRestException {
        return fire("DELETE", url, headers, null);
    }

    private Bullet fire(String mthd, String url, Map<String, String> headers, Object body) {
        try {
            URL obj = new URL(url.replaceAll(" ", "%20"));
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            //stackoverflow.com/question/25163131
            String method;
            if ("PATCH".equals(mthd)) {
                method = "POST";
                connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            } else {
                method = mthd;
            }

            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            String encoding = Props.get("api.encoding");
            LOG.info("Sending '" + method + "' request to URL : {}", URLDecoder.decode(url, encoding));

            //add request header
            if (null != headers && !headers.isEmpty()) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
                LOG.info("Headers are: {}", headers);
            }

            //add body
            if (null != body && !"".equals(body) && body instanceof String) {
                try (OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), encoding)) {
                    out.write((String) body);
                }
            }

            LOG.info("Body is: {}", body);

            Map<String, String> headersResponse = new HashMap<>();
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                String headerValueAsString = StringUtils.join(header.getValue(), ", "); 
                if (headersResponse.containsKey(header.getKey())) {
                    headersResponse.put(header.getKey(), headersResponse.get(header.getKey()) + ", " + headerValueAsString);
                } else {
                    headersResponse.put(header.getKey(), headerValueAsString);
                }
            }

            Object response;
            try {
                response = IOUtils.toString((InputStream) connection.getContent(), encoding);
                LOG.info("Response is {}", response);
            } catch (IOException e) {
                if (connection.getContentType() != null) {
                    response = IOUtils.toString(connection.getErrorStream(), encoding);
                    LOG.error("Response is {}", response, e);
                } else {
                    return new Bullet(headersResponse, null);
                }
            } catch (ClassCastException e) {
                response = connection.getContent();
                LOG.error("Response return an error", e);
            }

            return new Bullet(headersResponse, response.toString());
        } catch (IOException ex) {
            LOG.error("Failed to get response", ex);
        }
        return null;
    }
}
