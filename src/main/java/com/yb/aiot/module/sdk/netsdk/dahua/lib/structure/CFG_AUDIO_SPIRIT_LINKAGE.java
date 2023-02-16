package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.constant.SDKStructureFieldLenth;

/**
 * @author 47081
 * @version 1.0
 * @description 语音激励联动项
 * @date 2020/11/9
 */
public class CFG_AUDIO_SPIRIT_LINKAGE extends NetSDKLib.SdkStructure {
    /**
     * 矩阵输出口数量
     */
    public int				nOutputNum;
    /**
     * 同步大画面输出到(多个)矩阵输出口
     */
    public int[]				nOutputChanel=new int[SDKStructureFieldLenth.CFG_MAX_LOWER_MATRIX_OUTPUT];

}
