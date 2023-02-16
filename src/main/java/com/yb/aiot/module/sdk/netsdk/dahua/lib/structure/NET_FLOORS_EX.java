package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * @author 47081
 * @version 1.0
 * @description 楼层号,不要超过999
 * @date 2021/2/8
 */
public class NET_FLOORS_EX extends NetSDKLib.SdkStructure {
  public byte[] szFloorEx = new byte[8];
}
