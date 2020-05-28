package com.github.johnsonmoon.http.connector.socket.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by xuyh at 2020/5/27 15:12.
 */
public class UrlUtils {
    private static Pattern URL_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//([^/?#]*))?", Pattern.CASE_INSENSITIVE);

    public static HostPort getHostPortUrlFromUrl(String url) {
        String domain = url;
        String restUrl = url;
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.find()) {
            String group = matcher.group();
            domain = group.substring(group.indexOf("//") + 2, group.length());
            restUrl = url.substring(url.indexOf(group) + group.length(), url.length());
        }

        HostPort hostPort = new HostPort();
        hostPort.url = restUrl;
        if (!domain.contains(":")) {
            hostPort.host = domain;
            hostPort.port = 80;
        } else {
            hostPort.host = domain.substring(0, domain.indexOf(":"));
            hostPort.port = Integer.parseInt(domain.substring(domain.indexOf(":") + 1, domain.length()));
        }
        return hostPort;
    }

    public static class HostPort {
        private String host;
        private int port;
        private String url;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "HostPort{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
