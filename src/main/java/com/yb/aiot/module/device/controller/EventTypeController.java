package com.yb.aiot.module.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.dto.UpdateEventTypeDto;
import com.yb.aiot.module.device.service.IEventTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 事件类型信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
@RestController
@RequestMapping("/device/event-type")
@Api(tags = "事件类型")
public class EventTypeController {

    @Resource
    private IEventTypeService eventTypeService;

    @ApiOperation("更新事件类型信息")
    @PostMapping(value = "/update")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result executeEvent(@RequestBody UpdateEventTypeDto updateEventTypeDto) {
        return eventTypeService.update(updateEventTypeDto);
    }

}
