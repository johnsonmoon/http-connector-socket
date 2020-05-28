package com.github.johnsonmoon.http.connector.socket.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Request body in file format.
 * <pre>
 *     application/octet-stream
 * </pre>
 * <p>
 * Create by xuyh at 2020/5/28 17:09.
 */
public class FileBody implements Body {
    private static Logger logger = LoggerFactory.getLogger(FileBody.class);

    private File file;

    public FileBody() {
    }

    /**
     * @param file {@link File}
     */
    public FileBody(File file) {
        this.file = file;
    }

    /**
     * @param path Absolute file path name.
     */
    public FileBody(String path) {
        this.file = new File(path);
    }

    @Override
    public long getContentLength() {
        if (file != null && file.exists()) {
            return file.length();
        }
        return 0;
    }

    @Override
    public String getContentType() {
        return "application/octet-stream";
    }

    @Override
    public void writeBody(OutputStream outputStream) {
        if (file != null && file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        }
    }
}
