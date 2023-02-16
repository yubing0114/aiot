package com.yb.aiot.module.sdk.netsdk.dahua.lib.structure;

import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;

/**
 * 课程信息和ID
 *
 * @author ： 47040
 * @since ： Created in 2020/9/17 21:13
 */
public class NET_COURSE_RESULT extends NetSDKLib.SdkStructure {
    /**
     * 课程信息
     */
    public NET_COURSE stuCourseInfo;
    /**
     * 课程 ID
     */
    public int nId;
    /**
     * 保留字节
     */
    public byte[] byReserved = new byte[4];
}
