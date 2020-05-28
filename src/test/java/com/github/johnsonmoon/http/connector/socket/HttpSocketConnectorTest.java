package com.github.johnsonmoon.http.connector.socket;

import com.github.johnsonmoon.http.connector.socket.entity.Request;
import com.github.johnsonmoon.http.connector.socket.entity.Response;

import java.io.InputStreamReader;

/**
 * Create by xuyh at 2020/5/28 16:36.
 */
public class HttpSocketConnectorTest {
    public static void main(String[] args) throws Exception {
        Response response = HttpSocketConnector.connect(
                Request.create().get().url("http://127.0.0.1:11011/tests/file?path=/Users/johnson/Downloads/demos/tank-test/pom.xml"));
        System.out.println(response.getStatus());
        System.out.println(response.getHeaders());
        InputStreamReader reader = new InputStreamReader(response.getResponseAsInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[32];
        int length;
        while ((length = reader.read(buffer)) > 0) {
            stringBuilder.append(buffer, 0, length);
        }
        response.close();
        System.out.println(stringBuilder.toString());
    }
}
