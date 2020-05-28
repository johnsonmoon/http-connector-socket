package com.github.johnsonmoon.http.connector.socket.entity;

import java.io.OutputStream;

/**
 * Request body.
 *
 * Create by xuyh at 2020/5/28 15:38.
 */
public interface Body {
    /**
     * Get request body length in bytes.
     *
     * @return body length in bytes
     */
    long getContentLength();

    /**
     * Get request body content-type in MIME types.
     *
     * @return MIME type of request body
     */
    String getContentType();

    /**
     * Write request body into outputStream.
     *
     * @param outputStream output stream which request body will write into.
     */
    void writeBody(OutputStream outputStream);
}
