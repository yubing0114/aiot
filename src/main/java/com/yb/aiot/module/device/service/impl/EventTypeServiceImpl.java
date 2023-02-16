package com.yb.aiot.module.device.service.impl;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.EventType;
import com.yb.aiot.module.device.entity.dto.UpdateEventTypeDto;
import com.yb.aiot.module.device.mapper.EventTypeMapper;
import com.yb.aiot.module.device.service.IEventTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 事件类型信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
@Service
public class EventTypeServiceImpl extends ServiceImpl<EventTypeMapper, EventType> implements IEventTypeService {

    @Resource
    private EventTypeMapper eventTypeMapper;


    public Result update(UpdateEventTypeDto updateEventTypeDto) {
        EventType eventType = getById(updateEventTypeDto.getId());
        if (ObjectUtils.isEmpty(eventType)) {
            return Result.fail(String.format("id为%s的事件类型不存在", updateEventTypeDto.getId()));
        }
        eventType.setLevel(updateEventTypeDto.getLevel());
        eventType.setSaveDay(updateEventTypeDto.getSaveDay());
        if (updateById(eventType)) {
            return Result.ok("更新事件类型成功");
        }
        return Result.fail("更新事件类型失败");
    }

    public Result all() {
        return Result.ok(list());
    }

}
