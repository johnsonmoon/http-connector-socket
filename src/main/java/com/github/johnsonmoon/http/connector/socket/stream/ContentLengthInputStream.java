package com.github.johnsonmoon.http.connector.socket.stream;

import com.github.johnsonmoon.http.connector.socket.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public class ContentLengthInputStream extends InputStream {

    private InputStream wrapper;
    private long contentLength;
    private long pos = 0;
    private boolean closed = false;

    public ContentLengthInputStream(InputStream in, int contentLength) {
        this(in, (long) contentLength);
    }

    public ContentLengthInputStream(InputStream in, long contentLength) {
        super();
        this.wrapper = in;
        this.contentLength = contentLength;
    }

    public int read() throws IOException {
        if (closed) {
            throw new IOException("Stream was closed.");
        }
        if (pos >= contentLength) {
            return -1;
        }
        pos++;
        return this.wrapper.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Stream was closed.");
        }
        if (pos >= contentLength) {
            return -1;
        }
        if (pos + len > contentLength) {
            len = (int) (contentLength - pos);
        }
        int count = this.wrapper.read(b, off, len);
        pos += count;
        return count;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public long skip(long n) throws IOException {
        long length = Math.min(n, contentLength - pos);
        length = this.wrapper.skip(length);
        if (length > 0) {
            pos += length;
        }
        return length;
    }

    public int available() throws IOException {
        if (this.closed) {
            return 0;
        }
        int avail = this.wrapper.available();
        if (this.pos + avail > this.contentLength) {
            avail = (int) (this.contentLength - this.pos);
        }
        return avail;
    }

    public void close() throws IOException {
        if (!closed) {
            try {
                StreamUtils.exhaustInputStream(this);
                wrapper.close();
            } finally {
                closed = true;
            }
        }
    }
}
