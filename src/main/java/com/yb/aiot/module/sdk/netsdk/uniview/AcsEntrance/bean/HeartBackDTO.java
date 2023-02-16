package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @author: Administrator
 * @date: 2020/7/16 21:47
 * @modify: 2020/7/16 21:47
 * @description: 心跳返回结构体
 */
public class HeartBackDTO {

    public HeartBackDTO(String url, Integer code, Date time) {
        this.responseUrl = url;
        this.code = code;
        this.data = new Data(time);
    }

    @JSONField(name = "ResponseURL")
    private String responseUrl;

    @JSONField(name = "Code")
    private Integer code;

    @JSONField(name = "Data")
    private Data data;

    class Data {
        @JSONField(name = "Time", format = "yyyy-MM-dd HH:mm:ss")
        Date time;

        Data(Date time) {
            this.time = time;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HeartBackDO{" +
                "responseUrl='" + responseUrl + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
