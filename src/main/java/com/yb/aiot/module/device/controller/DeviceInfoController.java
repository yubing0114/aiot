package com.yb.aiot.module.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.dto.AddDeviceDTO;
import com.yb.aiot.module.device.entity.dto.QueryDeviceDTO;
import com.yb.aiot.module.device.entity.dto.UpdateDeviceNormalDTO;
import com.yb.aiot.module.device.entity.dto.UpdateDeviceThingsModelDTO;
import com.yslz.aiot.module.device.entity.dto.*;
import com.yb.aiot.module.device.service.IDeviceInfoService;
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
@Api(tags = "设备管理")
@RestController
@RequestMapping("/device/device-info")
public class DeviceInfoController {

    @Resource
    private IDeviceInfoService deviceInfoService;

    @ApiOperation("分页请求设备数据")
    @PostMapping(value = "/select/page")
    @SaCheckPermission(AuthConst.USER)
    public Result pageList(@RequestBody QueryDeviceDTO query){
        return deviceInfoService.selectByPage(query.getPageIndex(),query.getPageSize(),query);
    }

    @ApiOperation("分页请求摄像头设备")
    @PostMapping(value="/select/camera/page")
    @SaCheckPermission(AuthConst.USER)
    public Result cameraPageList(@RequestBody QueryDeviceDTO query){
        return deviceInfoService.selectCameraByPage(query.getPageIndex(), query.getPageSize(), query);
    }

    @ApiOperation("添加设备")
    @PostMapping(value="/add")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result addDevice(@RequestBody AddDeviceDTO addData){
        return deviceInfoService.addDevice(addData);
    }

    @ApiOperation("修改设备基础信息")
    @PostMapping(value="/update/normal")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result updateDeviceNormal(@RequestBody UpdateDeviceNormalDTO updateData){ return deviceInfoService.updateDeviceNormal(updateData); }

    @ApiOperation("修改设备物模型信息")
    @PostMapping(value="/update/thingsModel")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result updateDeviceThingsModel(@RequestBody UpdateDeviceThingsModelDTO updateData){
        return deviceInfoService.updateDeviceThingsModel(updateData);
    }

    @ApiOperation("根据Id获取设备")
    @GetMapping(value="/select/{deviceId}")
    @SaCheckPermission(AuthConst.USER)
    public Result selectById(@PathVariable("deviceId") Integer deviceId){
        return deviceInfoService.selectById(deviceId);
    }

    @ApiOperation("根据Id获取设备完整地址")
    @GetMapping(value="/select/address/{deviceId}")
    @SaCheckPermission(AuthConst.USER)
    public Result selectAddressConcatById(@PathVariable("deviceId") Integer deviceId){
        return deviceInfoService.selectAddressConcatById(deviceId);
    }

    @ApiOperation("根据Id删除设备")
    @GetMapping(value="/delete/{deviceId}")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result deleteById(@PathVariable("deviceId") Integer deviceId){
        return deviceInfoService.deleteById(deviceId);
    }

    @ApiOperation("检测并更新设备在线状态")
    @GetMapping(value="/checkIsOnline/{deviceIp}")
    @SaCheckPermission(AuthConst.USER)
    public Result selectById(@PathVariable("deviceIp") String deviceIp) {
        return deviceInfoService.checkIsOnline(deviceIp);
    }

}
