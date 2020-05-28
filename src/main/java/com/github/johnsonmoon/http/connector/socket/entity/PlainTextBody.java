package com.github.johnsonmoon.http.connector.socket.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;

/**
 * Create by xuyh at 2020/5/28 17:03.
 */
public class PlainTextBody implements Body {
    private static Logger logger = LoggerFactory.getLogger(PlainTextBody.class);

    private String text = "";
    private String charset = "UTF-8";

    public PlainTextBody() {
    }

    public PlainTextBody(String text) {
        this.text = text;
    }

    public PlainTextBody(String text, String charset) {
        this.text = text;
        this.charset = charset;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
            return text.getBytes(charset).length;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    public void writeBody(OutputStream outputStream) {
        try {
            outputStream.write(text.getBytes(charset));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "PlainTextBody{" +
                "text='" + text + '\'' +
                ", charset='" + charset + '\'' +
                '}';
    }
}
