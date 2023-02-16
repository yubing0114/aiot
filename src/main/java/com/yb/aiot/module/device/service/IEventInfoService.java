package com.yb.aiot.module.device.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.EventInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.device.entity.dto.ExecuteEventDTO;
import com.yb.aiot.module.device.entity.dto.QueryEventDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface IEventInfoService extends IService<EventInfo> {


    /**
     * 分页请求事件信息
     * @param pageIndex
     * @param pageSize
     * @return common.com.yb.aiot.Result
     */
    Result pageList(QueryEventDTO queryData);

    /**
     * 处理事件
     * @param executeData
     * @return common.com.yb.aiot.Result
     */
    Result executeEvent(ExecuteEventDTO executeData);

    /**
     * 获取7日内事件数
     * @return common.com.yb.aiot.Result
     */
    Result countSevenDaysEvent();

    /**
     * 统计未处理事件数
     * @return common.com.yb.aiot.Result
     */
    Result countUnExecuteEvent();

    /**
     * 清除不在保存时间内的事件
     *
     * @return common.com.yb.aiot.Result
     */
    Result deleteByEventSaveDay();

}
