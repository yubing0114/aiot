package com.yb.aiot.module.sdk.controller;

import com.alibaba.fastjson.JSONObject;
import com.yb.aiot.module.device.entity.EventInfo;
import com.yb.aiot.module.device.entity.EventType;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.sdk.dto.ControlDto;
import com.yb.aiot.module.sdk.dto.PtzDto;
import com.yb.aiot.module.sdk.module.hik.HikPlatform;
import com.yb.aiot.module.sdk.service.DeviceService;
import com.yb.aiot.module.sdk.service.SdkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Api(tags = "设备")
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;
    @Resource
    private SdkService sdkService;

    @ApiOperation("SDK加载")
    @GetMapping("/loadSdk/{manufacturer}")
    public Result loadSdk(@PathVariable("manufacturer") String manufacturer) {
        return deviceService.loadSdk(manufacturer);
    }

    @ApiOperation("设备操作")
    @PostMapping(value = "/control")
    public Result controlDoor(@RequestBody JSONObject jsonObject) {
        return deviceService.control(jsonObject);
    }

    @ApiOperation("设备登陆")
    @GetMapping(value = "/login/{deviceIp}")
    public Result login(@PathVariable("deviceIp") String deviceIp) {
        return sdkService.login(deviceIp);
    }

    @ApiOperation("布防")
    @GetMapping(value = "/openAlarm/{deviceIp}")
    public Result openAlarm(@PathVariable("deviceIp") String deviceIp) {
        return sdkService.openAlarm(deviceIp);
    }

    @ApiOperation("撤防")
    @GetMapping(value = "/closeAlarm/{deviceIp}")
    public Result closeAlarm(@PathVariable("deviceIp") String deviceIp) {
        return sdkService.closeAlarm(deviceIp);
    }

    @ApiOperation("云台控制")
    @PostMapping(value = "/ptzControl")
    public Result ptzControl(@RequestBody PtzDto devicePtzDto) {
        return sdkService.ptzControl(devicePtzDto);
    }

    @ApiOperation("道闸控制")
    @PostMapping(value = "/controlGate")
    public Result controlGate(@RequestBody ControlDto controlDto) {
        return sdkService.controlGate(controlDto);
    }

    @ApiOperation("远程控门")
    @PostMapping(value = "/controlDoor")
    public Result controlDoor(@RequestBody ControlDto controlDto) {
        return sdkService.controlDoor(controlDto);
    }

    @ApiIgnore
    @ApiOperation("订阅停车场事件")
    @PostMapping(value = "/eventRcv")
    public Result eventRcv(@RequestBody JSONObject eventData) {
        return HikPlatform.analyzeEventRcv(eventData);
    }

    @ApiOperation("重启")
    @GetMapping(value = "/reboot/{deviceIp}")
    public Result reboot(@PathVariable("deviceIp") String deviceIp) {
        return sdkService.reboot(deviceIp);
    }

    @ApiOperation("抓图")
    @GetMapping(value = "/capture/{deviceIp}")
    public Result capture(@PathVariable("deviceIp") String deviceIp) {
        return sdkService.capture(deviceIp);
    }

    @ApiIgnore
    @ApiOperation("获取卡信息")
    @GetMapping(value = "/findCard/{deviceIp}")
    public Result findCard(@PathVariable("deviceIp") String deviceIp) {
        return sdkService.findCard(deviceIp);
    }

    @ApiOperation("视频转码")
    @GetMapping("/transcode/{streamAddr}")
    public Result transcode(@PathVariable("streamAddr") String streamAddr) {
        return deviceService.transcode(streamAddr);
    }

    @ApiIgnore
    @PostMapping(value = "/selectOrSaveEventType")
    public Result selectOrSaveEventType(@RequestBody EventType eventType) {
        return deviceService.selectOrSaveEventType(eventType);
    }

    @ApiIgnore
    @GetMapping(value = "/select/{deviceIp}")
    public Result selectByIp(@PathVariable("deviceIp") String deviceIp) {
        return deviceService.selectBaseInfoByIp(deviceIp);
    }

    @ApiIgnore
    @PostMapping(value = "/eventInfo/save")
    public Result saveEventInfo(@RequestBody EventInfo eventInfo) {
        return deviceService.saveEventInfo(eventInfo);
    }

}
