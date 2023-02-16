package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;/**
 * @author 47081
 * @descriptio
 * @date 2020/11/9
 * @version 1.0
 */

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.constant.SDKStructureFieldLenth;

/**
 * @author 47081
 * @version 1.0
 * @description
 * @date 2020/11/9
 */
public class Auxs extends NetSDKLib.SdkStructure {
    public byte[] auxs=new byte[SDKStructureFieldLenth.CFG_COMMON_STRING_32];
}
