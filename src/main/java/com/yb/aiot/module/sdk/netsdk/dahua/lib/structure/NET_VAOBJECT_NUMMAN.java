package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * 检测到的人信息
 * @author 29779
 */
public class NET_VAOBJECT_NUMMAN extends NetSDKLib.SdkStructure {
	public int                	nObjectID;                          // 物体ID，每个ID表示一个唯一的物体
	/**
	 * @link EM_UNIFORM_STYLE 制服样式 
	 */
	public int  				emUniformStyle;
	public NetSDKLib.NET_RECT stuBoundingBox;                     // 包围盒,手套对象在全景图中的框坐标,为0~8191相对坐标
	public NetSDKLib.NET_RECT stuOriginalBoundingBox;             // 包围盒,绝对坐标
	public byte[] 				byReserved = new byte[128];         // 预留字节
}
