package com.github.johnsonmoon.http.connector.socket;

import com.github.johnsonmoon.http.connector.socket.util.StreamUtils;
import com.github.johnsonmoon.http.connector.socket.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

/**
 * Create by xuyh at 2020/5/28 16:00.
 */
public class SocketHttpTest {
    private static Logger logger = LoggerFactory.getLogger(SocketHttpTest.class);

    public static void main(String[] args) {
        // TODO
    }

    public static String get(String url) {
        UrlUtils.HostPort hostPort = UrlUtils.getHostPortUrlFromUrl(url);
        return get(hostPort.getHost(), hostPort.getPort(), hostPort.getUrl());
    }

    private static String get(String host, int port, String url) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = null;
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            String request = "GET " + url + " HTTP/1.1\r\n"
                    + "Host: " + host + ":" + port + "\r\n"
                    + "Connection: Keep-Alive\r\n"
                    + "\r\n";
            socket = new Socket();
            socket.connect(socketAddress, 3000);
            socket.setSoTimeout(30000);
            outputStream = socket.getOutputStream();
            outputStream.write(request.getBytes(StreamUtils.UTF_8));
            outputStream.flush();
            inputStream = socket.getInputStream();
            String status = StreamUtils.readLine(inputStream);
            if (status == null || !status.contains("200")) {
                return null;
            }
            Map<String, List<String>> headers = StreamUtils.readHeaders(inputStream);
            inputStream = StreamUtils.wrapInputStream(headers, inputStream);
            return StreamUtils.toString(inputStream);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            closeResource(inputStream, outputStream, socket);
        }
        return null;
    }

    public static String post(String url, String body) {
        UrlUtils.HostPort hostPort = UrlUtils.getHostPortUrlFromUrl(url);
        return post(hostPort.getUrl(), hostPort.getPort(), hostPort.getUrl(), body);
    }

    private static String post(String host, int port, String url, String body) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = null;
        try {
            SocketAddress address = new InetSocketAddress(host, port);
            socket = new Socket();
            socket.connect(address, 3000);
            socket.setSoTimeout(30000);
            outputStream = socket.getOutputStream();
            String request = "POST " + url + " HTTP/1.1\r\n" +
                    "Host: " + host + ":" + port + "\r\n" +
                    "Connection: Keep-Alive\r\n";
            if (body != null && !body.isEmpty()) {
                request = request + "Content-Length: " + body.getBytes().length + "\r\n";
                request = request + "Content-Type: application/json\r\n";
            }
            request = request + "\r\n";
            outputStream.write(request.getBytes(StreamUtils.UTF_8));
            if (body != null && !body.isEmpty()) {
                outputStream.write(body.getBytes(StreamUtils.UTF_8));
            }
            outputStream.flush();
            inputStream = socket.getInputStream();
            String status = StreamUtils.readLine(inputStream);
            if (status == null || !status.contains("200")) {
                return null;
            }
            Map<String, List<String>> headers = StreamUtils.readHeaders(inputStream);
            inputStream = StreamUtils.wrapInputStream(headers, inputStream);
            return StreamUtils.toString(inputStream);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        } finally {
            closeResource(inputStream, outputStream, socket);
        }
        return null;
    }

    private static void closeResource(InputStream inputStream, OutputStream outputStream, Socket socket) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }
}
