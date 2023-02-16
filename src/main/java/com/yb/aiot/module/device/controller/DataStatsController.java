package com.yb.aiot.module.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yslz.aiot.module.device.entity.dto.*;
import com.yb.aiot.module.device.service.IDeviceInfoService;
import com.yb.aiot.module.device.service.IEventInfoService;
import com.yb.aiot.module.device.service.IProductInfoService;
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
 * @since 2022-11-24
 */
@Api(tags = "设备统计")
@RestController
@RequestMapping("/device/dataStats")
public class DataStatsController {

    @Resource
    private IDeviceInfoService deviceInfoService;

    @Resource
    private IProductInfoService productInfoService;

    @Resource
    private IEventInfoService eventInfoService;

    @ApiOperation("获取所有产品分类产品数量")
    @GetMapping(value="/count/product/type")
    @SaCheckPermission(AuthConst.USER)
    public Result countProductByType(){
        return productInfoService.countProductByType();
    }

    @ApiOperation("获取所有产品分类设备数量")
    @GetMapping(value="/count/device/type")
    @SaCheckPermission(AuthConst.USER)
    public Result countDeviceByType() { return deviceInfoService.countDevicePieByType(); }

    @ApiOperation("获取设备运行状态统计数据")
    @GetMapping(value="/count/device/status")
    @SaCheckPermission(AuthConst.USER)
    public Result countDeviceByStatus() { return deviceInfoService.countDeviceByStatus(); }

    @ApiOperation("获取设备布防状态统计数据")
    @GetMapping(value="/count/device/alarmStatus")
    @SaCheckPermission(AuthConst.USER)
    public Result countDeviceByAlarmStatus() { return deviceInfoService.countDeviceByAlarmStatus(); }

    @ApiOperation("获取7日内每日事件总数")
    @GetMapping(value="/count/device/event/sevenDays")
    @SaCheckPermission(AuthConst.USER)
    public Result countSevenDaysEvent(){
        return eventInfoService.countSevenDaysEvent();
    }

    @ApiOperation("获取未处理事件总数")
    @GetMapping(value="/count/device/event/unExecute")
    @SaCheckPermission(AuthConst.USER)
    public Result countUnExecuteEvent(){
        return eventInfoService.countUnExecuteEvent();
    }
}
