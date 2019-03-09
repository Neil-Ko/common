package com.coolsen.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by YeKai on 2019/3/9
 */
public class HTTPUtils {
    /**
     * 普通 HTTP 请求
     *
     * @param url
     * @param map     key: value 参数键值对
     * @param charset
     * @return
     */
    public static String post(String url, Map<String, String> map, String charset) {
        if (Objects.isNull(charset)) charset = "UTF-8";
        String result = null;
        try {
            CloseableHttpClient httpClient = build(url);
            HttpPost httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
                // 释放资源(如果响应内容没有完全消耗掉底层连接不能安全地重复使用)
                EntityUtils.consume(resEntity);
                response.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 普通 POST请求
     *
     * @param url
     * @param param
     * @param charset
     * @return
     */
    public static String post(String url, String param, String charset) {
        if (Objects.isNull(charset)) charset = "UTF-8";
        String result = null;
        try {
            CloseableHttpClient httpClient = build(url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(param, charset));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
                // 释放资源
                EntityUtils.consume(resEntity);
                response.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * notify POST 请求
     * http post method 超时时间 10 秒
     *
     * @param url
     * @param map
     * @param charset
     * @return json格式的响应code和响应结果字符串
     */
    public static String post2(String url, Map<String, String> map, String charset) {
        if (Objects.isNull(charset)) charset = "UTF-8";
        try {
            CloseableHttpClient httpClient = build(url);
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(10000).build();
            httpPost.setConfig(requestConfig);
            //设置参数
            List<NameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            JSONObject result = new JSONObject();
            if (response != null) {
                result.put("statusCode", response.getStatusLine().getStatusCode());
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result.put("resText", EntityUtils.toString(resEntity, charset));
                }
                // 释放资源
                EntityUtils.consume(resEntity);
                response.close();
            }
            return result.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * notify POST 请求
     * http post 超时时间 10秒
     *
     * @param url
     * @param param   字符串参数
     * @param charset
     * @return json格式的响应code和响应结果字符串
     */
    public static String post2(String url, String param, String charset) {
        if (Objects.isNull(charset)) charset = "UTF-8";
        try {
            CloseableHttpClient httpClient = build(url);
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(10000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new StringEntity(param, charset));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            JSONObject result = new JSONObject();
            if (response != null) {
                result.put("statusCode", response.getStatusLine().getStatusCode());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result.put("resText", EntityUtils.toString(resEntity, charset));
                }
                // 释放资源
                EntityUtils.consume(resEntity);
                response.close();
            }
            return result.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串以 body 参数传参
     *
     * @param url
     * @param jsonString 请求json字符串
     * @param charset
     * @return
     */
    public static String postJson(String url, String jsonString, String charset) {
        if (Objects.isNull(charset)) charset = "UTF-8";
        String result = null;
        try {
            CloseableHttpClient httpClient = build(url);
            HttpPost httpPost = new HttpPost(url);
            // 以body参数传参，只需如下设置header即可
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonString, charset));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity);
                }
                // 释放资源
                EntityUtils.consume(resEntity);
                response.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * https get method
     *
     * @param url
     * @param charset
     * @return
     */
    public static String get(String url, String charset) {
        if (Objects.isNull(charset)) charset = "UTF-8";
        String result = null;
        try {
            CloseableHttpClient httpClient = build(url);
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
                // 释放资源
                EntityUtils.consume(resEntity);
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据请求链接生成对应的HttpClient
     *
     * @param url
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient build(String url) {
        if (Objects.nonNull(url) && url.startsWith("https://")) {
            return build();
        } else {
            return HttpClients.createDefault();
        }
    }

    /**
     * 在调用SSL之前需要重写验证方法，取消检测SSL
     * 创建ConnectionManager，添加Connection配置信息
     *
     * @return HttpClient 支持https
     */
    public static CloseableHttpClient build() {
        try {
            // 在调用SSL之前需要重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                }
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[]{trustManager}, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            // 创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

}
