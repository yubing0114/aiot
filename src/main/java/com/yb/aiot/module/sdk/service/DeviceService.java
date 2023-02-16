package com.yb.aiot.module.sdk.service;

import com.alibaba.fastjson.JSONObject;
import com.yb.aiot.module.device.entity.DeviceInfo;
import com.yb.aiot.module.device.entity.EventInfo;
import com.yb.aiot.module.device.entity.EventType;
import com.yb.aiot.module.device.mapper.DeviceInfoMapper;
import com.yb.aiot.module.device.mapper.EventTypeMapper;
import com.yb.aiot.module.device.service.IDeviceInfoService;
import com.yb.aiot.module.device.service.IEventInfoService;
import com.yb.aiot.module.device.service.IEventTypeService;
import com.yb.aiot.module.media.zlm.ZLMRESTfulUtils;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.sdk.dto.DeviceDto;
import com.yb.aiot.module.sdk.module.SdkModule;
import com.yb.aiot.module.sdk.utils.SdkUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Device服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Service
public class DeviceService {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private EventTypeMapper eventTypeMapper;

    @Resource
    private IEventTypeService eventTypeService;

    @Resource
    private IDeviceInfoService deviceInfoService;

    @Resource
    private IEventInfoService eventInfoService;

    @Resource
    ZLMRESTfulUtils zlmresTfulUtils;

    public Result loadSdk(String manufacturer) {
        if (SdkUtil.loadSdk(manufacturer)) {
            return Result.ok(true);
        }
        return Result.fail(false);
    }

    public Result control(JSONObject jsonObject) {
        DeviceDto deviceDto = SdkUtil.makeDeviceDto(jsonObject);
        List<DeviceDto> deviceDtoList = deviceInfoMapper.selectBaseInfoByIp(deviceDto.getIp());
        if (CollectionUtils.isEmpty(deviceDtoList)) {
            return Result.fail(String.format("ip为%s的设备不存在", deviceDto.getIp()));
        }
        DeviceDto deviceDtoTemp = deviceDtoList.get(0);
        deviceDto.setDeviceId(deviceDtoTemp.getDeviceId());
        deviceDto.setUsername(deviceDtoTemp.getUsername());
        deviceDto.setPassword(deviceDtoTemp.getPassword());
        deviceDto.setPort(deviceDtoTemp.getPort());
        deviceDto.setStreamUrl(deviceDtoTemp.getStreamUrl());
        SdkModule sdkModule = deviceDto.getSdkModule();
        Result result;
        switch (deviceDto.getMethod()) {
            case "transcode":
                if (ObjectUtils.isEmpty(deviceDto.getStreamAddr())) {
                    deviceDto.setStreamAddr(deviceDto.getIp());
                }
                result = transcode(deviceDto.getStreamAddr());
                break;
            default:
                result = control(deviceDto.getMethod(), deviceDto, sdkModule);
                break;
        }
        return result;
    }

    public Result control(String method, DeviceDto deviceDto, SdkModule sdkModule) {
        boolean flag = false;
        switch (method) {
            case "openAlarm":
                flag = sdkModule.openAlarm(deviceDto) && updateAlarmStatus(deviceDto.getDeviceId(), true);
                break;
            case "closeAlarm":
                flag = sdkModule.closeAlarm(deviceDto.getIp()) && updateAlarmStatus(deviceDto.getDeviceId(), false);
                break;
            case "controlDoor":
                flag = sdkModule.controlDoor(deviceDto);
                break;
            case "controlGate":
                flag = sdkModule.controlGate(deviceDto);
                break;
            case "ptzControl":
                flag = sdkModule.ptzControl(deviceDto);
                break;
            case "reboot":
                flag = sdkModule.reboot(deviceDto);
                break;
        }
        if (flag) {
            return Result.ok("操作成功");
        }
        return Result.fail("操作失败");
    }

    public boolean updateAlarmStatus(Integer deviceId, boolean alarmStatus) {
        DeviceInfo deviceInfo = deviceInfoService.getById(deviceId);
        deviceInfo.setAlarmStatus(alarmStatus);
        return deviceInfoService.updateById(deviceInfo);
    }

    public Result transcode(String streamAddr) {
        String pattern = "(((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
        Matcher matcher = Pattern.compile(pattern).matcher(streamAddr);
        String streamUrl = streamAddr;
        if (matcher.matches()) {
            List<DeviceDto> deviceDtoList = deviceInfoMapper.selectBaseInfoByIp(streamAddr);
            if (CollectionUtils.isEmpty(deviceDtoList)) {
                return Result.fail(String.format("ip为%s的设备不存在", streamAddr));
            }
            DeviceDto deviceDto = deviceDtoList.get(0);
            streamUrl = String.format("rtsp://%s:%s@%s%s", deviceDto.getUsername(), deviceDto.getPassword(), streamAddr, deviceDto.getStreamUrl());
        }
        return zlmresTfulUtils.proxy(null, streamUrl);
    }

    public DeviceDto getDeviceDto(String deviceIp) {
        List<DeviceDto> deviceDtoList = deviceInfoMapper.selectBaseInfoByIp(deviceIp);
        if (CollectionUtils.isEmpty(deviceDtoList)) {
            return null;
        }
        DeviceDto deviceDto = deviceDtoList.get(0);
        String manufacturer = SdkUtil.getManufacturer(deviceDto.getThingsModel());
        if (ObjectUtils.isEmpty(manufacturer)) {
            return null;
        }
        SdkModule module = SdkUtil.getModule(manufacturer);
        deviceDto.setSdkModule(module);
        return deviceDto;
    }

    /**
     * 根据设备ip查询
     *
     * @param deviceIp 设备ip
     * @return common.com.yb.aiot.Result
     */
    public Result selectBaseInfoByIp(String deviceIp) {
        List<DeviceDto> deviceDtoList = deviceInfoMapper.selectBaseInfoByIp(deviceIp);
        if (CollectionUtils.isEmpty(deviceDtoList)) {
            return Result.fail("设备不存在");
        }
        return Result.ok(deviceDtoList.get(0));
    }

    public Result saveEventInfo(EventInfo eventInfo) {
        if (eventInfoService.save(eventInfo)) {
            return Result.ok();
        }
        return Result.fail();
    }

    public Result selectOrSaveEventType(EventType eventType) {
        EventType selectEventType = selectEventType(eventType.getProductInfoId(), eventType.getName());
        if (ObjectUtils.isEmpty(selectEventType)) {
            eventTypeService.save(eventType);
            selectEventType = selectEventType(eventType.getProductInfoId(), eventType.getName());
        }
        return Result.ok(selectEventType);
    }

    public EventType selectEventType(Integer productInfoId, String name) {
        List<EventType> eventTypeList = eventTypeMapper.selectByProductTypeIdAndName(productInfoId, name);
        if (CollectionUtils.isEmpty(eventTypeList)) {
            return null;
        }
        return eventTypeList.get(0);
    }

}
