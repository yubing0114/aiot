package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : HeartRequestDTO
 * Author   : s04180
 * Date     : 2020/1/6 11:34
 * DESCRIPTION:
 * <p>
 * History:
 * DATE        NAME        DESC
 */
public class HeartRequestDTO {

    @JSONField(name = "RefId")
    private String strRefId;

    @JSONField(name = "Time")
    private String strTime;

    @JSONField(name = "NextTime")
    private String strNextTime;

    @JSONField(name = "DeviceCode")
    private String strDeviceCode;

    @JSONField(name = "DeviceType")
    private Integer iDeviceType;

    @Override
    public String toString() {
        return "HeartRequestDTO{" +
                "strRefId='" + strRefId + '\'' +
                ", strTime='" + strTime + '\'' +
                ", strNextTime='" + strNextTime + '\'' +
                ", strDeviceCode='" + strDeviceCode + '\'' +
                ", iDeviceType=" + iDeviceType +
                '}';
    }

    public String getStrRefId() {
        return strRefId;
    }

    public void setStrRefId(String strRefId) {
        this.strRefId = strRefId;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getStrNextTime() {
        return strNextTime;
    }

    public void setStrNextTime(String strNextTime) {
        this.strNextTime = strNextTime;
    }

    public String getStrDeviceCode() {
        return strDeviceCode;
    }

    public void setStrDeviceCode(String strDeviceCode) {
        this.strDeviceCode = strDeviceCode;
    }

    public Integer getiDeviceType() {
        return iDeviceType;
    }

    public void setiDeviceType(Integer iDeviceType) {
        this.iDeviceType = iDeviceType;
    }
}
