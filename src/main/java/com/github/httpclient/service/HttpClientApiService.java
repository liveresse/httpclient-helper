package com.github.httpclient.service;

import com.github.httpclient.bean.HttpResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装的HttpClientApiService
 *
 * @author lijin
 */
public class HttpClientApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientApiService.class);

    private static final String KEY_ENCODING = "UTF-8";

    private CloseableHttpClient httpclient;

    private RequestConfig requestConfig;

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    /**
     * GET请求
     *
     * @param url     URL
     * @param headers 请求头
     * @param params  请求参数
     * @param encode  编码
     * @return {@link HttpResult}
     * @throws IOException
     * @throws URISyntaxException
     */
    public HttpResult doGet(String url, Map<String, String> headers, Map<String, String> params, String encode) throws IOException, URISyntaxException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("HttpClientApiService===>doGet()===>url={},headers={},params={},encode={}", url, headers, params, encode);
        }
        if (StringUtils.isBlank(encode)) {
            encode = KEY_ENCODING;
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.requestConfig);
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!CollectionUtils.isEmpty(params)) {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }

        return execute(httpGet, encode);

    }

    /**
     * POST请求
     *
     * @param url     URL
     * @param headers 请求头
     * @param params  请求参数
     * @param encode  编码
     * @return {@link HttpResult}
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, String> headers, Map<String, String> params, String encode) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("HttpClientApiService===>doPost()===>url={},headers={},params={},encode={}", url, headers, params, encode);
        }
        if (StringUtils.isBlank(encode)) {
            encode = KEY_ENCODING;
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);

        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, encode);
            httpPost.setEntity(formEntity);
        }

        return execute(httpPost, encode);
    }

    /**
     * POST请求
     *
     * @param url     URL
     * @param headers 请求头
     * @param json    JSON
     * @param encode  编码
     * @return {@link HttpResult}
     * @throws IOException
     */
    public HttpResult doPostRaw(String url, Map<String, String> headers, String json, String encode) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("HttpClientApiService===>doPost()===>url={},headers={},json={},encode={}", url, headers, json, encode);
        }
        if (StringUtils.isBlank(encode)) {
            encode = KEY_ENCODING;
        }

        HttpPost httpPostRaw = new HttpPost(url);
        httpPostRaw.setConfig(this.requestConfig);

        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPostRaw.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (StringUtils.isNotBlank(json)) {
            StringEntity stringEntity = new StringEntity(json, encode);
            httpPostRaw.setEntity(stringEntity);
        }

        return execute(httpPostRaw, encode);
    }

    /**
     * PUT请求
     *
     * @param url     URL
     * @param headers 请求头
     * @param params  请求参数
     * @param encode  编码
     * @return {@link HttpResult}
     * @throws IOException
     */
    public HttpResult doPut(String url, Map<String, String> headers, Map<String, String> params, String encode) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("HttpClientApiService===>doPut()===>url={},headers={},params={},encode={}", url, headers, params, encode);
        }
        if (StringUtils.isBlank(encode)) {
            encode = KEY_ENCODING;
        }

        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(this.requestConfig);

        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPut.setHeader(entry.getKey(), entry.getValue());
            }
        }

        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                parameters.add(basicNameValuePair);
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, encode);
            httpPut.setEntity(formEntity);
        }

        return execute(httpPut, encode);
    }

    /**
     * DELETE请求
     *
     * @param url     URL
     * @param headers 请求头
     * @param encode  编码
     * @return {@link HttpResult}
     * @throws IOException
     */
    public HttpResult doDelete(String url, Map<String, String> headers, String encode) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("HttpClientApiService===>doDelete()===>url={},headers={},encode={}", url, headers, encode);
        }
        if (StringUtils.isBlank(encode)) {
            encode = KEY_ENCODING;
        }

        HttpDelete httpDelete = new HttpDelete(url);
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpDelete.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return execute(httpDelete, encode);

    }

    /**
     * 执行请求
     *
     * @param requestBase {@link HttpRequestBase}
     * @param encode      编码
     * @return {@link HttpResult}
     * @throws IOException
     */
    private HttpResult execute(HttpRequestBase requestBase, String encode) throws IOException {
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(requestBase);
            return new HttpResult(response.getStatusLine().getStatusCode(), response.getEntity() != null ? EntityUtils.toString(
                    response.getEntity(), encode) : null);
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

}
