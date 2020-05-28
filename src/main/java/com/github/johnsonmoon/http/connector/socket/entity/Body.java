package com.github.johnsonmoon.http.connector.socket.entity;

import java.io.OutputStream;

/**
 * Create by xuyh at 2020/5/28 15:38.
 */
public interface Body {
    long getContentLength();

    String getContentType();

    void writeBody(OutputStream outputStream);
}
