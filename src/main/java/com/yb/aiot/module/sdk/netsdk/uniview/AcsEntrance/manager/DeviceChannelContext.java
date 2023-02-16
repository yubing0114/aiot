package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager;

import io.netty.channel.ChannelHandlerContext;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : FastGateChannelHandlerContext
 * Author   : s04180
 * Date     : 2020/1/13 14:46
 * DESCRIPTION:
 * <p>
 * History:    设备序列号及连接的通道信息
 * DATE        NAME        DESC
 */
public class DeviceChannelContext {

    public DeviceChannelContext(ChannelHandlerContext ctx, String deviceCode, int isBound) {
        this.ctx = ctx;
        this.isBound = isBound;
        this.deviceCode = deviceCode;
    }

    private ChannelHandlerContext ctx;

    private String deviceCode;

    private Boolean isLock = false;

    // 0 = 未绑定， 1绑定
    private int isBound = 0;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Boolean getLock() {
        return isLock;
    }

    public void setLock(Boolean lock) {
        isLock = lock;
    }

    public int getIsBound() {
        return isBound;
    }

    public void setIsBound(int isBound) {
        this.isBound = isBound;
    }

}
