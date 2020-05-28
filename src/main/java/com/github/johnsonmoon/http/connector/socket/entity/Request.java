package com.github.johnsonmoon.http.connector.socket.entity;

import com.github.johnsonmoon.http.connector.socket.entity.dict.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * Http request define.
 * <p>
 * Create by xuyh at 2020/5/28 14:42.
 */
public class Request {
    /**
     * Http method.
     */
    private Method method = Method.GET;
    /**
     * Request URL.
     */
    private String url;
    /**
     * KeepAlive option.
     */
    private boolean keepAlive = true;
    private int connectTimeout = 3000;
    private int socketTimeout = 30000;
    /**
     * Http request headers.
     */
    private Map<String, String> headers;
    /**
     * Http request body.
     */
    private Body body;

    /**
     * Create a {@link Request}.
     *
     * @return {@link Request}
     */
    public static Request create() {
        return new Request();
    }

    /**
     * Set request method as GET.
     */
    public Request get() {
        this.method = Method.GET;
        return this;
    }

    /**
     * Set request method as DELETE.
     */
    public Request delete() {
        this.method = Method.DELETE;
        return this;
    }

    /**
     * Set request method as OPTIONS.
     */
    public Request options() {
        this.method = Method.OPTIONS;
        return this;
    }

    /**
     * Set request method as POST.
     */
    public Request post() {
        this.method = Method.POST;
        return this;
    }

    /**
     * Set request method as PUT.
     */
    public Request put() {
        this.method = Method.PUT;
        return this;
    }

    /**
     * Set request url.
     *
     * @param url request url.
     */
    public Request url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Set keepAlive option.
     */
    public Request keepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    /**
     * Set connect timeout.
     */
    public Request connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Set socket timeout.
     */
    public Request socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * Add header into a http request.
     *
     * @param key   key of header
     * @param value value of header
     */
    public Request header(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    /**
     * Set request body.
     *
     * @param body {@link Body}
     */
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
