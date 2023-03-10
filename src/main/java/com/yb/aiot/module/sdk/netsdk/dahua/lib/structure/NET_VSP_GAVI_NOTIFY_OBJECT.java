package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * @author 119178
 * @description 上报对象信息
 * @date 2021/4/20
 */
public class NET_VSP_GAVI_NOTIFY_OBJECT extends NetSDKLib.SdkStructure {
	/**
	 * 人脸选中
	 */
	public int							bFaceChecked;	
	/**
	 * 人体选中
	 */
	public int							bPersonChecked;			
	/**
	 * 机动车选中
	 */
	public int							bMotorVehicleChecked;	
	/**
	 * 非机动车选中
	 */
	public int							bNonMotorVehicleChecked;
	/**
	 * 图片选中
	 */
	public int							bImageChecked;		
	/**
	 * 预留字段
	 */
	public byte[]					    byReserved =new byte[516];			
}
