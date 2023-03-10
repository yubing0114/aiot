package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

public class NET_VSP_GAVI_CHANNEL_INFO extends NetSDKLib.SdkStructure {
	/**
	 * 通道编码
	 */
	public byte[] 							szChannelEncode = new byte[24];	
	/**
	 * 上报设备信息
	 */
	public NET_VSP_GAVI_NOTIFY_OBJECT		stuNotifyObject;	
	/**
	 * 预留字段
	 */
	public byte[] 							byReserved = new byte[512];			
}
