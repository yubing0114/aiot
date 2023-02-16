package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.sun.jna.Pointer;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

public class NET_OUT_DOWNLOAD_PIECE_FILE extends NetSDKLib.SdkStructure {
	public int				dwSize;
	/**
	 * 文件总长度
	 */
	public int				nFileLength;	
	/**
	 * 本次返回的数据长度,单位字节,建议为32KB,最大不超过4MB
	 */
	public int				nPacketLength;
	/**
	 * 本次请求的文件数据,由用户申请内存,大小为nBufferLen
	 */
	public Pointer          szBuffer;  
	/**
	 * 本次请求的文件数据长度,单位字节
	 */
	public int				nBufferLen;      
	 public NET_OUT_DOWNLOAD_PIECE_FILE() {
         this.dwSize = this.size();
     }	   
}
