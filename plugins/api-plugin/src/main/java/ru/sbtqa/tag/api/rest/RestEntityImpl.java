package ru.sbtqa.tag.api.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.allurehelper.ParamsHelper;
import ru.sbtqa.tag.api.exception.ApiRestException;
import ru.sbtqa.tag.api.repositories.Bullet;
import ru.sbtqa.tag.qautils.errors.AutotestError;
import ru.sbtqa.tag.qautils.properties.Props;

/**
 *
 *
 */
public class RestEntityImpl extends AbstractRestEntity implements Rest {

    private static final Logger LOG = LoggerFactory.getLogger(RestEntityImpl.class);

    @Override
    public Bullet get(String url, Map<String, String> headers) throws ApiRestException {
        HttpClient client = HttpClients.createDefault();
        Bullet bullet = null;

        try {
            LOG.info("Request url {}", url);
            final HttpGet get = new HttpGet(url);

            setHeaders(headers, get);

            LOG.info("Request headers {}", headers);

            HttpResponse response;
            try {
                response = client.execute(get);
            } catch (IOException ex) {
                LOG.error("There is an error in the request processing", ex);
                throw new AutotestError(ex);
            }
            Map<String, String> headersResponse = getHeadersMap(response);

            try {
                String responseBody = getEntityString(response, Props.get("api.encoding"));
                bullet = new Bullet(headersResponse, responseBody);
            } catch (IOException | ParseException ex) {
                LOG.error("Error in response body get", ex);
            }

        } finally {
            HttpClientUtils.closeQuietly(client);
        }

        return bullet;
    }

    private String getEntityString(HttpResponse response, String encoding) throws IOException {
        if (response.getEntity().getContentType() != null) {
            return EntityUtils.toString(response.getEntity(), encoding);
        }
        return null;
    }

    private void setHeaders(final Map<String, String> headers, HttpGet get) {
        for (Map.Entry<String,String> h: headers.entrySet()) {
            get.setHeader(h.getKey(), h.getValue());
        }
    }

    private Map<String, String> getHeadersMap(final HttpResponse response) {
        Map<String, String> headersResponse = new HashMap<>();
        for (Header h : response.getAllHeaders()) {
            ParamsHelper.addParam(h.getName(), h.getValue());
            if (headersResponse.containsKey(h.getName())) {
                headersResponse.put(h.getName(), headersResponse.get(h.getName()) + "; " + h.getValue());
            } else {
                headersResponse.put(h.getName(), h.getValue());
            }
        }
        return headersResponse;
    }

    @Override
    public Bullet post(String url, Map<String, String> headers, Object body) throws ApiRestException {
        HttpClient client = HttpClients.createDefault();

        try {
            LOG.info("Sending 'POST' request to URL : {}", url);
            final HttpPost post = new HttpPost(url);
            return performRequest(headers, body, client, post);
        } catch (IOException ex) {
            LOG.error("Failed to get response", ex);
            throw new ApiRestException("Empty bullet", ex);
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
    }

    @Override
    public Bullet patch(String url, Map<String, String> headers, Object body) throws ApiRestException {
        HttpClient client = HttpClients.createDefault();

        try {
            LOG.info("Sending 'PATCH' request to URL : {}", url);
            HttpPatch patch = new HttpPatch(url);
            return performRequest(headers, body, client, patch);
        } catch (IOException ex) {
            LOG.error("Failed to get response", ex);
            throw new ApiRestException("Empty bullet", ex);
        } finally {
            HttpClientUtils.closeQuietly(client);
        }
    }

    private Bullet performRequest(Map<String, String> headers, Object body, HttpClient client,
                                  HttpEntityEnclosingRequestBase request) throws IOException {
        for (Map.Entry<String,String> h: headers.entrySet()) {
            request.setHeader(h.getKey(), h.getValue());
        }

        LOG.info("Headers are: {}", headers);

        List<NameValuePair> postParams = new ArrayList<>();
        String encoding = Props.get("api.encoding");
        if (body instanceof Map) {
            Map<String, String> params = (Map<String, String>) body;

            for (Map.Entry<String,String> e: params.entrySet()) {
                postParams.add(new BasicNameValuePair(e.getKey(), e.getValue()));
            }

            if (!postParams.isEmpty()) {
                request.setEntity(new UrlEncodedFormEntity(postParams));
            }
            LOG.info("Body (form-data) is: {}", body);
        } else if (body instanceof String) {
            request.setEntity(new StringEntity((String) body, encoding));
        }

        HttpResponse response = client.execute(request);

        Map<String, String> headersResponse = getHeadersMap(response);
        String bodyResponse = null;
        try {
            bodyResponse = getEntityString(response, encoding);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return new Bullet(headersResponse, bodyResponse);
    }
}
