package com.github.johnsonmoon.http.connector.socket.entity.dict;

/**
 * Create by xuyh at 2020/5/28 14:44.
 */
public enum Method {
    /**
     * Http GET
     */
    GET("GET", false),
    /**
     * Http DELETE
     */
    DELETE("DELETE", true),
    /**
     * Http OPTIONS
     */
    OPTIONS("OPTIONS", false),
    /**
     * Http POST
     */
    POST("POST", true),
    /**
     * Http PUT
     */
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
