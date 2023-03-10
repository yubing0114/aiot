package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * 获取默认真实通道号出参，对应接口 {@link NetSDKLib#CLIENT_GetDefaultRealChannel}
 *
 * @author ： 47040
 * @since ： Created in 2020/9/28 10:11
 */
public class NET_OUT_GET_DEFAULT_REAL_CHANNEL extends NetSDKLib.SdkStructure {
    /**
     * 结构体大小
     */
    public int dwSize;
    /**
     * 通道数量
     */
    public int nChannelNum;
    /**
     * 通道号
     */
    public int[] nChannel = new int[NetSDKLib.MAX_PREVIEW_CHANNEL_NUM];

    public NET_OUT_GET_DEFAULT_REAL_CHANNEL(){
        dwSize = this.size();
    }
}
