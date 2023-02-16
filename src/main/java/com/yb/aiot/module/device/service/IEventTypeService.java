package com.yb.aiot.module.device.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.EventType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.device.entity.dto.UpdateEventTypeDto;

/**
 * <p>
 * 事件类型信息表 服务类
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
public interface IEventTypeService extends IService<EventType> {

    /**
     * 更新事件类型信息
     *
     * @param updateEventTypeDto 更新事件类型信息Dto
     * @return common.com.yb.aiot.Result
     */
    Result update(UpdateEventTypeDto updateEventTypeDto);

}
