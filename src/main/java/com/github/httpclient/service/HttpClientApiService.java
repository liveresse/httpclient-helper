package com.github.httpclient.service;

import com.github.httpclient.bean.HttpResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

    private CloseableHttpClient httpclient;

    private RequestConfig requestConfig;

    /**
     * Get请求
     *
     * @param url URL
     * @return 响应数据200返回响应内容, 其他返回null
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doGet(String url) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("通过HttpClientApiService===>doGet()从后台系统获取数据 url={}", url);
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 有参数的Get请求
     *
     * @param url   URL
     * @param param 参数
     * @return 响应数据200返回响应内容, 其他返回null
     * @throws IOException
     * @throws URISyntaxException
     */
    public String doGet(String url, Map<String, String> param) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        if (!CollectionUtils.isEmpty(param)) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        return doGet(builder.build().toString());
    }

    /**
     * POST请求
     *
     * @param url URL
     * @return HttpResult
     * @throws IOException
     */
    public HttpResult doPost(String url) throws IOException {
        return doPost(url, null);
    }

    /**
     * 有参数POST请求
     *
     * @param url   URL
     * @param param 参数
     * @return HttpResult
     * @throws IOException
     */
    public HttpResult doPost(String url, Map<String, String> param) throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("通过HttpClientApiService===>doPost() url={},param={}", url, param);
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);
        if (!CollectionUtils.isEmpty(param)) {
            List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
            for (Map.Entry<String, String> entry : param.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            httpPost.setEntity(formEntity);
        }
        return getHttpResult(httpPost);
    }

    /**
     * POST提交JSON数据
     *
     * @param url  URL
     * @param json JSON数据
     * @return HttpResult
     * @throws IOException
     */
    public HttpResult doPostJson(String url, String json) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        if (StringUtils.isNotBlank(json)) {
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(stringEntity);
        }
        return getHttpResult(httpPost);
    }

    /**
     * 执行HttpPost
     *
     * @param httpPost HttpPost
     * @return HttpResult
     * @throws IOException
     */
    private HttpResult getHttpResult(HttpPost httpPost) throws IOException {
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                    response.getEntity(), "UTF-8"));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 执行 HttpDelete
     *
     * @param url     URL
     * @param headers 请求头
     * @param encode  编码
     * @return HttpResult
     * @throws IOException
     */
    public HttpResult doDelete(String url, Map<String, String> headers, String encode) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        if (StringUtils.isBlank(encode)) {
            encode = "UTF-8";
        }
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpDelete.setHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpDelete);
            return new HttpResult(response.getStatusLine().getStatusCode(), response.getEntity() != null ? EntityUtils.toString(
                    response.getEntity(), encode) : null);
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

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
}
