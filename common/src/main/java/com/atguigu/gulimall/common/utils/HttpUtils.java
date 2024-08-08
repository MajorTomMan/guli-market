package com.atguigu.gulimall.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;

public class HttpUtils {

    private static String proxyHost = "127.0.0.1";
    private static int proxyPort = 20171;

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

    public static CloseableHttpResponse doPost(String host, String path,
            Map<String, String> headers,
            Map<String, String> querys,
            String body) throws Exception {
        try (CloseableHttpClient httpClient = wrapClient(host)) {
            String url = buildUrl(host, path, querys);
            HttpPost request = new HttpPost(url);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            if (body != null) {
                request.setEntity(new StringEntity(body, Charset.availableCharsets().get("UTF-8")));
            }
            return httpClient.execute(request);
        }
    }

    public static CloseableHttpResponse doPut(String host, String path,
            Map<String, String> headers,
            Map<String, String> querys,
            String body) throws Exception {
        try (CloseableHttpClient httpClient = wrapClient(host)) {
            String url = buildUrl(host, path, querys);
            HttpPut request = new HttpPut(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
            if (body != null) {
                request.setEntity(new StringEntity(body, Charset.availableCharsets().get("UTF-8")));
            }
            return httpClient.execute(request);
        }
    }

    public static CloseableHttpResponse doDelete(String host, String path,
            Map<String, String> headers,
            Map<String, String> querys) throws Exception {
        try (CloseableHttpClient httpClient = wrapClient(host)) {
            String url = buildUrl(host, path, querys);
            HttpDelete request = new HttpDelete(url);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
            return httpClient.execute(request);
        }
    }

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
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        if (host.startsWith("https://")) {
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(60, TimeUnit.SECONDS)
                    .setResponseTimeout(60, TimeUnit.SECONDS)
                    .build();
            BasicCookieStore defaultCookieStore = new BasicCookieStore();
            SSLContext sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustAllStrategy()).build();
            SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                    .setSslContext(sslcontext).build();
            PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(sslSocketFactory).build();
            httpClientBuilder.setDefaultCookieStore(defaultCookieStore).setDefaultRequestConfig(defaultRequestConfig)
                    .setConnectionManager(cm)
                    .evictExpiredConnections();
        }
        return httpClientBuilder.build();
    }
}
