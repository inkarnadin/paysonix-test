package ru.alarh.http;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestWrapper extends HttpServletRequestWrapper {

    @Getter
    private ByteArrayOutputStream cachedBodyOutputStream;

    private final Map<String, String> additionalHeaders = new HashMap<>();

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void addHeader(String name, String value) {
        additionalHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (additionalHeaders.containsKey(name))
            headerValue = additionalHeaders.get(name);
        return headerValue;
    }

    @Override
    @SneakyThrows
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @SneakyThrows
    private void cacheInputStream() {
        cachedBodyOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedBodyOutputStream);
    }

    @Override
    public ServletInputStream getInputStream() {
        if (Objects.isNull(cachedBodyOutputStream))
            cacheInputStream();

        return new CachedServletInputStream();
    }

    public class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream input;

        public CachedServletInputStream() {
            input = new ByteArrayInputStream(cachedBodyOutputStream.toByteArray());
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {}

        @Override
        public int read() {
            return input.read();
        }
    }

}
