package ru.sbtqa.tag.apifactory.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Map;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbtqa.tag.apifactory.exception.ApiSoapException;
import ru.sbtqa.tag.apifactory.repositories.Bullet;
import ru.sbtqa.tag.qautils.properties.Props;

/**
 *
 *
 */
public class SoapImpl implements Soap {

    private static final Logger LOG = LoggerFactory.getLogger(SoapImpl.class);
     
    @Override
    public Bullet send(String url, final Map<String, String> headers, Object body, final Proxy proxy) throws ApiSoapException {
        SOAPMessage response = null;
        
        try {
            SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = factory.createConnection();

            URL endpoint = new URL(null, url, new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL url) throws IOException {
                    // The url is the parent of this stream handler, so must create clone
                    URL clone = new URL(url.toString());

                    URLConnection connection;
                    if (Proxy.NO_PROXY != proxy) {
                        connection = clone.openConnection(proxy);
                    } else {
                        connection = clone.openConnection();
                    }
                    
                    int timeout = Integer.parseInt(Props.get("api.timeout"));
                    connection.setConnectTimeout(timeout * 1000);
                    connection.setReadTimeout(timeout * 1000);

                    // Custom headers
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        connection.addRequestProperty(header.getKey(), header.getValue());
                    }

                    return connection;
                }
            });

            MessageFactory messageFactory = MessageFactory.newInstance();
            String encoding = Props.get("api.encoding");
            SOAPMessage message = messageFactory.createMessage(new MimeHeaders(), new ByteArrayInputStream(((String) body).getBytes(Charset.forName(encoding))));
            encoding = encoding.replaceAll("-", "");
            try {
                response = connection.call(message, endpoint);
                connection.close();
                response.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, encoding);
            } catch (SOAPException e) {
                LOG.error("First message send failed", e);
                response = connection.call(message, endpoint);
                connection.close();
                response.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, encoding);
            }
        } catch (SOAPException | IOException ex) {
            throw new ApiSoapException("There are a problem with fire soap request", ex);
        }
        
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.writeTo(out);
            String responseBody = new String(out.toByteArray());
            return new Bullet(null, responseBody);
        } catch (SOAPException | IOException ex) {
            LOG.error("There are a problem with get response soap body", ex);
        }
        return null;
    }
}
