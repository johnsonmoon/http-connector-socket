package com.github.johnsonmoon.http.connector.socket.stream;

import com.github.johnsonmoon.http.connector.socket.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkedInputStream extends InputStream {

    private InputStream wrapper;
    private boolean closed = false;
    private boolean bof = true;
    private boolean eof = false;
    private int chunkSize;
    private int pos;

    public ChunkedInputStream(final InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream must not be null.");
        }
        this.wrapper = inputStream;
        this.pos = 0;
    }

    public int read() throws IOException {
        if (closed) {
            throw new IOException("Stream was closed.");
        }
        if (eof) {
            return -1;
        }
        if (pos >= chunkSize) {
            nextChunk();
            if (eof) {
                return -1;
            }
        }
        pos++;
        return wrapper.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Stream was closed.");
        }
        if (eof) {
            return -1;
        }
        if (pos >= chunkSize) {
            nextChunk();
            if (eof) {
                return -1;
            }
        }
        len = Math.min(len, chunkSize - pos);
        int count = wrapper.read(b, off, len);
        pos += count;
        return count;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    private void readCRLF() throws IOException {
        int cr = wrapper.read();
        int lf = wrapper.read();
        if ((cr != '\r') || (lf != '\n')) {
            throw new IOException("CRLF expected at end of chunk: " + cr + "/" + lf);
        }
    }

    private void nextChunk() throws IOException {
        if (!bof) {
            readCRLF();
        }
        chunkSize = getChunkSizeFromInputStream(wrapper);
        bof = false;
        pos = 0;
        if (chunkSize == 0) {
            //HttpSocketConnector.readHeaders(wrapper); TODO
            eof = true;
        }
    }

    private static int getChunkSizeFromInputStream(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int state = 0;
        while (state != -1) {
            int b = inputStream.read();
            if (b == -1) {
                throw new IOException("Stream ended unexpectedly");
            }
            switch (state) {
                case 0:
                    switch (b) {
                        case '\r':
                            state = 1;
                            break;
                        case '\"':
                            state = 2;
                        default:
                            byteArrayOutputStream.write(b);
                    }
                    break;
                case 1:
                    if (b == '\n') {
                        state = -1;
                    } else {
                        throw new IOException("Protocol violation: Unexpected single newline character wrapper chunk size");
                    }
                    break;
                case 2:
                    switch (b) {
                        case '\\':
                            b = inputStream.read();
                            byteArrayOutputStream.write(b);
                            break;
                        case '\"':
                            state = 0;
                        default:
                            byteArrayOutputStream.write(b);
                    }
                    break;
                default:
                    throw new RuntimeException("assertion failed");
            }
        }
        //parse data
        String dataString = new String(byteArrayOutputStream.toByteArray(), "US-ASCII");
        int separator = dataString.indexOf(';');
        dataString = (separator > 0) ? dataString.substring(0, separator).trim() : dataString.trim();
        int result;
        try {
            result = Integer.parseInt(dataString.trim(), 16);
        } catch (NumberFormatException e) {
            throw new IOException("Bad chunk size: " + dataString);
        }
        return result;
    }

    public void close() throws IOException {
        if (!closed) {
            try {
                if (!eof) {
                    StreamUtils.exhaustInputStream(this);
                }
                wrapper.close();
            } finally {
                eof = true;
                closed = true;
            }
        }
    }
}
