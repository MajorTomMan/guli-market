package com.atguigu.gulimall.thirdparty.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

public class HttpUtils {
    /**
     * Send an HTTP GET request.
     *
     * @param host    The host URL.
     * @param path    The path of the resource.
     * @param headers Request headers.
     * @param querys  Query parameters.
     * @return CloseableHttpResponse
     * @throws Exception
     */
    public static CloseableHttpResponse doGet(String host, String path,
            Map<String, String> headers,
            Map<String, String> querys) throws Exception {
        try (CloseableHttpClient httpClient = wrapClient(host)) {
            String url = buildUrl(host, path, querys);
            HttpGet request = new HttpGet(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
            return httpClient.execute(request);
        }
    }

    // ... Implement other HTTP request methods (POST, PUT, DELETE) in a similar
    // way.

    private static String buildUrl(String host, String path, Map<String, String> querys)
            throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder(host);
        if (!path.isEmpty()) {
            sbUrl.append(path);
        }
        if (querys != null) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (sbQuery.length() > 0) {
                    sbQuery.append("&");
                }
                if (query.getKey() == null && query.getValue() != null) {
                    sbQuery.append(query.getValue());
                }
                if (query.getKey() != null) {
                    sbQuery.append(query.getKey());
                    if (query.getValue() != null) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (sbQuery.length() > 0) {
                sbUrl.append("?").append(sbQuery);
            }
        }
        return sbUrl.toString();
    }

    private static CloseableHttpClient wrapClient(String host) throws Exception {
        CloseableHttpClient httpClient = null;
        if (host.startsWith("https://")) {
            SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
            sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            SSLContext ssl = sslContextBuilder.build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(ssl);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", (ConnectionSocketFactory) sslConnectionSocketFactory)
            .register("http", (ConnectionSocketFactory) new PlainConnectionSocketFactory())
            .build();
            BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registry);
            httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        }
        if (httpClient != null) {
            return httpClient;
        }
        return HttpClients.createDefault();
    }
}