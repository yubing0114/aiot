/**
 * Copyright (C),  2011-2020, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : LapiUriConstat
 * Author   : z06274
 * Date     : 2020/7/16 20:23
 * DESCRIPTION: 与设备请求交互接口URI地址
 * <p>
 * History:
 * DATE        NAME        DESC
 */
package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.constant;

/**
 * @author z06274
 * @date 2020/7/16 20:23
 * @description 与设备请求交互接口URI地址
 */
public interface LapiUriConstat {

    /**
     * 心跳保活接口URI
     */
    String HEART_URI = "/LAPI/V1.0/PACS/Controller/HeartReportInfo";

    /**
     * 出入记录接收URI
     */
    String ACCESS_RECEIVE_URI = "/LAPI/V1.0/System/Event/Notification/PersonVerification";

    /**
     * 出入记录结果返回URI
     */
    String ACCESS_RESPONSE_URI = "/LAPI/V1.0/PACS/Controller/Event/Notifications";

    /**
     * 人员库信息查询URI
     */
    String PEOPLE_BASIC_INFO_URI = "/LAPI/V1.0/PeopleLibraries/BasicInfo";

}
