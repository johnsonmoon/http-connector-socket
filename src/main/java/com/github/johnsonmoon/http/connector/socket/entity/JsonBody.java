package com.github.johnsonmoon.http.connector.socket.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

/**
 * Create by xuyh at 2020/5/28 16:50.
 */
public class JsonBody implements Body {
    private static Logger logger = LoggerFactory.getLogger(JsonBody.class);

    private String jsonStr = "{}";
    private String charset = "UTF-8";

    public JsonBody() {
    }

    public JsonBody(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public JsonBody(String jsonStr, String charset) {
        this.jsonStr = jsonStr;
        this.charset = charset;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public long getContentLength() {
        try {
            return jsonStr.getBytes(charset).length;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public void writeBody(OutputStream outputStream) {
        try {
            outputStream.write(jsonStr.getBytes(charset));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "JsonBody{" +
                "jsonStr='" + jsonStr + '\'' +
                ", charset='" + charset + '\'' +
                '}';
    }
}
