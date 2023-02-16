package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * @author 47081
 * @version 1.0
 * @description 车牌
 * @date 2021/2/22
 */
public class PlateNumber extends NetSDKLib.SdkStructure {
    public byte[] plateNumber=new byte[32];
}
