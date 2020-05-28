package com.github.johnsonmoon.http.connector.socket;

import com.github.johnsonmoon.http.connector.socket.entity.Method;
import com.github.johnsonmoon.http.connector.socket.entity.Request;
import com.github.johnsonmoon.http.connector.socket.entity.Response;
import com.github.johnsonmoon.http.connector.socket.util.StreamUtils;
import com.github.johnsonmoon.http.connector.socket.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

/**
 * Create by xuyh at 2020/5/27 10:55.
 */
public class HttpSocketConnector {
    private static Logger logger = LoggerFactory.getLogger(HttpSocketConnector.class);

    public static Response connect(Request request) {
        Method method = request.getMethod();
        String url = request.getUrl();
        UrlUtils.HostPort hostPort = UrlUtils.getHostPortUrlFromUrl(url);
        Socket socket;
        InputStream inputStream;
        OutputStream outputStream = null;
        Response response = new Response();
        try {
            SocketAddress socketAddress = new InetSocketAddress(hostPort.getHost(), hostPort.getPort());
            socket = new Socket();
            response.setSource(socket);
            socket.connect(socketAddress, request.getConnectTimeout());
            socket.setSoTimeout(request.getSocketTimeout());
            outputStream = socket.getOutputStream();
            outputStream.write(getRequestMeta(hostPort, request));
            if (method.isIncludeBody() && request.getBody() != null) {
                request.getBody().writeBody(outputStream);
            }
            outputStream.flush();
            inputStream = socket.getInputStream();
            String statusStr = StreamUtils.readLine(inputStream);
            response.setStatus(StreamUtils.responseCode(statusStr));
            Map<String, List<String>> responseHeaders = StreamUtils.readHeaders(inputStream);
            response.setHeaders(StreamUtils.convertHeaders(responseHeaders));
            inputStream = StreamUtils.wrapInputStream(responseHeaders, inputStream);
            response.setInputStream(inputStream);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            response.setMessage(e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return response;
    }

    private static byte[] getRequestMeta(UrlUtils.HostPort hostPort, Request request) {
        Method method = request.getMethod();
        String requestMetaData = method.getCode() + " " + hostPort.getUrl() + " HTTP/1.1\r\n";
        requestMetaData = requestMetaData + "Host: " + hostPort.getHost() + ":" + hostPort.getPort() + "\r\n";
        if (request.isKeepAlive()) {
            requestMetaData = requestMetaData + "Connection: Keep-Alive\r\n";
        }
        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                requestMetaData = requestMetaData + (entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
        }
        if (method.isIncludeBody() && request.getBody() != null) {
            requestMetaData = requestMetaData + "Content-Length: " + request.getBody().getContentLength() + "\r\n";
            requestMetaData = requestMetaData + "Content-Type: " + request.getBody().getContentType() + "\r\n";
        }
        requestMetaData = requestMetaData + "\r\n";
        return requestMetaData.getBytes(StreamUtils.UTF_8);
    }
}
