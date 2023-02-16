package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: Administrator
 * @date: 2020/7/16 21:45
 * @modify: 2020/7/16 21:45
 * @description:
 */
public class LapiResult {

    @JSONField(name = "Response")
    private LapiResponse response;

    public LapiResponse getResponse() {
        return response;
    }

    public void setResponse(LapiResponse response) {
        this.response = response;
    }
}
