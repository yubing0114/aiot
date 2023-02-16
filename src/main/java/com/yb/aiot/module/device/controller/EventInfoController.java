package com.yb.aiot.module.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.dto.ExecuteEventDTO;
import com.yb.aiot.module.device.entity.dto.QueryEventDTO;
import com.yb.aiot.module.device.service.IEventInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Api(tags = "事件统计")
@RestController
@RequestMapping("/device/event-info")
public class EventInfoController {

    @Resource
    private IEventInfoService eventInfoService;

    @ApiOperation("分页请求事件信息")
    @PostMapping(value="/select/list")
    @SaCheckPermission(AuthConst.USER)
    public Result pageList(@RequestBody QueryEventDTO queryData){
        return eventInfoService.pageList(queryData);
    }

    @ApiOperation("事件处理")
    @PostMapping(value = "/execute")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result executeEvent(@RequestBody ExecuteEventDTO executeData){
        return eventInfoService.executeEvent(executeData);
    }

    @ApiOperation("清除不在保存时间内的事件")
    @GetMapping(value="/deleteByEventSaveDay")
//    @SaCheckPermission(AuthConst.USER)
    public Result deleteByEventSaveDay(){
        return eventInfoService.deleteByEventSaveDay();
    }

}
