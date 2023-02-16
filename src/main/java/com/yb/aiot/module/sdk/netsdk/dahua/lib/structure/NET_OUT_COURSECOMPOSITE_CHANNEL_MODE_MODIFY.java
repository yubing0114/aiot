package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * 录播主机修改模式出参
 *
 * @author ： 47040
 * @since ： Created in 2020/9/27 17:21
 */
public class NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_MODIFY extends NetSDKLib.SdkStructure {
    /**
     * 结构体大小
     */
    public int dwSize;
    /**
     * 修改的模式个数, 和修改的模式个数相同
     */
    public int nReturnNum;
    /**
     * 返回码: 1 成功, 2 失败, 3, 已满, 4 资源不足
     */
    public int[] nReturnCode = new int[NetSDKLib.NET_MAX_MODE_NUMBER];

    public NET_OUT_COURSECOMPOSITE_CHANNEL_MODE_MODIFY() {
        dwSize = this.size();
    }
}
