package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: Administrator
 * @date: 2020/7/16 21:27
 * @modify: 2020/7/16 21:27
 * @description: 返回体结构
 */
public class LapiResponse<T> {

    @JSONField(name = "ResponseURL")
    private String responseUrl;

    @JSONField(name = "CreatedID")
    private Integer createID;

    @JSONField(name = "ResponseCode")
    private Integer responseCode;

    @JSONField(name = "ResponseString")
    private String responseString;

    @JSONField(name = "StatusCode")
    private Integer statusCode;

    @JSONField(name = "StatusString")
    private String statusString;

    @JSONField(name = "Data")
    T data;

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCreateID() {
        return createID;
    }

    public void setCreateID(Integer createID) {
        this.createID = createID;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }
}
