package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * @author 47081
 * @version 1.0
 * @description 热度图订阅接口出参
 * @date 2020/9/21
 */
public class NET_OUT_ATTACH_VIDEOSTAT_HEATMAP extends NetSDKLib.SdkStructure {
    public int dwSize;

    public NET_OUT_ATTACH_VIDEOSTAT_HEATMAP() {
        this.dwSize = size();
    }
}
