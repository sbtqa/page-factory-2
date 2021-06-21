package ru.sbtqa.tag.pagefactory.utils;

import cucumber.runtime.io.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * This class realizes virtual resource by given string source without reading it from filesystem
 */
public class GherkinResource implements Resource {

    private final InputStream source;
    private final URI path;

    public GherkinResource(String source, URI path) {
        this.source = new ByteArrayInputStream(source.getBytes(Charset.defaultCharset()));
        this.path = path;
    }

    @Override
    public URI getPath() {
        return path;
    }

    @Override
    public InputStream getInputStream() {
        return this.source;
    }
}
