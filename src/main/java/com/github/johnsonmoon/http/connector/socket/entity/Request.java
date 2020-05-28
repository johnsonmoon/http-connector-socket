package com.github.johnsonmoon.http.connector.socket.entity;

import com.github.johnsonmoon.http.connector.socket.entity.dict.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by xuyh at 2020/5/28 14:42.
 */
public class Request {
    private Method method = Method.GET;
    private String url;
    private boolean keepAlive = true;
    private int connectTimeout = 3000;
    private int socketTimeout = 30000;
    private Map<String, String> headers;
    private Body body;

    public static Request create() {
        return new Request();
    }

    public Request get() {
        this.method = Method.GET;
        return this;
    }

    public Request delete() {
        this.method = Method.DELETE;
        return this;
    }

    public Request options() {
        this.method = Method.OPTIONS;
        return this;
    }

    public Request post() {
        this.method = Method.POST;
        return this;
    }

    public Request put() {
        this.method = Method.PUT;
        return this;
    }

    public Request url(String url) {
        this.url = url;
        return this;
    }

    public Request keepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public Request connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Request socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public Request header(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    public Request body(Body body) {
        this.body = body;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method=" + method +
                ", url='" + url + '\'' +
                ", keepAlive=" + keepAlive +
                ", connectTimeout=" + connectTimeout +
                ", socketTimeout=" + socketTimeout +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
