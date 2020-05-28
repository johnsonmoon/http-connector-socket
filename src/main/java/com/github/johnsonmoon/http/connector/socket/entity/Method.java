package com.github.johnsonmoon.http.connector.socket.entity;

/**
 * Create by xuyh at 2020/5/28 14:44.
 */
public enum Method {
    GET("GET", false),
    DELETE("DELETE", false),
    OPTIONS("OPTIONS", false),
    POST("POST", true),
    PUT("PUT", true);
    private String code;
    private boolean includeBody;

    Method(String code, boolean includeBody) {
        this.code = code;
        this.includeBody = includeBody;
    }

    public String getCode() {
        return code;
    }

    public boolean isIncludeBody() {
        return includeBody;
    }

    @Override
    public String toString() {
        return "Method{" +
                "code='" + code + '\'' +
                ", includeBody=" + includeBody +
                '}';
    }
}
