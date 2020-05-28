package com.github.johnsonmoon.http.connector.socket.util;

import com.github.johnsonmoon.http.connector.socket.stream.ChunkedInputStream;
import com.github.johnsonmoon.http.connector.socket.stream.ContentLengthInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by xuyh at 2020/5/27 15:15.
 */
public class StreamUtils {
    private static Logger logger = LoggerFactory.getLogger(StreamUtils.class);

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static void exhaustInputStream(InputStream inputStream) {
        try {
            byte buffer[] = new byte[1024];
            for (; ; ) {
                int length = inputStream.read(buffer);
                if (length < 0) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public static int responseCode(String line) {
        String[] splits = line.split(" ");
        for (String split : splits) {
            try {
                return Integer.parseInt(split);
            } catch (Exception e) {
                //do nothing
            }
        }
        return 500;
    }

    public static String readLine(InputStream inputStream) {
        byte[] byteArrayData = new byte[0];
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = inputStream.read()) >= 0) {
                byteArrayOutputStream.write(ch);
                if (ch == '\n') {
                    break;
                }
            }
            if (byteArrayOutputStream.size() == 0) {
                return null;
            }
            byteArrayData = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        int length = byteArrayData.length;
        int offset = 0;
        if (length > 0) {
            if (byteArrayData[length - 1] == '\n') {
                offset++;
                if (length > 1) {
                    if (byteArrayData[length - 2] == '\r') {
                        offset++;
                    }
                }
            }
        }
        return new String(byteArrayData, 0, length - offset, UTF_8);
    }

    public static Map<String, List<String>> readHeaders(InputStream input) {
        Map<String, List<String>> headers = new HashMap<>();
        String line;
        do {
            line = readLine(input);
            if (line != null && !line.isEmpty()) {
                String[] headerPair = line.split(":");
                String name = headerPair[0].trim();
                String value = headerPair[1].trim();
                List<String> values = headers.get(name);
                if (values == null) {
                    values = new ArrayList<>();
                    headers.put(name, values);
                }
                values.add(value);
            }
        } while (line != null && !line.isEmpty());
        return headers;
    }

    public static Map<String, String> convertHeaders(Map<String, List<String>> headersRaw) {
        Map<String, String> headers = new HashMap<>();
        if (headersRaw != null && !headersRaw.isEmpty()) {
            headersRaw.forEach((key, array) -> {
                if (array != null && !array.isEmpty()) {
                    headers.put(key, array.get(0));
                }
            });
        }
        return headers;
    }

    public static String toString(InputStream inputStream, String charset) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return new String(byteArrayOutputStream.toByteArray(), charset);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    public static byte[] toBytes(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    public static InputStream wrapInputStream(Map<String, List<String>> headers, InputStream inputStream) {
        List<String> transferEncodingList = headers.get("Transfer-Encoding");
        if (transferEncodingList != null && !transferEncodingList.isEmpty()) {
            String encodings = transferEncodingList.get(0);
            String[] elements = encodings.split(";");
            int length = elements.length;
            if (length > 0 && "chunked".equalsIgnoreCase(elements[length - 1])) {
                return new ChunkedInputStream(inputStream);
            } else {
                return inputStream;
            }
        }
        List<String> contentLengthList = headers.get("Content-Length");
        if (contentLengthList != null && !contentLengthList.isEmpty()) {
            long length = -1;
            for (String contentLength : contentLengthList) {
                try {
                    length = Long.parseLong(contentLength);
                    break;
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
            if (length >= 0) {
                return new ContentLengthInputStream(inputStream, length);
            }
        }
        return inputStream;
    }
}
