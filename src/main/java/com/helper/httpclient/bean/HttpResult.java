package com.helper.httpclient.bean;

/**
 * 这是一个httpclient POST请求返回的结果bean
 *
 * @author lijin
 */
public class HttpResult {

    private Integer code;

    private String data;

    public HttpResult() {

    }

    public HttpResult(Integer code, String data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
