package com.github.johnsonmoon.http.connector.socket.entity;

import com.github.johnsonmoon.http.connector.socket.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Create by xuyh at 2020/5/28 14:42.
 */
public class Response implements Closeable {
    private static Logger logger = LoggerFactory.getLogger(Response.class);
    private int status;
    private String message;
    private Map<String, String> headers;
    private Socket source;
    private OutputStream outputStream;
    private InputStream inputStream;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setSource(Socket source) {
        this.source = source;
    }

    public InputStream getResponseAsInputStream() {
        return inputStream;
    }

    public String getResponseAsString() {
        String resp = StreamUtils.toString(inputStream);
        try {
            close();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resp;
    }

    public byte[] getResponseAsBytes() {
        byte[] bytes = StreamUtils.toBytes(inputStream);
        try {
            close();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return bytes;
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (source != null) {
            source.close();
        }
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", headers=" + headers +
                ", source=" + source +
                ", inputStream=" + inputStream +
                '}';
    }
}
